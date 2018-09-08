package org.synsystems.onlypass.components.logging

import com.orhanobut.logger.Logger
import timber.log.Timber.DebugTree

/**
 * A Timber tree that forwards logs to [Logger] for better formatting.
 */
class LoggerTree(private val minimumLevel: LogLevel = LogLevel.VERBOSE) : DebugTree() {

  override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
    if (priority < minimumLevel.androidLogPriority) {
      return
    }

    Logger.log(priority, tag, message, error)
  }
}