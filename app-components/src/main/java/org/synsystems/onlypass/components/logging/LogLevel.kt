package org.synsystems.onlypass.components.logging

import android.util.Log

/**
 * The available logging levels.
 */
enum class LogLevel private constructor(
    /**
     * @return the [Log] priority constant equivalent to this log level
     */
    val androidLogPriority: Int
) {

  /**
   * Traces and raw data.
   *
   * Example: Entered method MainActivity.onCreate(Bundle) with args '(null)' on thread 'Main'.
   */
  VERBOSE(Log.VERBOSE),

  /**
   * Events and state changes with low to medium importance.
   *
   * Example: Network switched from WiFi to cellular.
   */
  DEBUG(Log.DEBUG),

  /**
   * Events and state changes with high to extreme importance.
   *
   * Example: Attempting login with email address 'kjaneway@startfleet.ufp' and password 'Coffee'.
   */
  INFO(Log.INFO),

  /**
   * Non-fatal errors caused by a defect in this application or a related system.
   *
   * Example: Unable to deserialise network response. The response is missing the following required fields: 'id',
   * 'name'.
   */
  WARNING(Log.WARN),

  /**
   * Fatal errors that are about to crash the thread, process or application.
   *
   * Example: Unhandled exception in MainPresenter. NullPointerException at line 1701.
   */
  ERROR(Log.ERROR)
}