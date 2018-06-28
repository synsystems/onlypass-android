package org.synsystems.onlypass.framework.serialisation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestDateTimeSerialiser {
  private Gson gson;

  @Before
  public void setup() {
    // Serialiser is not used directly, so instead pass an instance to Gson
    gson = new GsonBuilder()
        .registerTypeAdapter(DateTime.class, new DateTimeSerialiser())
        .create();
  }

  @Test
  public void testSerialiseAndDeserialiseWithGson() {
    final DateTime date = new DateTime(2018, 12, 25, 12, 30);

    final String serialisedDate = gson.toJson(date);
    final DateTime deserialisedDate = gson.fromJson(serialisedDate, DateTime.class);

    assertThat(date, is(deserialisedDate));
  }
}