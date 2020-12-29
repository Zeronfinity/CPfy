package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.CookieRepository

class SetCookieUseCase(private val cookieRepository: CookieRepository) {
    operator fun invoke(cookieTitle: String, rawCookieString: String) = cookieRepository.setCookie(cookieTitle, rawCookieString)
}
