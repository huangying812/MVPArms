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
package sl.recharge.call.mvp.contract;

import android.app.Activity;

import sl.recharge.call.mvp.model.entity.User;
import com.plain.mvp.IModel;
import com.plain.mvp.IView;

import java.util.List;

import io.reactivex.Observable;

/**
 * ================================================
 * 展示 Contract 的用法
 * ================================================
 */
public interface UserContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void startLoadMore();
        void endLoadMore();
        Activity getActivity();
    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
    interface Model extends IModel{
        Observable<List<User>> getUsers(int lastIdQueried, boolean update);
    }
}
