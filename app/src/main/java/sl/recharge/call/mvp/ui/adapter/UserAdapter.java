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
package sl.recharge.call.mvp.ui.adapter;

import android.view.View;

import sl.recharge.call.R;
import sl.recharge.call.mvp.model.entity.User;
import sl.recharge.call.mvp.ui.holder.UserItemHolder;
import com.plain.base.BaseHolder;
import com.plain.base.DefaultAdapter;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * at 09/04/2016 12:57
 * ================================================
 */
public class UserAdapter extends DefaultAdapter<User> {

    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @NonNull
    @Override
    public BaseHolder<User> getHolder(@NonNull View v, int viewType) {
        return new UserItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }
}
