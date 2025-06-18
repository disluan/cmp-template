package com.sources

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform