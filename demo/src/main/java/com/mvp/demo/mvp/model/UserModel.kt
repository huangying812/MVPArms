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
package com.mvp.demo.mvp.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.mvp.demo.mvp.contract.UserContract
import com.mvp.demo.mvp.model.api.cache.CommonCache
import com.mvp.demo.mvp.model.api.service.UserService
import com.mvp.demo.mvp.model.entity.User
import com.plain.di.scope.ActivityScope
import com.plain.integration.IRepositoryManager
import com.plain.mvp.BaseModel
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey
import timber.log.Timber
import javax.inject.Inject

/**
 * ================================================
 * 展示 Model 的用法
 * ================================================
 */
@ActivityScope
class UserModel @Inject
constructor(repositoryManager: IRepositoryManager)
    : BaseModel(repositoryManager), UserContract.Model {

    override fun getUsers(lastIdQueried: Int, update: Boolean): Observable<List<User>> {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService::class.java)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap { listObservable ->
                    mRepositoryManager.obtainCacheService(CommonCache::class.java)
                            .getUsers(listObservable, DynamicKey(lastIdQueried), EvictDynamicKey(update))
                            .map { listReply -> listReply.data }
                }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    internal fun onPause() {
        Timber.d("Release Resource")
    }

    companion object {
        val USERS_PER_PAGE = 10
    }

}
