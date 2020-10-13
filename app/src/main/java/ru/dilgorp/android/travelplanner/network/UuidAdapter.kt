package ru.dilgorp.android.travelplanner.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*
import javax.inject.Inject

class UuidAdapter @Inject constructor() {
    @FromJson
    fun fromJson(uuidString: String): UUID {
        return UUID.fromString(uuidString)
    }

    @ToJson
    fun toJson(uuid: UUID): String {
        return uuid.toString()
    }
}