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
package sl.recharge.call.di.component

import sl.recharge.call.di.module.UserModule
import sl.recharge.call.mvp.ui.activity.UserActivity
import com.plain.di.component.AppComponent
import com.plain.di.scope.ActivityScope
import dagger.Component

/**
 * ================================================
 * 展示 Component 的用法
 * ================================================
 */
@ActivityScope
@Component(modules = [UserModule::class], dependencies = [AppComponent::class])
interface UserComponent {
    fun inject(activity: UserActivity)
}
