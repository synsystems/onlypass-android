package org.synsystems.onlypass.components.logging;

import android.util.Log;

import org.synsystems.onlypass.components.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class LoggingModule {
  @Provides
  @AppScope
  public CrashlyticsTree provideCrashlysicsTree() {
    return CrashlyticsTree.withMinimumPriority(Log.INFO);
  }

  @Provides
  @AppScope
  public LoggerTree provideLoggerTree() {
    return new LoggerTree();
  }
}