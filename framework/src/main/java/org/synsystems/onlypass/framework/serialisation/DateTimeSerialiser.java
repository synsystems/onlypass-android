package org.synsystems.onlypass.framework.serialisation;

import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Serialises and deserialises {@link DateTime} instances to/from JSON.
 */
public class DateTimeSerialiser implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
  private static final String TAG = "millisFromEpochUtc";

  @Override
  public DateTime deserialize(
      @NonNull final JsonElement json,
      @NonNull final Type typeOfT,
      @NonNull final JsonDeserializationContext context)
      throws JsonParseException {

    checkNotNull(json);
    checkNotNull(typeOfT);
    checkNotNull(context);

    final long millisFromEpoch = json
        .getAsJsonObject()
        .get(TAG)
        .getAsLong();

    return new DateTime(millisFromEpoch);
  }

  @Override
  public JsonElement serialize(
      @NonNull final DateTime src,
      @NonNull final Type typeOfSrc,
      @NonNull final JsonSerializationContext context) {

    checkNotNull(src);
    checkNotNull(typeOfSrc);
    checkNotNull(context);

    final JsonObject json = new JsonObject();

    json.addProperty(TAG, src.getMillis());

    return json;
  }
}