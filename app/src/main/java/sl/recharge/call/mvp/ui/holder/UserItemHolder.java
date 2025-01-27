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
package sl.recharge.call.mvp.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sl.recharge.call.R;
import sl.recharge.call.app.utils.IntentUtil;
import sl.recharge.call.mvp.model.entity.User;
import sl.recharge.call.mvp.ui.activity.WebActivity;
import com.plain.base.BaseHolder;
import com.plain.base.DefaultAdapter;
import com.plain.di.component.AppComponent;
import com.plain.glide.ImageConfigImpl;
import com.plain.http.imageloader.ImageLoader;
import com.plain.utils.ArmsUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * at 9/4/16 12:56
 * ================================================
 */
public class UserItemHolder extends BaseHolder<User> {

    ImageView mAvatar;
    TextView mName;
    private AppComponent mAppComponent;
    /**
     * 用于加载图片的管理类, 默认使用 Glide, 使用策略模式, 可替换框架
     */
    private ImageLoader mImageLoader;

    public UserItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方, 拿到 AppComponent, 从而得到用 Dagger 管理的单例对象
        mAvatar = itemView.findViewById(R.id.iv_avatar);
        mName = itemView.findViewById(R.id.tv_name);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(@NonNull User data, int position) {
        mName.setText(data.getLogin());

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        Context context = itemView.getContext();
        mImageLoader.loadImage(context,
                ImageConfigImpl
                        .builder()
                        .url(data.getAvatarUrl())
                        .imageView(mAvatar)
                        .build());
        itemView.setOnClickListener(v -> {
            /*MediaSelector.INSTANCE.selectPics((FragmentActivity) FuntionsKt.getActivity(context), (paths, uris) -> {
                ToastsKt.toast(context, "选中了：" + paths.size());
                return null;
            });*/
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra(WebActivity.EXTR_URL, data.getHomePage());
            intent.putExtra(WebActivity.EXTR_TITLE, data.getLogin());
            IntentUtil.startActivity(context, intent);
        });
    }

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        //只要传入的 Context 为 Activity, Glide 就会自己做好生命周期的管理, 其实在上面的代码中传入的 Context 就是 Activity
        //所以在 onRelease 方法中不做 clear 也是可以的, 但是在这里想展示一下 clear 的用法
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder()
                .imageViews(mAvatar)
                .build());
        this.mAvatar = null;
        this.mName = null;
        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}
