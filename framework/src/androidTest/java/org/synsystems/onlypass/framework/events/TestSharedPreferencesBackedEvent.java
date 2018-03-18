package org.synsystems.onlypass.framework.events;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TestSharedPreferencesBackedEvent {
  private SharedPreferences sharedPreferences;

  private SharedPreferencesBackedEvent event;

  @Before
  public void setup() {
    sharedPreferences = InstrumentationRegistry.getContext().getSharedPreferences("test", Context.MODE_PRIVATE);

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
  public void testGetNewOccurrences_oneOccurrencePreviouslyDeclared() {
    event
        .declareOccurredAt(LocalDateTime.now())
        .test()
        .awaitDone(200, MILLISECONDS);

    event
        .observeNewOccurrences()
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testGetNewOccurrences_noOccurrencesDeclared() {
    event
        .observeNewOccurrences()
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testGetNewOccurrences_oneNewOccurrenceDeclared() {
    final LocalDateTime time = new LocalDateTime(2018, 2, 16, 12, 0);

    final TestObserver<LocalDateTime> occurrences = event
        .observeNewOccurrences()
        .test();

    event
        .declareOccurredAt(time)
        .test()
        .awaitDone(200, MILLISECONDS);

    occurrences
        .assertNoErrors()
        .assertValues(time)
        .assertNotComplete();
  }

  @Test
  public void testGetNewOccurrences_twoDistinctOccurrencesDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(2018, 2, 16, 13, 0);

    final TestObserver<LocalDateTime> occurrences = event
        .observeNewOccurrences()
        .test();

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .test()
        .awaitDone(200, MILLISECONDS);

    occurrences
        .assertNoErrors()
        .assertValues(time1, time2)
        .assertNotComplete();
  }

  @Test
  public void testGetNewOccurrences_twoIdenticalOccurrencesDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(2018, 2, 16, 12, 0);

    final TestObserver<LocalDateTime> occurrences = event
        .observeNewOccurrences()
        .test();

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .test()
        .awaitDone(200, MILLISECONDS);

    occurrences
        .assertNoErrors()
        .assertValues(time1)
        .assertNotComplete();
  }

  @Test
  public void testGetPastOccurrences_noOccurrencesHaveBeenDeclared() {
    event
        .observePastOccurrences()
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertNoValues()
        .assertComplete();
  }

  @Test
  public void testGetPastOccurrences_oneOccurrenceHasBeenDeclared() {
    final LocalDateTime time = new LocalDateTime(2018, 2, 16, 12, 0);

    event
        .declareOccurredAt(time)
        .andThen(event.observePastOccurrences())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(time)
        .assertComplete();
  }

  @Test
  public void testGetPastOccurrences_twoDistinctOccurrencesHaveBeenDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(2018, 2, 16, 13, 0);

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .andThen(event.observePastOccurrences())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValues(time1, time2)
        .assertComplete();
  }

  @Test
  public void testGetPastOccurrences_twoIdenticalOccurrencesHaveBeenDeclared() {
    final LocalDateTime time1 = new LocalDateTime(2018, 2, 16, 12, 0);
    final LocalDateTime time2 = new LocalDateTime(time1);

    event
        .declareOccurredAt(time1)
        .andThen(event.declareOccurredAt(time2))
        .andThen(event.observePastOccurrences())
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValues(time1)
        .assertComplete();
  }
}