package org.synsystems.onlypass.components.logging

import com.crashlytics.android.Crashlytics
import timber.log.Timber.DebugTree

/**
 * A Timber tree that forwards logging calls to [Crashlytics]. The tree provides filtering based on log level.
 */
class CrashlyticsTree(private val minimumLevel: LogLevel = LogLevel.VERBOSE) : DebugTree() {

  override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
    if (priority < minimumLevel.androidLogPriority) {
      return
    }

    Crashlytics.log(priority, tag, message)

    if (error != null) {
      Crashlytics.logException(error)
    }
  }
}