package com.zeronfinity.cpfy.framework.implementations

import android.app.Application
import android.content.SharedPreferences
import com.zeronfinity.cpfy.common.DEFAULT_DAYS_INTERVAL
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class FilterTimeRangeSharedPreferencesTest {
    private val simpleDateFormat = SimpleDateFormat(
        "dd-MM-yy HH:mm",
        Locale.getDefault()
    )
    private val dateExpected = simpleDateFormat.parse("28-12-20 14:00")
    private val dateExpected2 = simpleDateFormat.parse("29-12-20 08:00")
    private val durationExpected = 2 * 24 * 60 * 60
    private val durationExpected2 = 3 * 24 * 60 * 60

    private val defaultDuration = 7 * 24 * 60 * 60

    private val defaultDaysAfterToday = DEFAULT_DAYS_INTERVAL

    private val sharedPreferencesFilter = "com.zeronfinity.cpfy.filters"
    private val startTimeLowerBoundLabel = "start_time_lower_bound"
    private val startTimeLowerBoundTodayLabel = "start_time_lower_bound_today"
    private val startTimeDaysAfterTodayLabel = "start_time_days_after_today"
    private val startTimeUpperBoundLabel = "start_time_upper_bound"
    private val endTimeLowerBoundLabel = "end_time_lower_bound"
    private val endTimeLowerBoundTodayLabel = "end_time_lower_bound_today"
    private val endTimeDaysAfterTodayLabel = "end_time_days_after_today"
    private val endTimeUpperBoundLabel = "end_time_upper_bound"
    private val durationLowerBoundLabel = "duration_lower_bound"
    private val durationUpperBoundLabel = "duration_upper_bound"

    private val applicationMock = mockk<Application>()
    private val sharedPreferencesMock = mockk<SharedPreferences>()
    private lateinit var sut: FilterTimeRangeSharedPreferences

    @BeforeEach
    internal fun setUp() {
        every { applicationMock.getSharedPreferences(any(), any()) } returns sharedPreferencesMock
        every { applicationMock.getString(any()) } returns sharedPreferencesFilter

        every { sharedPreferencesMock.edit().apply() } answers { nothing }

        sut = FilterTimeRangeSharedPreferences(applicationMock)
    }

    @Nested
    @DisplayName("Given no preference set before")
    inner class EmptyPreferences {
        private val slotParamsBool = slot<Boolean>()
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
        @DisplayName("When isStartTimeLowerBoundToday called, then correct parameters used")
        fun isStartTimeLowerBoundToday_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.getBoolean(capture(slotParamsString), capture(slotParamsBool)) } answers { true }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            // Act
            sut.isStartTimeLowerBoundToday()
            // Assert
            assertEquals(startTimeLowerBoundTodayLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getStartTimeDaysAfterToday called, then correct parameters used")
        fun getStartTimeDaysAfterToday_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.getInt(capture(slotParamsString), capture(slotParamsInt)) } answers { defaultDaysAfterToday }
            every { applicationMock.getString(any()) } returns startTimeDaysAfterTodayLabel
            // Act
            sut.getStartTimeDaysAfterToday()
            // Assert
            assertEquals(startTimeDaysAfterTodayLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then correct parameters used")
        fun getStartTimeUpperBound_correctParametersUsed() {
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
        fun getEndTimeLowerBound_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.getLong(capture(slotParamsString), capture(slotParamsLong)) } answers { dateExpected.time }
            every { applicationMock.getString(any()) } returns endTimeLowerBoundLabel
            // Act
            sut.getEndTimeLowerBound()
            // Assert
            assertEquals(endTimeLowerBoundLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When isEndTimeLowerBoundToday called, then correct parameters used")
        fun isEndTimeLowerBoundToday_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.getBoolean(capture(slotParamsString), capture(slotParamsBool)) } answers { true }
            every { applicationMock.getString(any()) } returns endTimeLowerBoundTodayLabel
            // Act
            sut.isEndTimeLowerBoundToday()
            // Assert
            assertEquals(endTimeLowerBoundTodayLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getEndTimeDaysAfterToday called, then correct parameters used")
        fun getEndTimeDaysAfterToday_correctParametersUsed() {
            // Arrange
            every { sharedPreferencesMock.getInt(capture(slotParamsString), capture(slotParamsInt)) } answers { defaultDaysAfterToday }
            every { applicationMock.getString(any()) } returns endTimeDaysAfterTodayLabel
            // Act
            sut.getEndTimeDaysAfterToday()
            // Assert
            assertEquals(endTimeDaysAfterTodayLabel, slotParamsString.captured)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then correct parameters used")
        fun getEndTimeUpperBound_correctParametersUsed() {
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
        fun getDurationLowerBound_correctParametersUsed() {
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
        fun getDurationUpperBound_correctParametersUsed() {
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
        @DisplayName("When setStartTimeLowerBoundToday called, then correct parameters used")
        fun setStartTimeLowerBoundToday_correctParametersUsed() {
            // Arrange
            val valueExpected = true
            every { sharedPreferencesMock.edit().putBoolean(capture(slotParamsString), capture(slotParamsBool)) } answers { nothing }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            // Act
            sut.setStartTimeLowerBoundToday(valueExpected)
            // Assert
            assertAll(
                Executable { assertEquals(startTimeLowerBoundTodayLabel, slotParamsString.captured) },
                Executable { assertEquals(valueExpected, slotParamsBool.captured) }
            )
        }

        @Test
        @DisplayName("When setStartTimeDaysAfterToday called, then correct parameters used")
        fun setStartTimeDaysAfterToday_correctParametersUsed() {
            // Arrange
            val resultExpected = 7
            every { sharedPreferencesMock.edit().putInt(capture(slotParamsString), capture(slotParamsInt)) } answers { nothing }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            // Act
            sut.setStartTimeDaysAfterToday(resultExpected)
            // Assert
            assertAll(
                Executable { assertEquals(startTimeLowerBoundTodayLabel, slotParamsString.captured) },
                Executable { assertEquals(resultExpected, slotParamsInt.captured) }
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
        @DisplayName("When setEndTimeLowerBoundToday called, then correct parameters used")
        fun setEndTimeLowerBoundToday_correctParametersUsed() {
            // Arrange
            val valueExpected = true
            every { sharedPreferencesMock.edit().putBoolean(capture(slotParamsString), capture(slotParamsBool)) } answers { nothing }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            // Act
            sut.setEndTimeLowerBoundToday(valueExpected)
            // Assert
            assertAll(
                Executable { assertEquals(startTimeLowerBoundTodayLabel, slotParamsString.captured) },
                Executable { assertEquals(valueExpected, slotParamsBool.captured) }
            )
        }

        @Test
        @DisplayName("When setEndTimeDaysAfterToday called, then correct parameters used")
        fun setEndTimeDaysAfterToday_correctParametersUsed() {
            // Arrange
            val resultExpected = 7
            every { sharedPreferencesMock.edit().putInt(capture(slotParamsString), capture(slotParamsInt)) } answers { nothing }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            // Act
            sut.setEndTimeDaysAfterToday(resultExpected)
            // Assert
            assertAll(
                Executable { assertEquals(startTimeLowerBoundTodayLabel, slotParamsString.captured) },
                Executable { assertEquals(resultExpected, slotParamsInt.captured) }
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
        @DisplayName("When isStartTimeLowerBoundToday called, then true returned")
        fun isStartTimeLowerBoundToday_trueReturned() {
            // Arrange
            every { sharedPreferencesMock.getBoolean(startTimeLowerBoundTodayLabel, any()) } answers { true }
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            // Act
            val value = sut.isStartTimeLowerBoundToday()
            // Assert
            assertEquals(true, value)
        }

        @Test
        @DisplayName("When getStartTimeDaysAfterToday called, then defaultDaysAfterToday used")
        fun getStartTimeDaysAfterToday_defaultDaysAfterTodayUsed() {
            // Arrange
            every { sharedPreferencesMock.getInt(startTimeDaysAfterTodayLabel, any()) } answers { defaultDaysAfterToday }
            every { applicationMock.getString(any()) } returns startTimeDaysAfterTodayLabel
            // Act
            val result = sut.getStartTimeDaysAfterToday()
            // Assert
            assertEquals(defaultDaysAfterToday, result)
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
        @DisplayName("When isEndTimeLowerBoundToday called, then true returned")
        fun isEndTimeLowerBoundToday_trueReturned() {
            // Arrange
            every { sharedPreferencesMock.getBoolean(endTimeLowerBoundTodayLabel, any()) } answers { true }
            every { applicationMock.getString(any()) } returns endTimeLowerBoundTodayLabel
            // Act
            val value = sut.isEndTimeLowerBoundToday()
            // Assert
            assertEquals(true, value)
        }

        @Test
        @DisplayName("When getEndTimeDaysAfterToday called, then defaultDaysAfterToday used")
        fun getEndTimeDaysAfterToday_defaultDaysAfterTodayUsed() {
            // Arrange
            every { sharedPreferencesMock.getInt(endTimeDaysAfterTodayLabel, any()) } answers { defaultDaysAfterToday }
            every { applicationMock.getString(any()) } returns endTimeDaysAfterTodayLabel
            // Act
            val result = sut.getEndTimeDaysAfterToday()
            // Assert
            assertEquals(defaultDaysAfterToday, result)
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
        private val slotParamsBool = slot<Boolean>()
        private val slotParamsInt = slot<Int>()
        private val slotParamsLong = slot<Long>()

        init {
            every { sharedPreferencesMock.edit().putInt(any(), capture(slotParamsInt)) } answers { nothing }
        }

        @Test
        @DisplayName("When getStartTimeLowerBound called, then last set date returned")
        fun getStartTimeLowerBound_setDateReturned() {
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
        @DisplayName("When isStartTimeLowerBoundToday called, then last set value returned")
        fun isStartTimeLowerBoundToday_setValueReturned() {
            // Arrange
            val valueExpected = true
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            every { sharedPreferencesMock.edit().putBoolean(startTimeLowerBoundTodayLabel, capture(slotParamsBool)) } answers { nothing }
            sut.setStartTimeLowerBoundToday(valueExpected)
            every { sharedPreferencesMock.getBoolean(startTimeLowerBoundTodayLabel, any()) } returns slotParamsBool.captured
            // Act
            val valueActual = sut.isStartTimeLowerBoundToday()
            // Assert
            assertEquals(valueExpected, valueActual)
        }

        @Test
        @DisplayName("When getStartTimeDaysAfterToday called, then last set value returned")
        fun getStartTimeDaysAfterToday_setValueReturned() {
            // Arrange
            val resultExpected = 7
            every { applicationMock.getString(any()) } returns startTimeDaysAfterTodayLabel
            every { sharedPreferencesMock.edit().putInt(startTimeDaysAfterTodayLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setStartTimeDaysAfterToday(resultExpected)
            every { sharedPreferencesMock.getInt(startTimeDaysAfterTodayLabel, any()) } returns slotParamsInt.captured
            // Act
            val resultActual = sut.getStartTimeDaysAfterToday()
            // Assert
            assertEquals(resultExpected, resultActual)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then last set date returned")
        fun getStartTimeUpperBound_setDateReturned() {
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
        fun getEndTimeLowerBound_setDateReturned() {
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
        @DisplayName("When isEndTimeLowerBoundToday called, then last set value returned")
        fun isEndTimeLowerBoundToday_setValueReturned() {
            // Arrange
            val valueExpected = true
            every { applicationMock.getString(any()) } returns endTimeLowerBoundTodayLabel
            every { sharedPreferencesMock.edit().putBoolean(endTimeLowerBoundTodayLabel, capture(slotParamsBool)) } answers { nothing }
            sut.setEndTimeLowerBoundToday(valueExpected)
            every { sharedPreferencesMock.getBoolean(endTimeLowerBoundTodayLabel, any()) } returns slotParamsBool.captured
            // Act
            val valueActual = sut.isEndTimeLowerBoundToday()
            // Assert
            assertEquals(valueExpected, valueActual)
        }

        @Test
        @DisplayName("When getEndTimeDaysAfterToday called, then last set value returned")
        fun getEndTimeDaysAfterToday_setValueReturned() {
            // Arrange
            val resultExpected = 7
            every { applicationMock.getString(any()) } returns startTimeDaysAfterTodayLabel
            every { sharedPreferencesMock.edit().putInt(startTimeDaysAfterTodayLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setEndTimeDaysAfterToday(resultExpected)
            every { sharedPreferencesMock.getInt(startTimeDaysAfterTodayLabel, any()) } returns slotParamsInt.captured
            // Act
            val resultActual = sut.getEndTimeDaysAfterToday()
            // Assert
            assertEquals(resultExpected, resultActual)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then last set date returned")
        fun getEndTimeUpperBound_setDateReturned() {
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
        fun getDurationLowerBound_setDurationReturned() {
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
        fun getDurationUpperBound_setDurationReturned() {
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
        private val slotParamsBool = slot<Boolean>()
        private val slotParamsInt = slot<Int>()
        private val slotParamsLong = slot<Long>()

        init {
            every { sharedPreferencesMock.edit().putInt(any(), capture(slotParamsInt)) } answers { nothing }
        }

        @Test
        @DisplayName("When getStartTimeLowerBound called, then last set date returned")
        fun getStartTimeLowerBound_setDateReturned() {
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
        @DisplayName("When isStartTimeLowerBoundToday called, then last set value returned")
        fun isStartTimeLowerBoundToday_setValueReturned() {
            // Arrange
            val valueExpected = true
            val valueExpected2 = false
            every { applicationMock.getString(any()) } returns startTimeLowerBoundTodayLabel
            every { sharedPreferencesMock.edit().putBoolean(startTimeLowerBoundTodayLabel, capture(slotParamsBool)) } answers { nothing }
            sut.setStartTimeLowerBoundToday(valueExpected)
            sut.setStartTimeLowerBoundToday(valueExpected2)
            every { sharedPreferencesMock.getBoolean(startTimeLowerBoundTodayLabel, any()) } returns slotParamsBool.captured
            // Act
            val valueActual = sut.isStartTimeLowerBoundToday()
            // Assert
            assertEquals(valueExpected2, valueActual)
        }

        @Test
        @DisplayName("When getStartTimeDaysAfterToday called, then last set value returned")
        fun getStartTimeDaysAfterToday_setValueReturned() {
            // Arrange
            val resultExpected = 7
            val resultExpected2 = 1
            every { applicationMock.getString(any()) } returns startTimeDaysAfterTodayLabel
            every { sharedPreferencesMock.edit().putInt(startTimeDaysAfterTodayLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setStartTimeDaysAfterToday(resultExpected)
            sut.setStartTimeDaysAfterToday(resultExpected2)
            every { sharedPreferencesMock.getInt(startTimeDaysAfterTodayLabel, any()) } returns slotParamsInt.captured
            // Act
            val resultActual = sut.getStartTimeDaysAfterToday()
            // Assert
            assertEquals(resultExpected2, resultActual)
        }

        @Test
        @DisplayName("When getStartTimeUpperBound called, then last set date returned")
        fun getStartTimeUpperBound_setDateReturned() {
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
        fun getEndTimeLowerBound_setDateReturned() {
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
        @DisplayName("When isEndTimeLowerBoundToday called, then last set value returned")
        fun isEndTimeLowerBoundToday_setValueReturned() {
            // Arrange
            val valueExpected = true
            val valueExpected2 = false
            every { applicationMock.getString(any()) } returns endTimeLowerBoundTodayLabel
            every { sharedPreferencesMock.edit().putBoolean(endTimeLowerBoundTodayLabel, capture(slotParamsBool)) } answers { nothing }
            sut.setEndTimeLowerBoundToday(valueExpected)
            sut.setEndTimeLowerBoundToday(valueExpected2)
            every { sharedPreferencesMock.getBoolean(endTimeLowerBoundTodayLabel, any()) } returns slotParamsBool.captured
            // Act
            val valueActual = sut.isEndTimeLowerBoundToday()
            // Assert
            assertEquals(valueExpected2, valueActual)
        }

        @Test
        @DisplayName("When getEndTimeDaysAfterToday called, then last set value returned")
        fun getEndTimeDaysAfterToday_setValueReturned() {
            // Arrange
            val resultExpected = 7
            val resultExpected2 = 1
            every { applicationMock.getString(any()) } returns endTimeDaysAfterTodayLabel
            every { sharedPreferencesMock.edit().putInt(endTimeDaysAfterTodayLabel, capture(slotParamsInt)) } answers { nothing }
            sut.setEndTimeDaysAfterToday(resultExpected)
            sut.setEndTimeDaysAfterToday(resultExpected2)
            every { sharedPreferencesMock.getInt(endTimeDaysAfterTodayLabel, any()) } returns slotParamsInt.captured
            // Act
            val resultActual = sut.getEndTimeDaysAfterToday()
            // Assert
            assertEquals(resultExpected2, resultActual)
        }

        @Test
        @DisplayName("When getEndTimeUpperBound called, then last set date returned")
        fun getEndTimeUpperBound_setDateReturned() {
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
        fun getDurationLowerBound_setDurationReturned() {
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
        fun getDurationUpperBound_setDurationReturned() {
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
