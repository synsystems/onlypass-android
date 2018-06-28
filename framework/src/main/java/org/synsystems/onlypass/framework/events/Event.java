package org.synsystems.onlypass.framework.events;

import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Persistent record of an event that can happen during app execution.
 */
public interface Event {
  /**
   * @return a new single that emits true if this event has occurred, false otherwise
   */
  @NonNull
  public Single<Boolean> hasOccurred();

  /**
   * @return a new completable that records the event as having occurred
   */
  @NonNull
  public Completable recordOccurrence();

  /**
   * @return a new completable that clears any record of the event
   */
  @NonNull
  public Completable clearRecord();
}