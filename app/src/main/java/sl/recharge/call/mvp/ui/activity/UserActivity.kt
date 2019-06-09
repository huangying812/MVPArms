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
package sl.recharge.call.mvp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.paginate.Paginate
import com.plain.base.BaseActivity
import com.plain.base.DefaultAdapter
import com.plain.di.component.AppComponent
import com.plain.utils.ArmsUtils
import com.plain.utils.Preconditions.checkNotNull
import kotlinx.android.synthetic.main.activity_user.*
import sl.recharge.call.R
import sl.recharge.call.di.component.DaggerUserComponent
import sl.recharge.call.di.module.UserModule
import sl.recharge.call.mvp.contract.UserContract
import sl.recharge.call.mvp.presenter.UserPresenter
import sl.recharge.call.mvp.ui.adapter.UserAdapter
import timber.log.Timber
import javax.inject.Inject


/**
 * ================================================
 * 展示 View 的用法
 * ================================================
 */
class UserActivity : BaseActivity<UserPresenter>(), UserContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    @Inject
    lateinit var mAdapter: UserAdapter

    private var mPaginate: Paginate? = null
    private var isLoadingMore: Boolean = false

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerUserComponent
                .builder()
                .appComponent(appComponent)
                .userModule(UserModule(this))
                .build()
                .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_user
    }

    override fun initData(savedInstanceState: Bundle?) {
        initRecyclerView()
        recyclerView.adapter = mAdapter
        initPaginate()
    }


    override fun onRefresh() {
        mPresenter?.requestUsers(true)
    }

    /**
     * 初始化RecyclerView
     */
    private fun initRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this)
        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager)
    }


    override fun showLoading() {
        Timber.tag(TAG).w("showLoading")
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        Timber.tag(TAG).w("hideLoading")
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showMessage(message: String) {
        checkNotNull(message)
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        checkNotNull(intent)
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }

    /**
     * 开始加载更多
     */
    override fun startLoadMore() {
        isLoadingMore = true
    }

    /**
     * 结束加载更多
     */
    override fun endLoadMore() {
        isLoadingMore = false
    }

    override fun getActivity(): Activity {
        return this
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private fun initPaginate() {
        if (mPaginate == null) {
            val callbacks = object : Paginate.Callbacks {
                override fun onLoadMore() {
                    mPresenter?.requestUsers(false)
                }

                override fun isLoading(): Boolean {
                    return isLoadingMore
                }

                override fun hasLoadedAllItems(): Boolean {
                    return false
                }
            }

            mPaginate = Paginate.with(recyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build()
            mPaginate?.setHasMoreDataToLoad(false)
        }
    }

    override fun onDestroy() {
        DefaultAdapter.releaseAllHolder(recyclerView)//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy()
        this.mPaginate = null
    }
}
