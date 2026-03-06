package com.showedup.app.crypto

import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Deterministic JSON serialization with sorted keys.
 * Ensures identical records always produce identical JSON bytes for hashing.
 */
@Singleton
class CanonicalJson @Inject constructor() {

    /**
     * Converts a map to canonical JSON bytes (sorted keys, no whitespace).
     */
    fun toCanonicalBytes(data: Map<String, Any?>): ByteArray {
        return toCanonicalString(data).toByteArray(Charsets.UTF_8)
    }

    /**
     * Converts a map to canonical JSON string.
     */
    fun toCanonicalString(data: Map<String, Any?>): String {
        return canonicalize(data)
    }

    private fun canonicalize(value: Any?): String {
        return when (value) {
            null -> "null"
            is Boolean -> value.toString()
            is Number -> {
                // Ensure consistent number formatting
                if (value is Double || value is Float) {
                    val d = value.toDouble()
                    if (d == d.toLong().toDouble()) {
                        d.toLong().toString()
                    } else {
                        d.toString()
                    }
                } else {
                    value.toString()
                }
            }
            is String -> JSONObject.quote(value)
            is Map<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val sorted = (value as Map<String, Any?>).toSortedMap()
                val entries = sorted.map { (k, v) ->
                    "${JSONObject.quote(k)}:${canonicalize(v)}"
                }
                "{${entries.joinToString(",")}}"
            }
            is List<*> -> {
                val elements = value.map { canonicalize(it) }
                "[${elements.joinToString(",")}]"
            }
            is ByteArray -> {
                // Encode byte arrays as hex strings
                JSONObject.quote(value.joinToString("") { "%02x".format(it) })
            }
            else -> JSONObject.quote(value.toString())
        }
    }
}
