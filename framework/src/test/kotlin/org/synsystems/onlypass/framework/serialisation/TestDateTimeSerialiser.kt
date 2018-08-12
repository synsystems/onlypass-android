package org.synsystems.onlypass.framework.serialisation

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test

class TestDateTimeSerialiser {

  private lateinit var gson: Gson

  @Before
  fun setup() {
    gson = GsonBuilder()
        .registerTypeAdapter(DateTime::class.java, DateTimeSerialiser())
        .create()
  }

  @Test
  fun testSerialiseAndDeserialiseWithGson() {
    val date = DateTime(2018, 12, 25, 12, 30)

    val serialisedDate = gson.toJson(date)
    val deserialisedDate = gson.fromJson(serialisedDate, DateTime::class.java)

    assertThat(date, `is`(deserialisedDate))
  }
}