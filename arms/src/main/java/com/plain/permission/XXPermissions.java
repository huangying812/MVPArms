package com.plain.permission;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.FragmentActivity;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/XXPermissions
 *    time   : 2018/06/15
 *    desc   : Android 危险权限请求类
 */
public final class XXPermissions {

    private FragmentActivity mActivity;
    private List<String> mPermissions;
    private boolean mConstant;

    /**
     * 私有化构造函数
     */
    private XXPermissions(FragmentActivity activity) {
        mActivity = activity;
    }

    /**
     * 设置请求的对象
     */
    public static XXPermissions with(FragmentActivity activity) {
        return new XXPermissions(activity);
    }

    /**
     * 设置权限组
     */
    public XXPermissions permission(String... permissions) {
        if (mPermissions == null) {
            mPermissions = new ArrayList<>(permissions.length);
        }
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    /**
     * 设置权限组
     */
    public XXPermissions permission(String[]... permissions) {
        if (mPermissions == null) {
            int length = 0;
            for (String[] permission : permissions) {
                length += permission.length;
            }
            mPermissions = new ArrayList<>(length);
        }
        for (String[] group : permissions) {
            mPermissions.addAll(Arrays.asList(group));
        }
        return this;
    }

    /**
     * 设置权限组
     */
    public XXPermissions permission(List<String> permissions) {
        if (mPermissions == null) {
            mPermissions = permissions;
        }else {
            mPermissions.addAll(permissions);
        }
        return this;
    }

    /**
     * 被拒绝后继续申请，直到授权或者永久拒绝
     */
    public XXPermissions constantRequest() {
        mConstant = true;
        return this;
    }

    /**
     * 请求权限
     */
    public void request(OnPermission call) {
        // 如果没有指定请求的权限，就使用清单注册的权限进行请求
        if (mPermissions == null || mPermissions.isEmpty()) {
            mPermissions = PermissionUtil.getManifestPermissions(mActivity);
        }
        if (mPermissions == null || mPermissions.isEmpty()) {
            throw new IllegalArgumentException("The requested permission cannot be empty");
        }
        // 使用isFinishing方法Activity在熄屏状态下会导致崩溃
        // if (mActivity == null || mActivity.isFinishing()) throw new IllegalArgumentException("Illegal Activity was passed in");
        if (mActivity == null) {
            throw new IllegalArgumentException("The activity is empty");
        }
        if (call == null) {
            throw new IllegalArgumentException("The permission request callback interface must be implemented");
        }

        PermissionUtil.checkTargetSdkVersion(mActivity, mPermissions);

        ArrayList<String> failPermissions = PermissionUtil.getFailPermissions(mActivity, mPermissions);

        if (failPermissions == null || failPermissions.isEmpty()) {
            // 证明权限已经全部授予过
            call.hasPermission(mPermissions, true);
        } else {
            // 检测权限有没有在清单文件中注册
            PermissionUtil.checkPermissions(mActivity, mPermissions);
            // 申请没有授予过的权限
            PermissionFragment.newInstance((new ArrayList<>(mPermissions)), mConstant).prepareRequest(mActivity, call);
        }
    }

    /**
     * 跳转到应用权限设置页面
     *
     * @param context 上下文对象
     */
    public static void gotoPermissionSettings(Context context) {
        PermissionSettingPage.start(context, false);
    }

    /**
     * 跳转到应用权限设置页面
     *
     * @param context 上下文对象
     * @param newTask 是否使用新的任务栈启动
     */
    public static void gotoPermissionSettings(Context context, boolean newTask) {
        PermissionSettingPage.start(context, newTask);
    }
}