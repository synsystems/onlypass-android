package org.synsystems.onlypass.framework.events;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.common.base.Supplier;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.HashSet;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

public class SharedPreferencesBackedEvent implements Event {
  private final SharedPreferences sharedPrefs;

  private final String prefKey;

  private final Supplier<LocalDateTime> nowSupplier;

  public SharedPreferencesBackedEvent(
      @NonNull final SharedPreferences sharedPrefs,
      @NonNull final String prefKey,
      @NonNull final Supplier<LocalDateTime> nowSupplier) {

    this.sharedPrefs = checkNotNull(sharedPrefs);
    this.prefKey = checkNotNull(prefKey);
    this.nowSupplier = checkNotNull(nowSupplier);
  }

  @NonNull
  @Override
  public Maybe<LocalDateTime> getLatestOccurrence() {
    return getAllOccurrences()
        .sorted(LocalDateTime::compareTo)
        .lastElement();
  }

  @NonNull
  @Override
  public Observable<LocalDateTime> getAllOccurrences() {
    return Single
        .fromCallable(() -> sharedPrefs.getStringSet(prefKey, new HashSet<>()))
        .flatMapObservable(Observable::fromIterable)
        .distinct()
        .flatMapSingle(this::deserialiseLocalDateTime);
  }

  @NonNull
  @Override
  public Completable declareOccurredNow() {
    return declareOccurredAt(nowSupplier.get());
  }

  @NonNull
  @Override
  public Completable declareOccurredAt(@NonNull final LocalDateTime time) {
    checkNotNull(time);

    return getAllOccurrences()
        .concatWith(Observable.just(time))
        .flatMapSingle(this::serialiseLocalDateTime)
        .collectInto(new HashSet<String>(), HashSet::add)
        .flatMapCompletable(set -> Completable.fromRunnable(() -> sharedPrefs
            .edit()
            .putStringSet(prefKey, set)
            .apply()));
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