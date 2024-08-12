package com.fitmate.fitmate.util

import java.time.DateTimeException
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateParseUtils {

    companion object {

        const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSSXXX"

        fun instantToString(instant: Instant?): String {
            instant ?: return ""

            val zonedDateTime = instant.atZone(ZoneId.systemDefault())

            val formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT)

            val formattedDate = zonedDateTime.format(formatter)

            return formattedDate
        }

        fun stringToInstant(stringInstantDate: String): Instant {
            val formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT)

            var result: Instant? = null

            try {
                val offsetDateTime = OffsetDateTime.parse(stringInstantDate, formatter)

                result = offsetDateTime.toInstant()
            } catch (e: DateTimeException) {
                throw DateTimeException("error format string to instant")
            }

            return result
        }
    }
}