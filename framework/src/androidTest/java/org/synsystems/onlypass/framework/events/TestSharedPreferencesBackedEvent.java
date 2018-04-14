package org.synsystems.onlypass.framework.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TestSharedPreferencesBackedEvent {
  private SharedPreferences sharedPreferences;

  private SharedPreferencesBackedEvent event;

  @Before
  public void setup() {
    sharedPreferences = InstrumentationRegistry
        .getContext()
        .getSharedPreferences("test", Context.MODE_PRIVATE);

    // In case tear down failed
    sharedPreferences
        .edit()
        .clear()
        .commit();

    event = new SharedPreferencesBackedEvent(sharedPreferences, "key");
  }

  @After
  public void tearDown() {
    sharedPreferences
        .edit()
        .clear()
        .commit();
  }

  @Test
  public void testHasOccurred_occurrenceNeverRecorded() {
    event
        .hasOccurred()
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValues(false)
        .assertComplete();
  }

  @Test
  public void testHasOccurred_occurrenceRecorded() {
    event
        .recordOccurrence()
        .andThen(event.hasOccurred())
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValues(true)
        .assertComplete();
  }

  @Test
  public void testHasOccurred_occurrenceRecordedThenCleared() {
    event
        .recordOccurrence()
        .andThen(event.clearRecord())
        .andThen(event.hasOccurred())
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertValues(false)
        .assertComplete();
  }

  @Test
  public void testClearRecord_occurrenceNeverRecorded() {
    event
        .clearRecord()
        .test()
        .awaitDone(200, TimeUnit.MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();
  }
}