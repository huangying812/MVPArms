package com.plain.permission

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.Size
import java.util.*

/**
 * description : 权限相关工具
 * 2018/6/14
 */
object PermissionUtil {

//    @JvmStatic
//    fun hasPermissions(@Size(min = 1) vararg perms: String) = hasPermissions(ContextHolder.getContext(), *perms)

    @JvmStatic
    fun hasPermissions(context: Context, @Size(min = 1) vararg perms: String): Boolean {
        return isHasPermission(context, *perms)
    }

//    @JvmStatic
//    fun hasPermissions(@Size(min = 1) perms: List<String>) = hasPermissions(ContextHolder.getContext(), perms)

    @JvmStatic
    fun hasPermissions(context: Context, @Size(min = 1) perms: List<String>): Boolean {
        val strings = Array(perms.size) { "" }
        perms.forEachIndexed { index, it ->
            strings[index] = it
        }
        return isHasPermission(context, strings)
    }

    @JvmStatic
    fun somePermissionPermanentlyDenied(host: Activity, deniedPermissions: List<String>): Boolean {
        return checkMorePermissionPermanentDenied(host, deniedPermissions)
    }

    @JvmStatic
    fun somePermissionPermanentlyDenied(host: Activity, @Size(min = 1) vararg perms: String): Boolean {
        return checkMorePermissionPermanentDenied(host, perms.toList())
    }

    @JvmStatic
    fun permissionPermanentlyDenied(host: Activity, deniedPermission: String): Boolean {
        return checkSinglePermissionPermanentDenied(host, deniedPermission)
    }

    /**
     * 检查某些权限是否全部授予了
     *
     * @param context     上下文对象
     * @param permissions 需要请求的权限组
     */
    @JvmStatic
    fun isHasPermission(context: Context, vararg permissions: String): Boolean {
        val failPermissions = getFailPermissions(context, Arrays.asList(*permissions))
        return failPermissions == null || failPermissions.isEmpty()
    }

    /**
     * 检查某些权限是否全部授予了
     *
     * @param context     上下文对象
     * @param permissions 需要请求的权限组
     */
    @JvmStatic
    fun isHasPermission(context: Context, vararg permissions: Array<String>): Boolean {
        val permissionList = ArrayList<String>()
        for (group in permissions) {
            permissionList.addAll(Arrays.asList(*group))
        }
        val failPermissions = getFailPermissions(context, permissionList)
        return failPermissions == null || failPermissions.isEmpty()
    }

    /**
     * 是否是6.0以上版本
     */
    @JvmStatic
    inline val isOverMarshmallow: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /**
     * 是否是8.0以上版本
     */
    @JvmStatic
    inline val isOverOreo: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * 返回应用程序在清单文件中注册的权限
     */
    @JvmStatic
    fun getManifestPermissions(context: Context): List<String>? {
        try {
            return Arrays.asList(*context.packageManager.getPackageInfo(context.packageName,
                    PackageManager.GET_PERMISSIONS).requestedPermissions)
        } catch (ignored: PackageManager.NameNotFoundException) {
            return null
        }

    }

    /**
     * 是否有安装权限
     */
    @JvmStatic
    @SuppressLint("NewApi")
    fun isHasInstallPermission(context: Context): Boolean {
        return if (isOverOreo) {
            context.packageManager.canRequestPackageInstalls()
        } else true
    }

    /**
     * 是否有悬浮窗权限
     */
    @JvmStatic
    @SuppressLint("NewApi")
    fun isHasOverlaysPermission(context: Context): Boolean {
        return if (isOverMarshmallow) {
            Settings.canDrawOverlays(context)
        } else true
    }

    /**
     * 获取没有授予的权限
     *
     * @param context               上下文对象
     * @param permissions           需要请求的权限组
     */
    @JvmStatic
    fun getFailPermissions(context: Context, permissions: List<String>): ArrayList<String>? {

        // 如果是安卓6.0以下版本就返回null
        if (!isOverMarshmallow) {
            return null
        }

        var failPermissions: ArrayList<String>? = null

        for (permission in permissions) {

            // 检测安装权限
            if (permission == Permission.REQUEST_INSTALL_PACKAGES) {
                if (!isHasInstallPermission(context)) {
                    if (failPermissions == null) failPermissions = ArrayList()
                    failPermissions.add(permission)
                }
                continue
            }

            // 检测悬浮窗权限
            if (permission == Permission.SYSTEM_ALERT_WINDOW) {

                if (!isHasOverlaysPermission(context)) {
                    if (failPermissions == null) failPermissions = ArrayList()
                    failPermissions.add(permission)
                }
                continue
            }

            // 检测8.0的两个新权限
            if (permission == Permission.ANSWER_PHONE_CALLS || permission == Permission.READ_PHONE_NUMBERS) {

                // 检查当前的安卓版本是否符合要求
                if (!isOverOreo) {
                    continue
                }
            }

            // 把没有授予过的权限加入到集合中
            @SuppressLint("NewApi")
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                if (failPermissions == null) failPermissions = ArrayList()
                failPermissions.add(permission)
            }
        }

        return failPermissions
    }

    /**
     * 是否还能继续申请没有授予的权限
     *
     * @param activity              Activity对象
     * @param failPermissions       失败的权限
     */
    @JvmStatic
    fun isRequestDeniedPermission(activity: Activity, failPermissions: List<String>): Boolean {
        for (permission in failPermissions) {
            // 安装权限和浮窗权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回false
            if (permission == Permission.REQUEST_INSTALL_PACKAGES || permission == Permission.SYSTEM_ALERT_WINDOW) {
                continue
            }

            // 检查是否还有权限还能继续申请的（这里指没有被授予的权限但是也没有被永久拒绝的）
            if (!checkSinglePermissionPermanentDenied(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 在权限组中检查是否有某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permissions            请求的权限
     */
    @JvmStatic
    fun checkMorePermissionPermanentDenied(activity: Activity, permissions: List<String>): Boolean {
        for (permission in permissions) {
            // 安装权限和浮窗权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回false
            if (permission == Permission.REQUEST_INSTALL_PACKAGES || permission == Permission.SYSTEM_ALERT_WINDOW) {
                continue
            }
            if (checkSinglePermissionPermanentDenied(activity, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 检查某个权限是否被永久拒绝
     *
     * @param activity              Activity对象
     * @param permission            请求的权限
     */
    @JvmStatic
    fun checkSinglePermissionPermanentDenied(activity: Activity, permission: String): Boolean {

        //        // 安装权限和浮窗权限不算，本身申请方式和危险权限申请方式不同，因为没有永久拒绝的选项，所以这里返回false
        //        if (permission.equals(Permission.REQUEST_INSTALL_PACKAGES) || permission.equals(Permission.SYSTEM_ALERT_WINDOW)) {
        //            return false;
        //        }

        // 检测8.0的两个新权限
        if (permission == Permission.ANSWER_PHONE_CALLS || permission == Permission.READ_PHONE_NUMBERS) {

            // 检查当前的安卓版本是否符合要求
            if (!isOverOreo) {
                return false
            }
        }

        if (isOverMarshmallow) {
            @SuppressLint("NewApi")
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED && !activity.shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取没有授予的权限
     *
     * @param permissions           需要请求的权限组
     * @param grantResults          允许结果组
     */
    @JvmStatic
    fun getFailPermissions(permissions: Array<String>, grantResults: IntArray): List<String> {
        val failPermissions = ArrayList<String>()
        for (i in grantResults.indices) {

            // 把没有授予过的权限加入到集合中，-1表示没有授予，0表示已经授予
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                failPermissions.add(permissions[i])
            }
        }
        return failPermissions
    }

    /**
     * 获取已授予的权限
     *
     * @param permissions       需要请求的权限组
     * @param grantResults      允许结果组
     */
    @JvmStatic
    fun getSucceedPermissions(permissions: Array<String>, grantResults: IntArray): List<String> {

        val succeedPermissions = ArrayList<String>()
        for (i in grantResults.indices) {

            // 把授予过的权限加入到集合中，-1表示没有授予，0表示已经授予
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                succeedPermissions.add(permissions[i])
            }
        }
        return succeedPermissions
    }

    /**
     * 检测权限有没有在清单文件中注册
     *
     * @param activity              Activity对象
     * @param requestPermissions    请求的权限组
     */
    @JvmStatic
    fun checkPermissions(activity: Activity, requestPermissions: List<String>) {
        val manifestPermissions = getManifestPermissions(activity)
        if (manifestPermissions != null && !manifestPermissions.isEmpty()) {
            for (permission in requestPermissions) {
                if (!manifestPermissions.contains(permission)) {
                    throw ManifestRegisterException(permission)
                }
            }
        } else {
            throw ManifestRegisterException(null)
        }
    }

    /**
     * 检查targetSdkVersion是否符合要求
     *
     * @param context                   上下文对象
     * @param requestPermissions       请求的权限组
     */
    @JvmStatic
    fun checkTargetSdkVersion(context: Context, requestPermissions: List<String>) {
        // 检查是否包含了8.0的权限
        if (requestPermissions.contains(Permission.REQUEST_INSTALL_PACKAGES)
                || requestPermissions.contains(Permission.ANSWER_PHONE_CALLS)
                || requestPermissions.contains(Permission.READ_PHONE_NUMBERS)) {
            // 必须设置 targetSdkVersion >= 26 才能正常检测权限
            if (context.applicationInfo.targetSdkVersion < Build.VERSION_CODES.O) {
                throw RuntimeException("The targetSdkVersion SDK must be 26 or more")
            }
        } else {
            // 必须设置 targetSdkVersion >= 23 才能正常检测权限
            if (context.applicationInfo.targetSdkVersion < Build.VERSION_CODES.M) {
                throw RuntimeException("The targetSdkVersion SDK must be 23 or more")
            }
        }
    }


    interface Callback {
        fun onDenied(permissions: List<String>?)
        fun onGranted(permissions: List<String>?)
    }

}

fun Context.getPermissionSettingPageIntent() = PermissionSettingPage.getIntent(this)
