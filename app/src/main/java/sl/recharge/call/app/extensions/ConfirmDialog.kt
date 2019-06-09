package sl.recharge.call.app.extensions

import android.app.Dialog
import android.util.Range
import androidx.fragment.app.FragmentActivity
import com.plain.dialog.InputDialog
import com.plain.dialog.MessageDialog
import java.math.BigDecimal
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend inline fun FragmentActivity.confirmSusp(title: String, message: String = "", okBtn: String = "确定",
                                         cancelBtn: String = "取消", canceledOnTouchOutside: Boolean = false) = suspendCoroutine<Boolean> { continuation ->
    confirm(title, message, okBtn, cancelBtn, canceledOnTouchOutside) {
        continuation.resume(it)
    }
}

suspend fun FragmentActivity.inputSusp(title: String, hint: String = "请输入$title", defaultValue: String = "",
                                       maxEms: Int = Int.MAX_VALUE, valueRange: Range<BigDecimal>? = null,
                                       inputType: Int = 0, canceledOnTouchOutside: Boolean = false) = suspendCoroutine<Pair<Boolean, String?>> { continuation ->
    input(title, hint, defaultValue, maxEms, valueRange, inputType, canceledOnTouchOutside) {
        continuation.resume(it)
    }
}

fun FragmentActivity.confirm(title: String, message: String = "", okBtn: String = "确定",
                             cancelBtn: String = "取消", canceledOnTouchOutside: Boolean = false,
                             callback: (Boolean) -> Unit) {
    MessageDialog.Builder(this)
            .setTitle(title) // 标题可以不用填写
            .setMessage(message)
            .setConfirm(okBtn)
            .setCancel(cancelBtn) // 设置 null 表示不显示取消按钮
            //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
            .setListener(object : MessageDialog.OnListener {

                override fun onConfirm(dialog: Dialog) {
                    callback(true)
                }

                override fun onCancel(dialog: Dialog) {
                    callback(false)
                }
            })
            .addOnCancelListener {
                callback(false)
            }
            .show().apply {
                setCanceledOnTouchOutside(canceledOnTouchOutside)
            }
}

fun FragmentActivity.input(title: String, hint: String = "请输入$title", defaultValue: String = "",
                           maxEms: Int = Int.MAX_VALUE, valueRange: Range<BigDecimal>? = null,
                           inputType: Int = 0, canceledOnTouchOutside: Boolean = false,
                           callback: (Pair<Boolean, String?>) -> Unit) {
    InputDialog.Builder(this)
            .setTitle(title) // 标题可以不用填写
            .setContent(defaultValue)
            .setHint(hint)
            .setInputType(inputType)
            .setMaxEms(maxEms)
            .setValueRange(valueRange)
            .setConfirm("确定")
            .setCancel("取消") // 设置 null 表示不显示取消按钮
            //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
            .setListener(object : InputDialog.OnListener {
                override fun onConfirm(dialog: Dialog?, content: String?) {
                    callback(true to content)
                }

                override fun onCancel(dialog: Dialog) {
                    callback(false to null)
                }
            })
            .addOnCancelListener {
                callback(false to null)
            }
            .show().apply {
                setCanceledOnTouchOutside(canceledOnTouchOutside)
            }
}