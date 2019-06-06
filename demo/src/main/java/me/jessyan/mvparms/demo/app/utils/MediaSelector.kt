package me.jessyan.mvparms.demo.app.utils

import android.content.pm.ActivityInfo
import com.jess.arms.base.BaseActivity
import com.jess.arms.http.imageloader.glide.Glide4Engine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.ui.MatisseActivity
import me.jessyan.mvparms.demo.app.extensions.startActivityForResult


class MediaSelector {

    fun select(activity: BaseActivity<*>) {
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(Glide4Engine())
        activity.startActivityForResult<MatisseActivity> {

        }
    }
}