package org.synsystems.onlypass.framework.events

import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry

import org.junit.After
import org.junit.Before
import org.junit.Test

import java.util.concurrent.TimeUnit

class TestSharedPreferencesBackedEvent {

  private lateinit var sharedPreferences: SharedPreferences

  private lateinit var event: SharedPreferencesBackedEvent

  @Before
  fun setup() {
    sharedPreferences = InstrumentationRegistry
        .getContext()
        .getSharedPreferences("test", Context.MODE_PRIVATE)

    // In case previous tear down failed
    sharedPreferences.edit().clear().commit()

    event = SharedPreferencesBackedEvent(sharedPreferences, "key")
  }

  @After
  fun tearDown() {
    sharedPreferences.edit().clear().commit()
  }

  @Test
  fun testHasOccurred_occurrenceNeverRecorded() {
    event
        .hasOccurred()
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValues(false)
        .assertComplete()
  }

  @Test
  fun testHasOccurred_occurrenceRecorded() {
    event
        .recordOccurrence()
        .andThen(event.hasOccurred())
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValues(true)
        .assertComplete()
  }

  @Test
  fun testHasOccurred_occurrenceRecordedThenCleared() {
    event
        .recordOccurrence()
        .andThen(event.clearRecord())
        .andThen(event.hasOccurred())
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValues(false)
        .assertComplete()
  }

  @Test
  fun testClearRecord_occurrenceNeverRecorded() {
    event
        .clearRecord()
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete()
  }
}