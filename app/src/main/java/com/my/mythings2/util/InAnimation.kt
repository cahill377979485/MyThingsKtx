package com.my.mythings2.util

import android.animation.ObjectAnimator
import android.view.View

/**
 * @author 文琳
 * @time 2020/6/18 17:00
 * @desc
 */
private const val GOLDEN_RATIO = .618f

class InAnimation @JvmOverloads constructor(private val mFrom: Float = GOLDEN_RATIO) {

    fun getAnimators(view: View?): Array<ObjectAnimator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)
        return arrayOf(scaleX, scaleY, alpha)
    }
}