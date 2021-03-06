package ru.skillbranch.skillarticles.extensions

import android.view.View
import androidx.core.view.marginRight
import androidx.core.view.marginTop

/**
 * Created by Reva on 07.09.2020
 */

fun View.setMarginOptionally(top: Int = 0, bottom: Int = 0, left: Int = 0, right: Int = 0) {

}

fun View.setPaddingOptionally(
    left: Int = paddingLeft,
    top: Int = paddingLeft,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}