package org.synsystems.onlypass.framework.events;

import android.support.annotation.NonNull;

import org.joda.time.LocalDateTime;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Persistent record of an event that can happen during app execution.
 */
public interface Event {
  /**
   * Creates an observable that emits each time a new occurrence is declared. The emitted times are not guaranteed to
   * actually be in the past, since {@link #declareOccurredAt(LocalDateTime)} does not perform validation.
   *
   * @return the new observable
   */
  @NonNull
  public Observable<LocalDateTime> observeNewOccurrences();

  /**
   * Creates an observable the occurrences that have been previously declared and then completes. The emitted times are
   * not guaranteed to actually be in the past, since {@link #declareOccurredAt(LocalDateTime)} does not perform
   * validation.
   *
   * @return the new observable
   */
  @NonNull
  public Observable<LocalDateTime> observePastOccurrences();

  /**
   * Records the supplied time as an occurrence of this event. All times are valid, including times in the future.
   *
   * @param time
   *     the time to record
   *
   * @return a completable that performs the operation
   */
  @NonNull
  public Completable declareOccurredAt(@NonNull LocalDateTime time);
}