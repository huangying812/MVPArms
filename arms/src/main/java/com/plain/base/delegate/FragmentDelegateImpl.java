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
package com.plain.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.plain.utils.ArmsUtils;
import com.plain.integration.EventBusManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * ================================================
 * {@link FragmentDelegate} 默认实现类
 * <p>
 * at 29/04/2017 16:12
 * ================================================
 */
public class FragmentDelegateImpl implements FragmentDelegate {
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;

    public FragmentDelegateImpl(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        this.mFragmentManager = fragmentManager;
        this.mFragment = fragment;
        this.iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (iFragment.useEventBus())//如果要使用eventbus请将此方法返回true
            EventBusManager.getInstance().register(mFragment);//注册到事件主线
        iFragment.setupFragmentComponent(ArmsUtils.obtainAppComponentFromContext(mFragment.getActivity()));
    }

    @Override
    public void onCreateView(@Nullable View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {
        iFragment.initData(savedInstanceState);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroyView() {
    }

    @Override
    public void onDestroy() {
        if (iFragment != null && iFragment.useEventBus())//如果要使用eventbus请将此方法返回true
            EventBusManager.getInstance().unregister(mFragment);//注册到事件主线
        this.mFragmentManager = null;
        this.mFragment = null;
        this.iFragment = null;
    }

    @Override
    public void onDetach() {

    }

    /**
     * Return true if the fragment is currently added to its activity.
     */
    @Override
    public boolean isAdded() {
        return mFragment != null && mFragment.isAdded();
    }
}
