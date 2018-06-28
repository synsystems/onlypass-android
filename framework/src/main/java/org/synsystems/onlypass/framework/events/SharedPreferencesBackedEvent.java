package org.synsystems.onlypass.framework.events;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An event that records occurrences in shared preferences.
 */
public class SharedPreferencesBackedEvent implements Event {
  private final SharedPreferences sharedPrefs;

  private final String key;

  /**
   * Constructs a new SharedPreferencesBackedEvent.
   *
   * @param sharedPrefs
   *     the shared preferences to store the record in
   * @param key
   *     the shared preferences key to use when storing the event
   */
  public SharedPreferencesBackedEvent(@NonNull final SharedPreferences sharedPrefs, @NonNull final String key) {
    this.sharedPrefs = checkNotNull(sharedPrefs);
    this.key = checkNotNull(key);
  }

  @NonNull
  @Override
  public Single<Boolean> hasOccurred() {
    return Single.fromCallable(() -> sharedPrefs.getBoolean(key, false));
  }

  @NonNull
  @Override
  public Completable recordOccurrence() {
    return Completable.fromRunnable(() -> sharedPrefs
        .edit()
        .putBoolean(key, true)
        .apply());
  }

  @NonNull
  @Override
  public Completable clearRecord() {
    return Completable.fromRunnable(() -> sharedPrefs
        .edit()
        .remove(key)
        .apply());
  }
}