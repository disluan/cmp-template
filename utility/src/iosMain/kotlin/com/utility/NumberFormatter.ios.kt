package com.utility

import platform.Foundation.NSLocale
import platform.Foundation.NSNumberFormatter

actual class PlatformNumberFormat actual constructor(languageTag: String) {

    private val formatter = NSNumberFormatter()

    init {
        formatter.locale = NSLocale(languageTag)
    }

    actual val currencySymbol: String
        get() = formatter.currencySymbol

    actual val decimalSeparator: String
        get() = formatter.decimalSeparator

    actual val groupingSeparator: String
        get() = formatter.groupingSeparator
}