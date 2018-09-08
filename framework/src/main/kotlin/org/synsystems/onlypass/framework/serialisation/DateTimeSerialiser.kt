package org.synsystems.onlypass.framework.serialisation

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.joda.time.DateTime
import java.lang.reflect.Type

/**
 * Serialises and deserialises [DateTime] instances using JSON.
 */
class DateTimeSerialiser : JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
  @Throws(JsonParseException::class)
  override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DateTime {
    val millisFromEpoch = json
        .asJsonObject
        .get(TAG)
        .asLong

    return DateTime(millisFromEpoch)
  }

  override fun serialize(src: DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonObject {
    return JsonObject().apply { addProperty(TAG, src.millis) }
  }

  private companion object {
    private const val TAG = "millisecondsFromEpochUtc"
  }
}