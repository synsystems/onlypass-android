package org.synsystems.onlypass.components.logging;

import com.orhanobut.logger.Logger;

import timber.log.Timber.DebugTree;

/**
 * A Timber tree that forwards logs to {@link Logger} for better log formatting.
 */
public class LoggerTree extends DebugTree {
  @Override
  protected void log(final int priority, final String tag, final String message, final Throwable error) {
    Logger.log(priority, tag, message, error);
  }
}