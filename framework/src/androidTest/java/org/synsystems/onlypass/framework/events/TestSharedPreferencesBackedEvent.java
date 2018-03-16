package org.synsystems.onlypass.framework.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TestSharedPreferencesBackedEvent {
  private SharedPreferences sharedPreferences;

  private SharedPreferencesBackedEvent event;

  private LocalDateTime now;

  @Before
  public void setup() {
    sharedPreferences = InstrumentationRegistry.getContext().getSharedPreferences("test", Context.MODE_PRIVATE);

    event = new SharedPreferencesBackedEvent(sharedPreferences, "key", () -> now);
  }

  @After
  public void tearDown() {
    sharedPreferences
        .edit()
        .clear()
        .apply();
  }

  @Test
  public void testGetLastOccurrence_noOccurrencesHaveBeenDeclared() {
    event
        .getLatestOccurrence()
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();
  }

  @Test
  public void testGetLastOccurrence_oneOccurrenceHasBeenDeclared() {
    final LocalDateTime time = new LocalDateTime(2018, 2, 16, 12, 0);

    event
        .declareOccurredAt(time)
        .andThen(event.getLatestOccurrence())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(time)
        .assertComplete();
  }

  @Test
  public void testGetLastOccurrence_twoDistinctOccurrencesHaveBeenDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(2018, 2, 16, 13, 0);

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .andThen(event.getLatestOccurrence())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(time2)
        .assertComplete();
  }

  @Test
  public void testGetLastOccurrence_twoSimultaneousOccurrencesHaveBeenDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(time1);

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .andThen(event.getLatestOccurrence())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(time1)
        .assertComplete();
  }

  @Test
  public void testGetLastOccurrence_declaredAsHavingOccurringNow() {
    now = new LocalDateTime(2016, 2, 16, 12, 0);

    event
        .declareOccurredNow()
        .andThen(event.getLatestOccurrence())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(now)
        .assertComplete();
  }

  @Test
  public void testGetAllOccurrences_noOccurrencesHaveBeenDeclared() {
    event
        .getAllOccurrences()
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();
  }

  @Test
  public void testGetAllOccurrences_oneOccurrenceHasBeenDeclared() {
    final LocalDateTime time = new LocalDateTime(2018, 2, 16, 12, 0);

    event
        .declareOccurredAt(time)
        .andThen(event.getAllOccurrences())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(time)
        .assertComplete();
  }

  @Test
  public void testGetAllOccurrences_twoDistinctOccurrencesHaveBeenDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(2018, 2, 16, 13, 0);

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .andThen(event.getAllOccurrences())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValues(time1, time2)
        .assertComplete();
  }

  @Test
  public void testGetAllOccurrences_twoSimultaneousOccurrencesHaveBeenDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(time1);

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .andThen(event.getAllOccurrences())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValues(time1)
        .assertComplete();
  }

  @Test
  public void testGetAllOccurrence_declaredAsHavingOccurringNow() {
    now = new LocalDateTime(2016, 2, 16, 12, 0);

    event
        .declareOccurredNow()
        .andThen(event.getLatestOccurrence())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(now)
        .assertComplete();
  }
}