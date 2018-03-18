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

  private final String prefKey;

  /**
   * Constructs a new SharedPreferencesBackedEvent.
   *
   * @param sharedPrefs
   *     the preferences to store the record in
   * @param prefKey
   *     the key to store the record with
   */
  public SharedPreferencesBackedEvent(@NonNull final SharedPreferences sharedPrefs, @NonNull final String prefKey) {
    this.sharedPrefs = checkNotNull(sharedPrefs);
    this.prefKey = checkNotNull(prefKey);
  }

  @NonNull
  @Override
  public Single<Boolean> hasOccurred() {
    return Single.fromCallable(() -> sharedPrefs.getBoolean(prefKey, false));
  }

  @NonNull
  @Override
  public Completable recordOccurrence() {
    return Completable.fromRunnable(() -> sharedPrefs
        .edit()
        .putBoolean(prefKey, true)
        .apply());
  }

  @NonNull
  @Override
  public Completable clearRecord() {
    return Completable.fromRunnable(() -> sharedPrefs
        .edit()
        .remove(prefKey)
        .apply());
  }
}