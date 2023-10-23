package com.borred.zimran_test_app.repositories

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.Duration
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val FORMAT_DATE = "yyyy-MM-dd"
private const val FORMAT_d_MMM = "d MMM"
private const val FORMAT_HH_mm = "HH:mm"

internal fun Instant.format(): String {
    val uploadedTime = this.toJavaInstant()
        .atOffset(ZoneOffset.UTC)
    val now = OffsetDateTime.now()

    val months = Period.between(
        uploadedTime.toLocalDate(),
        now.toLocalDate()
    ).months
    if (months > 12) {
        return uploadedTime.format(
            pattern = FORMAT_DATE
        )
    }

    var duration = Duration.between(
        uploadedTime,
        now
    )
    if (duration.isNegative) {
        duration = Duration.ZERO
    }
    val days = duration.toDays()
    if (days > 0) {
        return uploadedTime.format(
            pattern = FORMAT_d_MMM
        )
    }
    return uploadedTime.format(
        pattern = FORMAT_HH_mm
    )
}

private fun OffsetDateTime.format(
    pattern: String,
    timeZone: ZoneOffset = ZoneOffset.UTC
): String {
    return atZoneSameInstant(timeZone).format(
        DateTimeFormatter.ofPattern(pattern)
    )
}
