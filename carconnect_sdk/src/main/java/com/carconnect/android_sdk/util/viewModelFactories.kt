package com.carconnect.android_sdk.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * create a factory for a ViewModel with 2 arguments
 *
 * @param constructor of ViewModel
 */
fun <T : ViewModel, A, B> doubleArgsViewModelFactory(constructor: (A, B) -> T): (A, B) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A, arg2: B ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg, arg2) as V
            }
        }
    }
}