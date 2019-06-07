package com.mvp.demo.app.extensions

import android.content.Context
import kotlin.coroutines.suspendCoroutine



suspend fun Context.confirm(title: String, message: String = "", okBtn: String = "确定",
                            cancelBtn: String = "取消", canceledOnTouchOutside: Boolean = false) = suspendCoroutine<Boolean> { continuation ->
    /*BaseDialogFragment.Builder(this)
            .setContentView(R.layout.dialog_custom)
            .setAnimStyle(BaseDialog.AnimStyle.SCALE)
            //.setText(id, "我是预设置的文本")
            .setOnClickListener(R.id.btn_dialog_custom_ok, BaseDialog.OnClickListener<Any> { dialog, view -> dialog.dismiss() })
            .addOnShowListener(BaseDialog.OnShowListener { toast("Dialog  显示了") })
            .addOnCancelListener(BaseDialog.OnCancelListener { toast("Dialog 取消了") })
            .addOnDismissListener(BaseDialog.OnDismissListener { toast("Dialog 销毁了") })
            .show()*/
   /* CommonConfirmDialog.showTwoButton(this, title, message, okBtn, cancelBtn,
            object : TemplateDialog.ConfirmListener {
                override fun ok() {
                    continuation.resume(true)
                }

                override fun cancel() {
                    continuation.resume(false)
                }
            }).apply {
        setOnCancelListener {
            continuation.resume(false)
        }
        setCanceledOnTouchOutside(canceledOnTouchOutside)
    }*/
}

suspend fun Context.input(title: String, defaultValue: String = "", maxInputLength: Int = Int.MAX_VALUE,
                          inputType: Int? = null, canceledOnTouchOutside: Boolean = false) = suspendCoroutine<Pair<Boolean, String?>> { continuation ->
    /*CommonInputDialog(this, maxInputLength, title, defaultValue, object : CommonInputDialog.ButtonListener {
        override fun save(text: String?) {
            continuation.resume(true to text)
        }

        override fun cancel() {
            continuation.resume(false to "")
        }
    }).apply {
        if (inputType != null) {
            setInputType(inputType)
        }
        setOnCancelListener {
            continuation.resume(false to "")
        }
        setCanceledOnTouchOutside(canceledOnTouchOutside)
    }.show()*/
}