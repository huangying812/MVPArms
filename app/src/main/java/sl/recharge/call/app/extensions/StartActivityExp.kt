package sl.recharge.call.app.extensions

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.plain.permission.Permission
import com.plain.permission.PermissionUtil
import com.plain.permission.getPermissionSettingPageIntent
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 启动Activity并接收Intent的扩展方法，不需要重写[#onActivityResult]方法
 * @receiver 基于[FragmentActivity]的扩展方法
 * @param params Array<out Pair<String, *>> 要携带的参数
 * @param options Bundle?   动画参数
 * @param callback (Intent) -> Unit 返回此界面时，当ResultCode为RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [LambdaHolder#onCanceled]或
 *          [LambdaHolder#onDefined]方法来设置ResultCode为RESULT_CANCELED或RESULT_FIRST_USER时的回调
 */
inline fun <reified F : FragmentActivity> FragmentActivity.startActivityForResult(
        vararg params: Pair<String, *>,
        options: Bundle? = null,
        noinline callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return startActivityForResult(intentFor<F>(*params), options, callback)
}


/**
 * 启动Activity并接收Intent的扩展方法，不需要重写[#onActivityResult]方法
 * @receiver 基于[Fragment]的扩展方法
 * @param params Array<out Pair<String, *>> 要携带的参数
 * @param options Bundle?   动画参数
 * @param callback (Intent) -> Unit 返回此界面时，当ResultCode为RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [#onCanceled]或[#onDefined]方法来
 *          设置ResultCode为RESULT_CANCELED或RESULT_FIRST_USER时的回调
 */
inline fun <reified F : FragmentActivity> Fragment.startActivityForResult(
        vararg params: Pair<String, *>,
        options: Bundle? = null,
        noinline callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return requireActivity().startActivityForResult(intentFor<F>(*params), options, callback)
}

suspend fun FragmentActivity.reqMediaPermission() = reqAppPermissions(Permission.NAME_PAIR_READ_EXTERNAL_STORAGE)

suspend fun FragmentActivity.reqAppPermissions(vararg permissions: Pair<String, String>) = suspendCoroutine<Boolean> { continuation ->

    val permission = Array(permissions.size) { "" }
    val permissionName = Array(permissions.size) { "" }
    permissions.forEachIndexed { index, pair ->
        permission[index] = (pair.first)
        permissionName[index] = (pair.second)
    }
    val activity = this
    reqPermissions(*permission) {
        continuation.resume(true)
    }.setOnDeniedCallback {
        launchUI {
            if (PermissionUtil.somePermissionPermanentlyDenied(activity, *permission)
                    && confirmSusp("需要权限", "-${permissionName.joinToString("\n-")}\n请同意", "去权限设置页面")) {
                startActivityForResult(getPermissionSettingPageIntent()) {
                    continuation.resume(PermissionUtil.hasPermissions(activity, *permission))
                }.setOnCanceledCallback {
                    continuation.resume(PermissionUtil.hasPermissions(activity, *permission))
                }
            } else {
                continuation.resume(false)
            }
        }
    }
}

