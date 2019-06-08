package com.mvp.demo.app.utils

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.livinglifetechway.k4kotlin.shortToast
import com.mvp.demo.R
import com.mvp.demo.app.extensions.*
import com.plain.http.imageloader.glide.Glide4Engine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.ui.MatisseActivity
import com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_SELECTION
import com.zhihu.matisse.ui.MatisseActivity.EXTRA_RESULT_SELECTION_PATH


object MediaSelector {

    fun selectAll(activity: FragmentActivity,
                  callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        Matisse.from(activity).init(MimeType.ofAll())
        activity.checkPermissionAndSelect(callback)
    }

    fun selectAll(fragment: Fragment,
                  callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        Matisse.from(fragment).init(MimeType.ofAll())
        fragment.activity?.checkPermissionAndSelect(callback)
    }

    fun selectPics(activity: FragmentActivity,
                   callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        Matisse.from(activity).init(MimeType.ofImage())
        activity.checkPermissionAndSelect(callback)
    }

    fun selectPics(fragment: Fragment,
                   callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        Matisse.from(fragment).init(MimeType.ofImage())
        fragment.activity?.checkPermissionAndSelect(callback)
    }

    fun selectVideo(activity: FragmentActivity,
                    callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        Matisse.from(activity).init(MimeType.ofVideo())
        activity.checkPermissionAndSelect(callback)
    }

    fun selectVideo(fragment: Fragment,
                    callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        Matisse.from(fragment).init(MimeType.ofVideo())
        fragment.activity?.checkPermissionAndSelect(callback)
    }

    private fun FragmentActivity.checkPermissionAndSelect(callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) {
        launchUI {
            reqMediaPermission().yes {
                startActivityForResult<MatisseActivity> {
                    val pathList: ArrayList<String>? = it.getStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH)
                    val uriList: ArrayList<Uri>? = it.getParcelableArrayListExtra(EXTRA_RESULT_SELECTION)
                    callback(pathList, uriList)
                }
            }.otherwise {
                shortToast("没有权限，无法访问相册")
            }
        }
    }

    private fun Matisse.init(type: Set<MimeType> = MimeType.ofImage(), maxSelectable: Int = 9) {
        choose(type).apply {
            countable(true)
            theme(R.style.MatisseTheme)
            maxSelectable(maxSelectable)
            //gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
            restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            thumbnailScale(0.85f)
            imageEngine(Glide4Engine())
        }
    }
}

fun FragmentActivity.selectPics(callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) = MediaSelector.selectPics(this, callback)

fun Fragment.selectPics(callback: (ArrayList<String>?, ArrayList<Uri>?) -> Unit) = MediaSelector.selectPics(this, callback)