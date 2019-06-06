package me.jessyan.mvparms.demo.app.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import com.livinglifetechway.k4kotlin.orFalse
import me.jessyan.mvparms.demo.R

/**
 * 作者：Yu on 2018/6/18 18:00
 * Version v1.0
 * 描述：view动画扩展，目前只是使用ObjectAnimator的api来操作view，具体就是位移，旋转，透明度，缩放 四种
 * 操作的目的是 展示view的显示和隐藏动画效果
 */

fun View.leftIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "translationX", -width.toFloat(), 0f).setDuration(duration).start()
    }
}

fun View.rightIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "translationX", width.toFloat(), 0f).setDuration(duration).start()
    }
}

fun View.topIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "translationY", -height.toFloat(), 0f).setDuration(duration).start()
    }
}

fun View.bottomIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "translationY", height.toFloat(), 0f).setDuration(duration).start()
    }
}


fun View.leftOut(duration: Long = 200) {
    isAnimatDismissing(true)
    isAnimatNeedShow(false)
    ObjectAnimator.ofFloat(this, "translationX", 0f, -width.toFloat()).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                translationX = 0f
                isAnimatDismissing(false)
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.rightOut(duration: Long = 200) {
    isAnimatDismissing(true)
    isAnimatNeedShow(false)
    ObjectAnimator.ofFloat(this, "translationX", 0f, width.toFloat()).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                translationX = 0f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.topOut(duration: Long = 200) {
    isAnimatDismissing(true)
    isAnimatNeedShow(false)
    ObjectAnimator.ofFloat(this, "translationY", 0f, -height.toFloat()).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                translationY = 0f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.bottomOut(duration: Long = 200) {
    isAnimatDismissing(true)
    isAnimatNeedShow(false)
    ObjectAnimator.ofFloat(this, "translationY", 0f, height.toFloat()).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                translationY = 0f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.rotateIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "rotation", -720f, 0f).setDuration(duration).start()
    }
}

fun View.revRotateIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "rotation", 720f, 0f).setDuration(duration).start()
    }
}

fun View.rotateOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "rotation", 0f, -720f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                rotation = 0f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.revRotateOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "rotation", 0f, 720f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                rotation = 0f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.scaleXSmallIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "scaleX", 2.0f, 1f).setDuration(duration).start()
    }
}

fun View.scaleXSmallOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 2.0f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                scaleX = 1f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.scaleXBigIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "scaleX", 0.0f, 1f).setDuration(duration).start()
    }
}

fun View.scaleXBigOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.0f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                scaleX = 1f
            }
        })
        setDuration(duration)
        start()
    }
}


fun View.scaleYSmallIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "scaleY", 2.0f, 1f).setDuration(duration).start()
    }
}

fun View.scaleYSmallOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 2.0f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                scaleY = 1f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.scaleYBigIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1f).setDuration(duration).start()
    }
}

fun View.scaleYBigOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.0f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                scaleY = 1f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.alphaIn(duration: Long = 200) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "alpha", 0.5f, 1f).setDuration(duration).start()
    }
}

fun View.alphaOut(duration: Long = 200) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.5f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                alpha = 1f
            }
        })
        setDuration(duration)
        start()
    }
}

fun View.splashIn(duration: Long = 1000) {
    isAnimatNeedShow(true)
    if (visibility != View.VISIBLE || isAnimatDismissing()) {
        visibility = View.VISIBLE
        ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1f, 0f, 0.1f, 0.3f, 0.5f, 0.7f, 0.9f, 1f).setDuration(duration).start()
    }
}

fun View.splashOut(duration: Long = 1000) {
    isAnimatNeedShow(false)
    isAnimatDismissing(true)
    ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f, 1.0f, 0.9f, 0.7f, 0.5f, 0.3f, 0.1f, 0.0f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isAnimatNeedShow()) {
                    visibility = View.GONE
                }
                isAnimatDismissing(false)
                alpha = 1f
            }
        })
        setDuration(duration)
        start()
    }
}

private fun View.isAnimatDismissing(tag: Boolean) {
    setTag(R.id.key_animat_tag_dismissing, tag)
}

private fun View.isAnimatDismissing() = (getTag(R.id.key_animat_tag_dismissing) as? Boolean).orFalse()


private fun View.isAnimatNeedShow(tag: Boolean) {
    setTag(R.id.key_animat_tag_need_show, tag)
}

private fun View.isAnimatNeedShow() = (getTag(R.id.key_animat_tag_need_show) as? Boolean).orFalse()

