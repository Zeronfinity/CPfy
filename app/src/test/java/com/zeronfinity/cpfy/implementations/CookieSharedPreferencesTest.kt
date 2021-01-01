package com.zeronfinity.cpfy.implementations

import android.app.Application
import android.content.SharedPreferences
import com.zeronfinity.cpfy.framework.implementations.CookieSharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

internal class CookieSharedPreferencesTest {

    private val cookie = "cookie"
    private val cookie2 = "cookie"
    private val cookieTitle = "cookie_title"
    private val sharedPreferencesCookies = "com.zeronfinity.cpfy.cookies"

    private val applicationMock = mockk<Application>()
    private val sharedPreferencesMock = mockk<SharedPreferences>()
    private lateinit var sut: CookieSharedPreferences

    @BeforeEach
    internal fun setUp() {
        every { applicationMock.getSharedPreferences(any(), any()) } returns sharedPreferencesMock
        every { applicationMock.getString(any()) } returns sharedPreferencesCookies

        every { sharedPreferencesMock.edit().commit() } answers { true }

        sut = CookieSharedPreferences(applicationMock)
    }

    @Nested
    @DisplayName("Given no preference set before")
    inner class EmptyPreferences {
        private val slotParamsStringKey = slot<String>()
        private val slotParamsStringVal = slot<String>()
        private val defList = mutableListOf<String?>()

        @Test
        @DisplayName("When get called, then correct parameters used")
        fun get_correctParametersUsed() {
            // Arrange
            every {
                sharedPreferencesMock.getString(
                    capture(slotParamsStringKey),
                    captureNullable(defList)
                )
            } answers { null }
            // Act
            sut.get(cookieTitle)
            // Assert
            assertAll(
                Executable { assertEquals(cookieTitle, slotParamsStringKey.captured) },
                Executable { assertNull(defList[0]) }
            )
        }

        @Test
        @DisplayName("When set called, then correct parameters used")
        fun set_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.edit().putString(capture(slotParamsStringKey), capture(slotParamsStringVal)) } answers { nothing }
            // Act
            sut.set(cookieTitle, cookie)
            // Assert
            assertAll(
                Executable { assertEquals(cookieTitle, slotParamsStringKey.captured) },
                Executable { assertEquals(cookie, slotParamsStringVal.captured) },
            )
        }

        @Test
        @DisplayName("When get called, then null returned")
        fun get_nullReturned() {
            // Arrange
            every { sharedPreferencesMock.getString(any(), any()) } answers { null }
            // Act
            val cookieActual = sut.get(cookieTitle)
            // Assert
            assertNull(cookieActual)
        }
    }

    @Nested
    @DisplayName("Given preference set before")
    inner class AlreadySetPreferences {
        private val slotParamsStringKey = slot<String>()
        private val slotParamsStringVal = slot<String>()

        @Test
        @DisplayName("When get called, then last set cookie returned")
        fun get_lastSetCookieReturned() {
            // Arrange
            every { sharedPreferencesMock.edit().putString(capture(slotParamsStringKey), capture(slotParamsStringVal)) } answers { nothing }
            sut.set(cookieTitle, cookie)
            every { sharedPreferencesMock.getString(cookieTitle, any()) } returns slotParamsStringVal.captured
            // Act
            val cookieActual = sut.get(cookieTitle)
            // Assert
            assertEquals(cookie, cookieActual)
        }
    }

    @Nested
    @DisplayName("Given preference set twice before")
    inner class SetPreferencesTwice {
        private val slotParamsStringKey = slot<String>()
        private val slotParamsStringVal = slot<String>()

        @Test
        @DisplayName("When get called, then last set cookie returned")
        fun get_lastSetCookieReturned() {
            // Arrange
            every { sharedPreferencesMock.edit().putString(capture(slotParamsStringKey), capture(slotParamsStringVal)) } answers { nothing }
            sut.set(cookieTitle, cookie)
            sut.set(cookieTitle, cookie2)
            every { sharedPreferencesMock.getString(cookieTitle, any()) } returns slotParamsStringVal.captured
            // Act
            val cookieActual = sut.get(cookieTitle)
            // Assert
            assertEquals(cookie2, cookieActual)
        }
    }
}
