package org.synsystems.onlypass.framework.events;

import android.support.annotation.NonNull;

import org.joda.time.LocalDateTime;
import org.synsystems.onlypass.framework.rxutils.Pulse;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Persistent record of an event that can happen during app execution.
 */
public interface Event {
  @NonNull
  public Observable<Pulse> observeOccurrences();

  @NonNull
  public Maybe<LocalDateTime> getLatestOccurrence();

  @NonNull
  public Observable<LocalDateTime> getAllOccurrences();

  @NonNull
  public Completable declareOccurredNow();

  @NonNull
  public Completable declareOccurredAt(@NonNull LocalDateTime time);
}