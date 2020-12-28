package com.zeronfinity.cpfy.model

import android.app.Application
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class FilterTimeRangeSharedPreferenceTest {
    private val simpleDateFormat = SimpleDateFormat(
        "dd-MM-yy HH:mm",
        Locale.getDefault()
    )
    private val dateExpected = simpleDateFormat.parse("28-12-20 14:00")
    private val dateExpected2 = simpleDateFormat.parse("29-12-20 08:00")
    private val durationExpected = 2 * 24 * 60 * 60
    private val durationExpected2 = 3 * 24 * 60 * 60

    private val defaultDuration = 7 * 24 * 60 * 60

    private val sharedPreferencesFilter = "com.zeronfinity.cpfy.filters"
    private val startTimeLowerBoundLabel = "start_time_lower_bound"
    private val startTimeUpperBoundLabel = "start_time_upper_bound"
    private val endTimeLowerBoundLabel = "end_time_lower_bound"
    private val endTimeUpperBoundLabel = "end_time_upper_bound"
    private val durationLowerBoundLabel = "duration_lower_bound"
    private val durationUpperBoundLabel = "duration_upper_bound"

    private val applicationMock = mockk<Application>()
    private val sharedPreferencesMock = mockk<SharedPreferences>()
    private lateinit var sut: FilterTimeRangeSharedPreference

    @BeforeEach
    internal fun setUp() {
        every { applicationMock.getSharedPreferences(any(), any()) } returns sharedPreferencesMock
        every { applicationMock.getString(any()) } returns sharedPreferencesFilter

        every { sharedPreferencesMock.edit().apply() } answers { nothing }

        sut = FilterTimeRangeSharedPreference(applicationMock)
    }

    @Nested
    @DisplayName("Given no preference set before")
    inner class EmptyPreferences {
        private val slotParamsInt = slot<Int>()
        private val slotParamsLong = slot<Long>()
        private val slotParamsString = slot<String>()

        @Test
        @DisplayName("When getStartTimeLowerBound called, then correct parameters used")
        fun getStartTimeLowerBound_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.getLong(capture(slotParamsString), capture(slotParamsLong)) } answers { dateExpected.time }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundLabel
            // Act
            sut.getStartTimeLowerBound()
            // Assert
            assertEquals(startTimeLowerBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then correct parameters used")
        fun getStartTimeUpperBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.getLong(capture(slotParamsString), capture(slotParamsLong)) } answers { dateExpected.time }
            every { applicationMock.getString(any()) } returns startTimeUpperBoundLabel
            // Act
            sut.getStartTimeUpperBound()
            // Assert
            assertEquals(startTimeUpperBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getEndTimeLowerBound called, then correct parameters used")
        fun getEndTimeLowerBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.getLong(capture(slotParamsString), capture(slotParamsLong)) } answers { dateExpected.time }
            every { applicationMock.getString(any()) } returns endTimeLowerBoundLabel
            // Act
            sut.getEndTimeLowerBound()
            // Assert
            assertEquals(endTimeLowerBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then correct parameters used")
        fun getEndTimeUpperBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.getLong(capture(slotParamsString), capture(slotParamsLong)) } answers { dateExpected.time }
            every { applicationMock.getString(any()) } returns endTimeUpperBoundLabel
            // Act
            sut.getEndTimeUpperBound()
            // Assert
            assertEquals(endTimeUpperBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getDurationLowerBound called, then correct parameters used")
        fun getDurationLowerBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.getInt(capture(slotParamsString), capture(slotParamsInt)) } answers { durationExpected }
            every { applicationMock.getString(any()) } returns durationLowerBoundLabel
            // Act
            sut.getDurationLowerBound()
            // Assert
            assertEquals(durationLowerBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getDurationUpperBound called, then correct parameters used")
        fun getDurationUpperBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.getInt(capture(slotParamsString), capture(slotParamsInt)) } answers { durationExpected }
            every { applicationMock.getString(any()) } returns durationUpperBoundLabel
            // Act
            sut.getDurationUpperBound()
            // Assert
            assertEquals(durationUpperBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When setStartTimeLowerBound called, then correct parameters used")
        fun setStartTimeLowerBound_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.edit().putLong(capture(slotParamsString), capture(slotParamsLong)) } answers { nothing }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundLabel
            // Act
            sut.setStartTimeLowerBound(dateExpected)
            // Assert
            val dateActual = Date()
            dateActual.time = slotParamsLong.captured
            assertAll(
                Executable { assertEquals(startTimeLowerBoundLabel, slotParamsString.captured) },
                Executable { assertEquals(dateExpected, dateActual) }
            )
        }

        @Test
        @DisplayName("When setStartTimeUpperBound called, then correct parameters used")
        fun setStartTimeUpperBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.edit().putLong(capture(slotParamsString), capture(slotParamsLong)) } answers { nothing }
            every { applicationMock.getString(any()) } returns startTimeUpperBoundLabel
            // Act
            sut.setStartTimeUpperBound(dateExpected)
            // Assert
            val dateActual = Date()
            dateActual.time = slotParamsLong.captured
            assertAll(
                Executable { assertEquals(startTimeUpperBoundLabel, slotParamsString.captured) },
                Executable { assertEquals(dateExpected, dateActual) }
            )
        }

        @Test
        @DisplayName("When setEndTimeLowerBound called, then correct parameters used")
        fun setEndTimeLowerBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.edit().putLong(capture(slotParamsString), capture(slotParamsLong)) } answers { nothing }
            every { applicationMock.getString(any()) } returns endTimeLowerBoundLabel
            // Act
            sut.setEndTimeLowerBound(dateExpected)
            // Assert
            val dateActual = Date()
            dateActual.time = slotParamsLong.captured
            assertAll(
                Executable { assertEquals(endTimeLowerBoundLabel, slotParamsString.captured) },
                Executable { assertEquals(dateExpected, dateActual) }
            )
        }

        @Test
        @DisplayName("When setEndTimeUpperBound called, then correct parameters used")
        fun setEndTimeUpperBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.edit().putLong(capture(slotParamsString), capture(slotParamsLong)) } answers { nothing }
            every { applicationMock.getString(any()) } returns endTimeUpperBoundLabel
            // Act
            sut.setEndTimeUpperBound(dateExpected)
            // Assert
            val dateActual = Date()
            dateActual.time = slotParamsLong.captured
            assertAll(
                Executable { assertEquals(endTimeUpperBoundLabel, slotParamsString.captured) },
                Executable { assertEquals(dateExpected, dateActual) }
            )
        }

        @Test
        @DisplayName("When setDurationLowerBound called, then correct parameters used")
        fun setDurationLowerBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.edit().putInt(capture(slotParamsString), capture(slotParamsInt)) } answers { nothing }
            every { applicationMock.getString(any()) } returns durationLowerBoundLabel
            // Act
            sut.setDurationLowerBound(durationExpected)
            // Assert
            assertAll(
                Executable { assertEquals(durationLowerBoundLabel, slotParamsString.captured) },
                Executable { assertEquals(durationExpected, slotParamsInt.captured) }
            )
        }

        @Test
        @DisplayName("When setDurationUpperBound called, then correct parameters used")
        fun setDurationUpperBound_correctValueSetInPreference() {
            // Arrange
            every { sharedPreferencesMock.edit().putInt(capture(slotParamsString), capture(slotParamsInt)) } answers { nothing }
            every { applicationMock.getString(any()) } returns durationUpperBoundLabel
            // Act
            sut.setDurationUpperBound(durationExpected)
            // Assert
            assertAll(
                Executable { assertEquals(durationUpperBoundLabel, slotParamsString.captured) },
                Executable { assertEquals(durationExpected, slotParamsInt.captured) }
            )
        }

        @Test
        @DisplayName("When getStartTimeLowerBound called, then current date returned")
        fun getStartTimeLowerBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns startTimeLowerBoundLabel
            val currentDate = Date()
            every { sharedPreferencesMock.getLong(startTimeLowerBoundLabel, any()) } returns currentDate.time
            // Act
            val date = sut.getStartTimeLowerBound()
            // Assert
            assertEquals(currentDate, date)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then current date returned")
        fun getStartTimeUpperBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns startTimeUpperBoundLabel
            val currentDate = Date()
            every { sharedPreferencesMock.getLong(startTimeUpperBoundLabel, any()) } returns currentDate.time
            // Act
            val date = sut.getStartTimeUpperBound()
            // Assert
            assertEquals(currentDate, date)
        }

        @Test
        @DisplayName("When getEndTimeLowerBound called, then current date returned")
        fun getEndTimeLowerBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns endTimeLowerBoundLabel
            val currentDate = Date()
            every { sharedPreferencesMock.getLong(endTimeLowerBoundLabel, any()) } returns currentDate.time
            // Act
            val date = sut.getEndTimeLowerBound()
            // Assert
            assertEquals(currentDate, date)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then current date returned")
        fun getEndTimeUpperBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns endTimeUpperBoundLabel
            val currentDate = Date()
            every { sharedPreferencesMock.getLong(endTimeUpperBoundLabel, any()) } returns currentDate.time
            // Act
            val date = sut.getEndTimeUpperBound()
            // Assert
            assertEquals(currentDate, date)
        }

        @Test
        @DisplayName("When getDurationLowerBound called, then default duration returned")
        fun getDurationLowerBound_defaultDurationReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns durationLowerBoundLabel
            every { sharedPreferencesMock.getInt(durationLowerBoundLabel, any()) } returns defaultDuration
            // Act
            val date = sut.getDurationLowerBound()
            // Assert
            assertEquals(defaultDuration, date)
        }

        @Test
        @DisplayName("When getDurationUpperBound called, then default duration returned")
        fun getDurationUpperBound_defaultDurationReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns durationUpperBoundLabel
            every { sharedPreferencesMock.getInt(durationUpperBoundLabel, any()) } returns defaultDuration
            // Act
            val date = sut.getDurationUpperBound()
            // Assert
            assertEquals(defaultDuration, date)
        }
    }

    @Nested
    @DisplayName("Given preference set before")
    inner class AlreadySetPreferences {
        private val slotParamsInt = slot<Int>()
        private val slotParamsLong = slot<Long>()

        init {
            every { sharedPreferencesMock.edit().putInt(any(), capture(slotParamsInt)) } answers { nothing }
        }

        @Test
        @DisplayName("When getStartTimeLowerBound called, then last set date returned")
        fun getStartTimeLowerBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns startTimeLowerBoundLabel
            every { sharedPreferencesMock.edit().putLong(startTimeLowerBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setStartTimeLowerBound(dateExpected)
            every { sharedPreferencesMock.getLong(startTimeLowerBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getStartTimeLowerBound()
            // Assert
            assertEquals(dateExpected, dateActual)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then last set date returned")
        fun getStartTimeUpperBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns startTimeUpperBoundLabel
            every { sharedPreferencesMock.edit().putLong(startTimeUpperBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setStartTimeUpperBound(dateExpected)
            every { sharedPreferencesMock.getLong(startTimeUpperBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getStartTimeUpperBound()
            // Assert
            assertEquals(dateExpected, dateActual)
        }

        @Test
        @DisplayName("When getEndTimeLowerBound called, then last set date returned")
        fun getEndTimeLowerBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns endTimeLowerBoundLabel
            every { sharedPreferencesMock.edit().putLong(endTimeLowerBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setEndTimeLowerBound(dateExpected)
            every { sharedPreferencesMock.getLong(endTimeLowerBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getEndTimeLowerBound()
            // Assert
            assertEquals(dateExpected, dateActual)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then last set date returned")
        fun getEndTimeUpperBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns endTimeUpperBoundLabel
            every { sharedPreferencesMock.edit().putLong(endTimeUpperBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setEndTimeUpperBound(dateExpected)
            every { sharedPreferencesMock.getLong(endTimeUpperBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getEndTimeUpperBound()
            // Assert
            assertEquals(dateExpected, dateActual)
        }

        @Test
        @DisplayName("When getDurationLowerBound called, then last set duration returned")
        fun getDurationLowerBound_defaultDurationReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns durationLowerBoundLabel
            every { sharedPreferencesMock.edit().putInt(durationLowerBoundLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setDurationLowerBound(durationExpected)
            every { sharedPreferencesMock.getInt(durationLowerBoundLabel, any()) } returns slotParamsInt.captured
            // Act
            val durationActual = sut.getDurationLowerBound()
            // Assert
            assertEquals(durationExpected, durationActual)
        }

        @Test
        @DisplayName("When getDurationUpperBound called, then last set duration returned")
        fun getDurationUpperBound_defaultDurationReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns durationUpperBoundLabel
            every { sharedPreferencesMock.edit().putInt(durationUpperBoundLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setDurationUpperBound(durationExpected)
            every { sharedPreferencesMock.getInt(durationUpperBoundLabel, any()) } returns slotParamsInt.captured
            // Act
            val durationActual = sut.getDurationUpperBound()
            // Assert
            assertEquals(durationExpected, durationActual)
        }
    }

    @Nested
    @DisplayName("Given preference set twice before")
    inner class SetPreferencesTwice {
        private val slotParamsInt = slot<Int>()
        private val slotParamsLong = slot<Long>()

        init {
            every { sharedPreferencesMock.edit().putInt(any(), capture(slotParamsInt)) } answers { nothing }
        }

        @Test
        @DisplayName("When getStartTimeLowerBound called, then last set date returned")
        fun getStartTimeLowerBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns startTimeLowerBoundLabel
            every { sharedPreferencesMock.edit().putLong(startTimeLowerBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setStartTimeLowerBound(dateExpected)
            sut.setStartTimeLowerBound(dateExpected2)
            every { sharedPreferencesMock.getLong(startTimeLowerBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getStartTimeLowerBound()
            // Assert
            assertEquals(dateExpected2, dateActual)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then last set date returned")
        fun getStartTimeUpperBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns startTimeUpperBoundLabel
            every { sharedPreferencesMock.edit().putLong(startTimeUpperBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setStartTimeUpperBound(dateExpected)
            sut.setStartTimeUpperBound(dateExpected2)
            every { sharedPreferencesMock.getLong(startTimeUpperBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getStartTimeUpperBound()
            // Assert
            assertEquals(dateExpected2, dateActual)
        }

        @Test
        @DisplayName("When getEndTimeLowerBound called, then last set date returned")
        fun getEndTimeLowerBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns endTimeLowerBoundLabel
            every { sharedPreferencesMock.edit().putLong(endTimeLowerBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setEndTimeLowerBound(dateExpected)
            sut.setEndTimeLowerBound(dateExpected2)
            every { sharedPreferencesMock.getLong(endTimeLowerBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getEndTimeLowerBound()
            // Assert
            assertEquals(dateExpected2, dateActual)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then last set date returned")
        fun getEndTimeUpperBound_currentDateReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns endTimeUpperBoundLabel
            every { sharedPreferencesMock.edit().putLong(endTimeUpperBoundLabel, capture(slotParamsLong)) } answers { nothing }
            sut.setEndTimeUpperBound(dateExpected)
            sut.setEndTimeUpperBound(dateExpected2)
            every { sharedPreferencesMock.getLong(endTimeUpperBoundLabel, any()) } returns slotParamsLong.captured
            // Act
            val dateActual = sut.getEndTimeUpperBound()
            // Assert
            assertEquals(dateExpected2, dateActual)
        }

        @Test
        @DisplayName("When getDurationLowerBound called, then last set duration returned")
        fun getDurationLowerBound_defaultDurationReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns durationLowerBoundLabel
            every { sharedPreferencesMock.edit().putInt(durationLowerBoundLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setDurationLowerBound(durationExpected)
            sut.setDurationLowerBound(durationExpected2)
            every { sharedPreferencesMock.getInt(durationLowerBoundLabel, any()) } returns slotParamsInt.captured
            // Act
            val durationActual = sut.getDurationLowerBound()
            // Assert
            assertEquals(durationExpected2, durationActual)
        }

        @Test
        @DisplayName("When getDurationUpperBound called, then last set duration returned")
        fun getDurationUpperBound_defaultDurationReturned() {
            // Arrange
            every { applicationMock.getString(any()) } returns durationUpperBoundLabel
            every { sharedPreferencesMock.edit().putInt(durationUpperBoundLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setDurationUpperBound(durationExpected)
            sut.setDurationUpperBound(durationExpected2)
            every { sharedPreferencesMock.getInt(durationUpperBoundLabel, any()) } returns slotParamsInt.captured
            // Act
            val durationActual = sut.getDurationUpperBound()
            // Assert
            assertEquals(durationExpected2, durationActual)
        }
    }
}
