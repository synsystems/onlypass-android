package org.synsystems.onlypass.components.logging;

import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber.DebugTree;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Timber tree that forwards logging calls to {@link Crashlytics}. The tree provides filtering based on log level.
 */
public class CrashlyticsTree extends DebugTree {
  @NonNull
  private final LogLevel minimumLevel;

  private CrashlyticsTree(@NonNull final LogLevel minimumLevel) {
    this.minimumLevel = minimumLevel;
  }

  /**
   * Constructs a new CrashlyticsTree that does not filter logging calls.
   *
   * @return the new CrashlyticsTree
   */
  @NonNull
  public static CrashlyticsTree withoutFiltering() {
    return new CrashlyticsTree(LogLevel.DEBUG);
  }

  /**
   * Constructs a new CrashlyticsTree that filters logging calls based on log level. All logging calls with a priority
   * below the minimum level are ignored.
   *
   * @param minimumLevel
   *     the minimum log level to accept
   *
   * @return the new CrashlyticsTree
   */
  @NonNull
  public static CrashlyticsTree withMinimumLevelFiltering(@NonNull final LogLevel minimumLevel) {
    checkNotNull(minimumLevel);

    return new CrashlyticsTree(minimumLevel);
  }

  @Override
  protected void log(final int priority, final String tag, @NonNull final String message, final Throwable error) {
    if (priority < minimumLevel.getAndroidLogPriority()) {
      return;
    }

    Crashlytics.log(priority, tag, message);

    if (error != null) {
      Crashlytics.logException(error);
    }
  }
}