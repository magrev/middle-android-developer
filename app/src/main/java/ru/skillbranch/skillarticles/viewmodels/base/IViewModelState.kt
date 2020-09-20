package ru.skillbranch.skillarticles.viewmodels.base

import android.os.Bundle

/**
 * Created by Reva on 07.09.2020
 */

interface IViewModelState {
    fun save(outState: Bundle)
    fun restore(savedState: Bundle): IViewModelState
}