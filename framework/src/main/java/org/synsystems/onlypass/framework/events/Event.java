package org.synsystems.onlypass.framework.events;

import android.support.annotation.NonNull;

import org.joda.time.LocalDateTime;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Persistent record of an event that can happen during app execution.
 */
public interface Event {
  @NonNull
  public Maybe<LocalDateTime> getLatestOccurrence();

  @NonNull
  public Observable<LocalDateTime> getAllOccurrences();

  @NonNull
  public Completable declareOccurredNow();

  @NonNull
  public Completable declareOccurredAt(@NonNull LocalDateTime time);
}