package com.core.network

import io.ktor.util.PlatformUtils

val baseUrl = if (PlatformUtils.IS_DEVELOPMENT_MODE) {
    "http://192.168.1.25:8000/api"
} else {
    "https://example.com/api"
}