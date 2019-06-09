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
package sl.recharge.call.di.module

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sl.recharge.call.mvp.contract.UserContract
import sl.recharge.call.mvp.model.UserModel
import sl.recharge.call.mvp.model.entity.User
import sl.recharge.call.mvp.ui.adapter.UserAdapter
import com.plain.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * ================================================
 * 展示 Module 的用法
 * ================================================
 */
@Module
class UserModule(val view: UserContract.View) {

    @ActivityScope
    @Provides
    internal fun provideUserView(): UserContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    internal fun provideSplashModel(model: UserModel): UserContract.Model {
        return model
    }

    @ActivityScope
    @Provides
    internal fun provideLayoutManager(view: UserContract.View): RecyclerView.LayoutManager {
        return GridLayoutManager(view.activity, 2)
    }

    @ActivityScope
    @Provides
    fun provideUserList() = mutableListOf<User>()

    @ActivityScope
    @Provides
    fun provideUserAdapter(list: MutableList<User>) = UserAdapter(list)
}
