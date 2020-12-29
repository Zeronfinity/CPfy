package com.zeronfinity.core.usecase

import com.zeronfinity.core.repository.CookieRepository

class GetCookieUseCase(private val cookieRepository: CookieRepository) {
    operator fun invoke(cookieTitle: String) = cookieRepository.getCookie(cookieTitle)
}
