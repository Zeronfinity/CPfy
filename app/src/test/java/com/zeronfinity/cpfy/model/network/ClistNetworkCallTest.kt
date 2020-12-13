package com.zeronfinity.cpfy.model.network

import com.squareup.moshi.Moshi
import com.zeronfinity.cpfy.CustomApplication
import com.zeronfinity.cpfy.R
import com.zeronfinity.cpfy.framework.network.ResultWrapper.*
import com.zeronfinity.cpfy.model.network.pojo.ClistContestObjectResponse
import com.zeronfinity.cpfy.model.network.pojo.ClistResourceResponse
import com.zeronfinity.cpfy.model.network.pojo.ClistServerResponse
import com.zeronfinity.cpfy.network.ClistApiClientMock
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.function.Executable
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

internal class ClistNetworkCallTest {
    /* Helper constant fields */

    private val username = "username"
    private val apiKey = "test_api_key"

    private val emptyParamsMap: Map<String, String> = mapOf()
    private val validParamsMap: Map<String, String> = mapOf("key1" to "val1", "key2" to "val2")

    private val platformResource = ClistResourceResponse("/icon_url_segment", "platform_name")
    private val contestObject1 = ClistContestObjectResponse(
        "title1",
        "2020-12-30T07:00:00",
        "2020-12-30T08:00:00",
        "url1",
        platformResource
    )
    private val contestObject2 = ClistContestObjectResponse(
        "title2",
        "2020-12-20T08:00:00",
        "2020-12-22T08:00:00",
        "url2",
        platformResource
    )

    private val emptyParamServerResponseJson = convertToJson(
        ClistServerResponse(
            listOf(
                contestObject1,
                contestObject2
            )
        )
    )
    private val validParamServerResponseJson = convertToJson(
        ClistServerResponse(
            listOf(
                contestObject1
            )
        )
    )

    private val emptyParamResponseExpected = Response.success(
        ClistServerResponse(
            listOf(
                contestObject1,
                contestObject2
            )
        )
    )
    private val validParamResponseExpected = Response.success(
        ClistServerResponse(
            listOf(
                contestObject1
            )
        )
    )

    private val emptyParamQueryUrl = "/api/v1/json/contest/?username=$username&api_key=$apiKey"
    private val validParamQueryUrl = "/api/v1/json/contest/?username=$username&api_key=$apiKey&key1=val1&key2=val2"

    /* Unit tests start from here */

    private val applicationMock = mockk<CustomApplication>()
    private val clistApiMock = ClistApiClientMock()
    private val sut = ClistNetworkCall(applicationMock, clistApiMock.getClistApi())

    init {
        every { applicationMock.getString(R.string.clist_api_username) } returns username
        every { applicationMock.getClistApiKey() } returns apiKey
    }

    @Nested
    @DisplayName("Given network success")
    inner class NetworkSuccess {
        @Test
        @DisplayName("When getContestData called with empty parameters, then correct request is made to server")
        internal fun getContestData_noParam_serverRequestedCorrectly() {
            print("test start")
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(emptyParamServerResponseJson)
            )
            // Act
            runBlocking { sut.getContestData(emptyParamsMap) }
            // Assert
            val request = clistApiMock.getRequestMadeToMockServer()
            assertEquals(
                emptyParamQueryUrl,
                request.path,
                "Server is requested with incorrect parameters in query path"
            )
            print("test end")
        }

        @Test
        @DisplayName("When getContestData called with valid parameters, then correct request is made to server")
        internal fun getContestData_validParams_serverRequestedCorrectly() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(validParamServerResponseJson)
            )
            // Act
            runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            val request = clistApiMock.getRequestMadeToMockServer()
            assertEquals(
                validParamQueryUrl,
                request.path,
                "Server is requested with incorrect parameters in query path"
            )
        }

        @Test
        @DisplayName("When getContestData called with empty parameters, then correct result returned")
        internal fun getContestData_noParam_correctResultReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(emptyParamServerResponseJson)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(emptyParamsMap) }
            // Assert
            when (wrappedResultActual) {
                is Success -> assertAll(
                    Executable {
                        assertEquals(
                            HttpURLConnection.HTTP_OK,
                            wrappedResultActual.value.code(),
                            "Response code mismatched"
                        )
                    },
                    Executable {
                        assertEquals(
                            emptyParamResponseExpected.message(),
                            wrappedResultActual.value.message(),
                            "Response message mismatched"
                        )
                    },
                    Executable {
                        assertEquals(
                            emptyParamResponseExpected.body(),
                            wrappedResultActual.value.body(),
                            "Response body mismatched"
                        )
                    }
                )
                else -> fail("Wrapped result is not of type ResultWrapper.Success!")
            }
        }

        @Test
        @DisplayName("When getContestData called with valid parameters, then correct result returned")
        internal fun getContestData_validParams_correctResultReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(validParamServerResponseJson)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            when (wrappedResultActual) {
                is Success -> assertAll(
                    Executable {
                        assertEquals(
                            HttpURLConnection.HTTP_OK,
                            wrappedResultActual.value.code(),
                            "Response code mismatched"
                        )
                    },
                    Executable {
                        assertEquals(
                            validParamResponseExpected.message(),
                            wrappedResultActual.value.message(),
                            "Response message mismatched"
                        )
                    },
                    Executable {
                        assertEquals(
                            validParamResponseExpected.body(),
                            wrappedResultActual.value.body(),
                            "Response body mismatched"
                        )
                    }
                )
                else -> fail("Wrapped result is not of type ResultWrapper.Success!")
            }
        }
    }

    @Nested
    @DisplayName("Given valid network response with error code")
    inner class ValidResponseWithErrorCode {
        @Test
        @DisplayName("When getContestData called with empty parameters, then correct result returned")
        internal fun getContestData_noParam_correctResultReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                    .setBody(emptyParamServerResponseJson)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(emptyParamsMap) }
            // Assert
            when (wrappedResultActual) {
                is GenericError -> assertEquals(
                    HttpURLConnection.HTTP_BAD_REQUEST,
                    wrappedResultActual.code,
                    "Response code mismatched"
                )
                else -> fail("Wrapped result is not of type ResultWrapper.GenericError!")
            }
        }

        @Test
        @DisplayName("When getContestData called with valid parameters, then correct result returned")
        internal fun getContestData_validParams_correctResultReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                    .setBody(validParamServerResponseJson)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            when (wrappedResultActual) {
                is GenericError -> assertEquals(
                    HttpURLConnection.HTTP_BAD_REQUEST,
                    wrappedResultActual.code,
                    "Response code mismatched"
                )
                else -> fail("Wrapped result is not of type ResultWrapper.GenericError!")
            }
        }
    }

    @Nested
    @DisplayName("Given server disconnected at some point")
    inner class ServerDisconnectPolicies {
        @Test
        @DisplayName("When server disconnected at start, then correct result returned")
        internal fun getContestData_serverDisconnectedAtStart_networkErrorReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            assertTrue(wrappedResultActual is NetworkError, "Wrapped result is not of type ResultWrapper.NetworkError!")
        }

        @Test
        @DisplayName("When server disconnected after request, then correct result returned")
        internal fun getContestData_serverDisconnectedAfterRequest_networkErrorReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            assertTrue(wrappedResultActual is NetworkError, "Wrapped result is not of type ResultWrapper.NetworkError!")
        }

        @Test
        @DisplayName("When server disconnected at end, then correct result returned")
        internal fun getContestData_serverDisconnectedAtEnd_networkErrorReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_END)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            assertTrue(wrappedResultActual is NetworkError, "Wrapped result is not of type ResultWrapper.NetworkError!")
        }

        @Test
        @DisplayName("When server disconnected during request body, then correct result returned")
        internal fun getContestData_serverDisconnectedDuringRequestBody_networkErrorReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_DURING_REQUEST_BODY)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            assertTrue(wrappedResultActual is NetworkError, "Wrapped result is not of type ResultWrapper.NetworkError!")
        }

        @Test
        @DisplayName("When server disconnected during reponse body, then correct result returned")
        internal fun getContestData_serverDisconnectedDuringResponseBody_networkErrorReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            assertTrue(wrappedResultActual is NetworkError, "Wrapped result is not of type ResultWrapper.NetworkError!")
        }
    }

    @Nested
    @DisplayName("Given slow network")
    inner class SlowNetwork {
        @Test
        @DisplayName("When getContestData called, then NetworkError returned")
        internal fun getContestData_networkErrorReturned() {
            // Arrange
            clistApiMock.enqueMockServerResponse(
                MockResponse().throttleBody(1024, 1, TimeUnit.SECONDS)
            )
            // Act
            val wrappedResultActual = runBlocking { sut.getContestData(validParamsMap) }
            // Assert
            assertTrue(wrappedResultActual is NetworkError, "Wrapped result is not of type ResultWrapper.NetworkError!")
        }
    }

    /* Helper functions */

    private fun convertToJson(clistServerResponse: ClistServerResponse) =
        Moshi.Builder().build().adapter(ClistServerResponse::class.java).toJson(clistServerResponse)
}
