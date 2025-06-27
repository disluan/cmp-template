package com.utility

import java.text.DecimalFormatSymbols
import java.util.Locale

actual class PlatformNumberFormat actual constructor(languageTag: String) {

    private val formatter = DecimalFormatSymbols.getInstance(Locale.forLanguageTag(languageTag))

    actual val currencySymbol: String
        get() = formatter.currencySymbol

    actual val decimalSeparator: String
        get() = formatter.decimalSeparator.toString()

    actual val groupingSeparator: String
        get() = formatter.groupingSeparator.toString()
}