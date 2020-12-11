package com.zeronfinity.cpfy.model

import com.zeronfinity.core.entity.Platform
import com.zeronfinity.core.entity.ServerContestInfoResponse
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.FAILURE
import com.zeronfinity.core.entity.ServerContestInfoResponse.ResponseStatus.SUCCESS
import com.zeronfinity.cpfy.framework.network.ErrorResponse
import com.zeronfinity.cpfy.framework.network.ResultWrapper.*
import com.zeronfinity.cpfy.model.network.ClistNetworkCall
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse
import com.zeronfinity.cpfy.model.network.pojo.ClistResourceResponse
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import retrofit2.Response

internal class ServerContestInfoClistTest {
    private val emptyParamsMap: Map<String, String> = mapOf()
    private val validParamsMap: Map<String, String> = mapOf("key1" to "val1", "key2" to "val2")

    private val platformResource = ClistResourceResponse("/icon_url_segment", "platform_name")
    private val contestObject1 = ClistContestObjectResponse("title1", "2020-12-30T07:00:00", "2020-12-30T08:00:00", "url1", platformResource)
    private val contestObject2 = ClistContestObjectResponse("title2", "2020-12-20T08:00:00", "2020-12-22T08:00:00", "url2", platformResource)

    private val serverResponseResult = Success(Response.success(ClistServerResponse(listOf(contestObject1, contestObject2))))
    private val serverResponseExpectedForSuccess = ServerContestInfoResponse(
        SUCCESS,
        null,
        null,
        listOf(contestObject1.toContest(), contestObject2.toContest()),
        listOf(
            Platform("platform_name", "https://clist.by" + "/icon_url_segment"),
            Platform("platform_name", "https://clist.by" + "/icon_url_segment")
        )
    )

    private val errorCode = 404
    private val errorResponse = ErrorResponse("error_description")
    private val genericError = GenericError(errorCode, errorResponse)
    private val responseExpectedForGenericError = ServerContestInfoResponse(
        FAILURE,
        404,
        "error_description",
        null,
        null
    )

    private val networkError = NetworkError
    private val responseExpectedForNetworkError = ServerContestInfoResponse(
        FAILURE,
        null,
        "Network Failure: IOException!",
        null,
        null
    )

    private val clistNetworkCallMock = mockk<ClistNetworkCall>()
    private val sut = ServerContestInfoClist(clistNetworkCallMock)

    @Nested
    @DisplayName("Given network success")
    inner class NetworkSuccess {
        @Test
        @DisplayName("When getInfo called with empty parameters, then getContestData is called with correct parameters")
        internal fun getInfo_noParam_getContestDataCalledWithCorrectParameters() {
            // Arrange
            val slotParamsMap = slot<Map<String, String>>()
            coEvery { clistNetworkCallMock.getContestData(capture(slotParamsMap)) } returns serverResponseResult
            // Act
            runBlocking { sut.getInfo(emptyParamsMap) }
            // Assert
            assertEquals(emptyParamsMap, slotParamsMap.captured)
        }

        @Test
        @DisplayName("When getInfo called with valid parameters, then getContestData is called with correct parameters")
        internal fun getInfo_validParams_getContestDataCalledWithCorrectParameters() {
            // Arrange
            val slotParamsMap = slot<Map<String, String>>()
            coEvery { clistNetworkCallMock.getContestData(capture(slotParamsMap)) } returns serverResponseResult
            // Act
            runBlocking { sut.getInfo(validParamsMap) }
            // Assert
            assertEquals(validParamsMap, slotParamsMap.captured)
        }

        @Test
        @DisplayName("When getInfo called, then correct result returned")
        internal fun getInfo_success_correctServerContestInfoResponseReturned() {
            // Arrange
            coEvery { clistNetworkCallMock.getContestData(any()) } returns serverResponseResult
            // Act
            val serverResponseActual = runBlocking { sut.getInfo(validParamsMap) }
            // Assert
            assertEquals(serverResponseExpectedForSuccess, serverResponseActual)
        }
    }

    @Nested
    @DisplayName("Given generic error")
    inner class GenericError {
        @Test
        @DisplayName("When getInfo called with empty parameters, then getContestData is called with correct parameters")
        internal fun getInfo_noParam_getContestDataCalledWithCorrectParameters() {
            // Arrange
            val slotParamsMap = slot<Map<String, String>>()
            coEvery { clistNetworkCallMock.getContestData(capture(slotParamsMap)) } returns genericError
            // Act
            runBlocking { sut.getInfo(emptyParamsMap) }
            // Assert
            assertEquals(emptyParamsMap, slotParamsMap.captured)
        }

        @Test
        @DisplayName("When getInfo called with valid parameters, then getContestData is called with correct parameters")
        internal fun getInfo_validParams_getContestDataCalledWithCorrectParameters() {
            // Arrange
            val slotParamsMap = slot<Map<String, String>>()
            coEvery { clistNetworkCallMock.getContestData(capture(slotParamsMap)) } returns genericError
            // Act
            runBlocking { sut.getInfo(validParamsMap) }
            // Assert
            assertEquals(validParamsMap, slotParamsMap.captured)
        }

        @Test
        @DisplayName("When getInfo called, then correct result returned")
        internal fun getInfo_success_correctServerContestInfoResponseReturned() {
            // Arrange
            coEvery { clistNetworkCallMock.getContestData(any()) } returns genericError
            // Act
            val serverResponseActual = runBlocking { sut.getInfo(validParamsMap) }
            // Assert
            assertEquals(responseExpectedForGenericError, serverResponseActual)
        }
    }

    @Nested
    @DisplayName("Given network error")
    inner class NetworkError {
        @Test
        @DisplayName("When getInfo called with empty parameters, then getContestData is called with correct parameters")
        internal fun getInfo_noParam_getContestDataCalledWithCorrectParameters() {
            // Arrange
            val slotParamsMap = slot<Map<String, String>>()
            coEvery { clistNetworkCallMock.getContestData(capture(slotParamsMap)) } returns networkError
            // Act
            runBlocking { sut.getInfo(emptyParamsMap) }
            // Assert
            assertEquals(emptyParamsMap, slotParamsMap.captured)
        }

        @Test
        @DisplayName("When getInfo called with valid parameters, then getContestData is called with correct parameters")
        internal fun getInfo_validParams_getContestDataCalledWithCorrectParameters() {
            // Arrange
            val slotParamsMap = slot<Map<String, String>>()
            coEvery { clistNetworkCallMock.getContestData(capture(slotParamsMap)) } returns networkError
            // Act
            runBlocking { sut.getInfo(validParamsMap) }
            // Assert
            assertEquals(validParamsMap, slotParamsMap.captured)
        }

        @Test
        @DisplayName("When getInfo called, then correct result returned")
        internal fun getInfo_success_correctServerContestInfoResponseReturned() {
            // Arrange
            coEvery { clistNetworkCallMock.getContestData(any()) } returns networkError
            // Act
            val serverResponseActual = runBlocking { sut.getInfo(validParamsMap) }
            // Assert
            assertEquals(responseExpectedForNetworkError, serverResponseActual)
        }
    }
}