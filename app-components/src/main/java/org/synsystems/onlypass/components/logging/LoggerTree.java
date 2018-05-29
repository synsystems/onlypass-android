package org.synsystems.onlypass.components.logging;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import timber.log.Timber.DebugTree;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Timber tree that forwards logs to {@link Logger} for better formatting.
 */
public class LoggerTree extends DebugTree {
  @NonNull
  private final LogLevel minimumLevel;

  private LoggerTree(@NonNull final LogLevel minimumLevel) {
    this.minimumLevel = minimumLevel;
  }

  /**
   * Constructs a new LoggerTree that does not filter logging calls.
   *
   * @return the new LoggerTree
   */
  @NonNull
  public static LoggerTree withoutFiltering() {
    return new LoggerTree(LogLevel.DEBUG);
  }

  /**
   * Constructs a new LoggerTree that filters logging calls based on log level. All logging calls with a priority
   * below the minimum level are ignored.
   *
   * @param minimumLevel
   *     the minimum log level to accept
   *
   * @return the new LoggerTree
   */
  @NonNull
  public static LoggerTree withMinimumLevelFiltering(@NonNull final LogLevel minimumLevel) {
    checkNotNull(minimumLevel);

    return new LoggerTree(minimumLevel);
  }

  @Override
  protected void log(final int priority, final String tag, @NonNull final String message, final Throwable error) {
    if (priority < minimumLevel.getAndroidLogPriority()) {
      return;
    }

    Logger.log(priority, tag, message, error);
  }
}