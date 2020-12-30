package com.zeronfinity.cpfy.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.zeronfinity.core.usecase.SetCookieUseCase

class CookieViewModel @ViewModelInject constructor(
    private val setCookieUseCase: SetCookieUseCase
) : ViewModel() {
    fun setCookie(cookieTitle: String, rawCookieString: String) {
        setCookieUseCase(cookieTitle, rawCookieString)
    }
}
