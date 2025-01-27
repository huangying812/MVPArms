/*
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sl.recharge.call.mvp.presenter

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import sl.recharge.call.mvp.contract.UserContract
import sl.recharge.call.mvp.model.entity.User
import sl.recharge.call.mvp.ui.adapter.UserAdapter
import com.plain.di.scope.ActivityScope
import com.plain.integration.AppManager
import com.plain.mvp.BasePresenter
import com.plain.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * ================================================
 * 展示 Presenter 的用法
 * ================================================
 */
@ActivityScope
class UserPresenter @Inject
constructor(model: UserContract.Model, rootView: UserContract.View)
    : BasePresenter<UserContract.Model, UserContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mAppManager: AppManager
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mUsers: MutableList<User>
    @Inject
    lateinit var mAdapter: UserAdapter
    private var lastUserId = 1
    private var isFirst = true
    private var preEndIndex: Int = 0

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 `Presenter` 可以与 [SupportActivity] 和 [Fragment] 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    internal fun onCreate() {
        requestUsers(true)//打开 App 时自动加载列表
    }

    fun requestUsers(pullToRefresh: Boolean) {
        requestFromModel(pullToRefresh)
    }

    private fun requestFromModel(pullToRefresh: Boolean) {
        if (pullToRefresh) lastUserId = 1//下拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        var isEvictCache = pullToRefresh//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false
            isEvictCache = false
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe {
                    if (pullToRefresh)
                        mRootView.showLoading()//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore()//显示上拉加载更多的进度条
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    if (pullToRefresh)
                        mRootView.hideLoading()//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore()//隐藏上拉加载更多的进度条
                }
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    override fun onNext(users: List<User>) {
                        lastUserId = users[users.size - 1].id//记录最后一个id,用于下一次请求
                        if (pullToRefresh) mUsers.clear()//如果是下拉刷新则清空列表
                        preEndIndex = mUsers.size//更新之前列表总长度,用于确定加载更多的起始位置
                        mUsers.addAll(users)
                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged()
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size)
                    }
                })
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
