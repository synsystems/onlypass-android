package org.synsystems.onlypass.components.logging;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import timber.log.Timber.DebugTree;

/**
 * A Timber tree that forwards logs to Crashlytics. The tree allows filtering based on log priority.
 */
public class CrashlyticsTree extends DebugTree {
  private final int minimumPriority;

  private CrashlyticsTree(final int minimumPriority) {
    this.minimumPriority = minimumPriority;
  }

  /**
   * Constructs a new CrashlyticsTree that filters no logging calls.
   *
   * @return the new CrashlyticsTree, not null
   */
  @NonNull
  public static CrashlyticsTree withoutMinimumPriority() {
    return new CrashlyticsTree(Integer.MIN_VALUE);
  }

  /**
   * Constructs a new CrashlyticsTree that filters out logging calls based on the supplied minimum priority. See
   * {@link Log} for priority constants.
   *
   * @param minimumPriority
   *     the minimum priority to log
   *
   * @return the new CrashlyticsTree, not null
   */
  @NonNull
  public static CrashlyticsTree withMinimumPriority(final int minimumPriority) {
    return new CrashlyticsTree(minimumPriority);
  }

  @Override
  protected void log(final int priority, final String tag, final String message, final Throwable error) {
    if (priority < minimumPriority) {
      return;
    }

    Crashlytics.log(priority, tag, message);

    if (error != null) {
      Crashlytics.logException(error);
    }
  }
}