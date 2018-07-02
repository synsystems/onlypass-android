package org.synsystems.onlypass.framework.events

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single

/**
 * An [Event] that is recorded in shared preferences.
 *
 * @property sharedPrefs the shared preferences to use for persistence
 * @property key the key to use when storing events in [sharedPrefs]
 */
class SharedPreferencesBackedEvent(
    private val sharedPrefs: SharedPreferences,
    private val key: String
) : Event {

  override fun hasOccurred() = Single.fromCallable { sharedPrefs.getBoolean(key, false) }

  override fun recordOccurrence() = Completable.fromRunnable {
    sharedPrefs
        .edit()
        .putBoolean(key, true)
        .apply()
  }

  override fun clearRecord() = Completable.fromRunnable {
    sharedPrefs
        .edit()
        .remove(key)
        .apply()
  }
}