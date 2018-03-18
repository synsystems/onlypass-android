package org.synsystems.onlypass.framework.events;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.HashSet;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An event that uses shared preferences to store the occurrence history.
 */
public class SharedPreferencesBackedEvent implements Event {
  private final PublishSubject<LocalDateTime> newOccurrences = PublishSubject.create();

  private final SharedPreferences sharedPrefs;

  private final String prefKey;

  /**
   * Constructs a new SharedPreferencesBackedEvent.
   *
   * @param sharedPrefs
   *     the preferences to store the occurrence history in
   * @param prefKey
   *     the key to store the occurrence history with
   */
  public SharedPreferencesBackedEvent(@NonNull final SharedPreferences sharedPrefs, @NonNull final String prefKey) {
    this.sharedPrefs = checkNotNull(sharedPrefs);
    this.prefKey = checkNotNull(prefKey);
  }

  @NonNull
  @Override
  public Observable<LocalDateTime> observeNewOccurrences() {
    return newOccurrences.distinct();
  }

  @NonNull
  @Override
  public Observable<LocalDateTime> observePastOccurrences() {
    return Single
        .fromCallable(() -> sharedPrefs.getStringSet(prefKey, new HashSet<>()))
        .flatMapObservable(Observable::fromIterable)
        .flatMapSingle(this::deserialiseLocalDateTime);
  }

  @NonNull
  @Override
  public Completable declareOccurredAt(@NonNull final LocalDateTime time) {
    checkNotNull(time);

    return observePastOccurrences()
        .concatWith(Observable.just(time))
        .distinct()
        .flatMapSingle(this::serialiseLocalDateTime)
        .collectInto(new HashSet<String>(), HashSet::add)
        .flatMapCompletable(set -> Completable.fromRunnable(() -> sharedPrefs
            .edit()
            .putStringSet(prefKey, set)
            .apply()))
        .andThen(Completable.fromRunnable(() -> newOccurrences.onNext(time)));
  }

  private Single<String> serialiseLocalDateTime(@NonNull final LocalDateTime date) {
    return Single
        .just(date)
        .map(it -> it.toDateTime(DateTimeZone.UTC))
        .map(DateTime::getMillis)
        .map(millisecondsSinceEpoch -> Long.toString(millisecondsSinceEpoch));
  }

  private Single<LocalDateTime> deserialiseLocalDateTime(@NonNull final String date) {
    return Single
        .just(date)
        .map(Long::parseLong)
        .map(millisecondsSinceEpoch -> new LocalDateTime(millisecondsSinceEpoch, DateTimeZone.UTC));
  }
}