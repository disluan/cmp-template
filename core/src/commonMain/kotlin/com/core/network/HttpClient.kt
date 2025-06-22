package com.core.network

import com.firebase.auth.Auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.PlatformUtils
import kotlinx.serialization.json.Json

internal object HttpClient {

    private val jsonConfig = Json {
        isLenient = true
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
        allowStructuredMapKeys = true
        allowSpecialFloatingPointValues = true
    }

    private val loggerLevel = when(PlatformUtils.IS_DEVELOPMENT_MODE) {
        true -> LogLevel.BODY
        false -> LogLevel.NONE
    }

    fun get(auth: Auth): HttpClient {
        return HttpClient {
            expectSuccess = true

            install(ContentNegotiation) {
                json(json = jsonConfig)
            }
            install(Auth) {
                bearerAuth(auth)
            }
            install(Logging) {
                level = loggerLevel
            }
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    when(exception) {
                        is ResponseException -> throw ApiException(exception.response.body<ApiError>())
                        else -> throw exception
                    }
                }
            }
        }
    }

    private fun AuthConfig.bearerAuth(auth: Auth) {
        bearer {
            loadTokens {
                auth.getAuthToken(false).getOrNull()?.let { token ->
                    BearerTokens(token, null)
                }
            }
            refreshTokens {
                auth.getAuthToken(true).getOrNull()?.let { token ->
                    BearerTokens(token, null)
                }
            }
        }
    }
}