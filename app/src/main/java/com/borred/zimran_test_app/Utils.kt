package com.borred.zimran_test_app

import android.util.Log
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.TreeMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch(context) {
        try {
            block()
        } catch (ce: CancellationException) {
            // You can ignore or log this exception
        } catch (e: Exception) {
            // Here it's better to at least log the exception
            Log.e("TAG", "Coroutine error", e)
        }
    }
}

private val suffixes = TreeMap<Long, String>().apply {
    this[1_000] = "k"
    this[1_000_000] = "m"
    this[1_000_000_000] = "G"
    this[1_000_000_000_000L] = "T"
    this[1_000_000_000_000_000L] = "P"
    this[1_000_000_000_000_000_000L] = "E"
}

@Stable
fun Int.prettyNumber(): String {
    return this.toLong().prettyNumber()
}

@Stable
fun Long.prettyNumber(): String {
    // Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
    if (this == Long.MIN_VALUE) return (Long.MIN_VALUE + 1).prettyNumber()
    if (this < 0) return "-${(-this).prettyNumber()}"
    if (this < 1000) return "$this"

    val entry = suffixes.floorEntry(this)
    val divideBy = entry?.key ?: return "$this"
    val suffix = entry.value
    val truncated = this / (divideBy / 10) // the number part of the output times 10
    val hasDecimal = truncated < 100 && (truncated / 10.0) != (truncated / 10).toDouble()
    return if (hasDecimal) {
        "${truncated / 10.0}$suffix"
    } else {
        "${truncated / 10}$suffix"
    }
}
