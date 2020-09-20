package ru.skillbranch.skillarticles.ui.custom

import android.text.TextPaint
import android.text.style.BackgroundColorSpan
import androidx.core.graphics.ColorUtils

/**
 * Created by Reva on 07.09.2020
 */

open class SearchSpan(bgColor: Int, private val fgColor: Int) : BackgroundColorSpan(bgColor) {
    private val alphaColor by lazy {
        ColorUtils.setAlphaComponent(backgroundColor, 160)
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.bgColor = alphaColor
        textPaint.color = fgColor
    }
}