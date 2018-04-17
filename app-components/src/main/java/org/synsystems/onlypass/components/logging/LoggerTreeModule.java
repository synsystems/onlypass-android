package org.synsystems.onlypass.components.logging;

import org.synsystems.onlypass.components.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class LoggerTreeModule {
  @Provides
  @AppScope
  public LoggerTree provideLoggerTree() {
    return LoggerTree.withoutFiltering();
  }
}