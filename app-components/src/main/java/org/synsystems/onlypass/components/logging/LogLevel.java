package org.synsystems.onlypass.components.logging;

import android.util.Log;

/**
 * The available logging levels.
 */
public enum LogLevel {
  /**
   * Traces and raw data.
   * <p>
   * Example:<br>
   * <i>Entered method MainActivity.onCreate(Bundle) with args [null] on thread 'Main'</i>.
   */
  VERBOSE(Log.VERBOSE),

  /**
   * Events and state changes with low to medium importance.
   * <p>
   * Example:<br>
   * <i>Network switched from WiFi to cellular.</i>
   */
  DEBUG(Log.DEBUG),

  /**
   * Events and state changes with high to extreme importance.
   * <p>
   * Example:<br>
   * <i>Attempting login with email address 'kjaneway@startfleet.ufp' and password 'Coffee'.</i>
   */
  INFO(Log.INFO),

  /**
   * Non-fatal errors caused by a defect in this application or a related system.
   * <p>
   * Example:<br>
   * <i>Unable to deserialise network response. The response is missing the following required fields: 'id', 'name'.</i>
   */
  WARNING(Log.WARN),

  /**
   * Fatal errors that are about to crash the thread, process or application.
   * <p>
   * Example:<br>
   * <i>Unhandled exception in MainPresenter. NullPointerException at line 1701.</i>
   */
  ERROR(Log.ERROR);

  private final int androidLogLevel;

  LogLevel(final int androidLogLevel) {
    this.androidLogLevel = androidLogLevel;
  }

  /**
   * @return the {@link Log} priority constant equivalent to this log level
   */
  public int getAndroidLogPriority() {
    return androidLogLevel;
  }
}