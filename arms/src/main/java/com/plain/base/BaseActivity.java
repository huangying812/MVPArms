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
package com.plain.base;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.widget.TextView;

import com.plain.R;
import com.plain.base.delegate.IActivity;
import com.plain.integration.cache.Cache;
import com.plain.integration.cache.CacheType;
import com.plain.integration.lifecycle.ActivityLifecycleable;
import com.plain.mvp.IPresenter;
import com.plain.utils.ArmsUtils;
import com.plain.utils.StatusManager;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@link Activity} 的三方库, 那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity}, 然后再按照 {@link BaseActivity} 的格式, 将代码复制过去, 记住一定要实现{@link IActivity}
 * ================================================
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, ActivityLifecycleable {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
            }
        } catch (Exception e) {
            if (e instanceof InflateException) throw e;
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        TextView tvTitle = findViewById(R.id.toolbar_title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        super.setTitle(titleId);
        TextView tvTitle = findViewById(R.id.toolbar_title);
        if (tvTitle != null) {
            tvTitle.setText(titleId);
        }
    }

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 {@code true} (默认为 {@code true}), Arms 会自动注册 EventBus
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个 {@link Activity} 是否会使用 {@link Fragment}, 框架会根据这个属性判断是否注册 {@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回 {@code false}, 那意味着这个 {@link Activity} 不需要绑定 {@link Fragment}, 那你再在这个 {@link Activity} 中绑定继承于 {@link BaseFragment} 的 {@link Fragment} 将不起任何作用
     *
     * @return 返回 {@code true} (默认为 {@code true}), 则需要使用 {@link Fragment}
     */
    @Override
    public boolean useFragment() {
        return true;
    }

    /**
     * 和 setContentView 对应的方法
     */
    public View getContentView() {
        return getWindow().getDecorView();
    }

    private final StatusManager mStatusManager = new StatusManager();

    /**
     * 显示加载中
     */
    public void showLoading() {
        mStatusManager.showLoading(this);
    }

    /**
     * 显示加载完成
     */
    public void showComplete() {
        mStatusManager.showComplete();
    }

    /**
     * 显示空提示
     */
    public void showEmpty() {
        mStatusManager.showEmpty(getContentView());
    }

    /**
     * 显示错误提示
     */
    public void showError() {
        mStatusManager.showError(getContentView());
    }

    /**
     * 显示自定义提示
     */
    public void showLayout(@DrawableRes int iconId, @StringRes int textId) {
        mStatusManager.showLayout(getContentView(), iconId, textId);
    }

    public void showLayout(Drawable drawable, CharSequence hint) {
        mStatusManager.showLayout(getContentView(), drawable, hint);
    }
}
