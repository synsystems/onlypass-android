package org.synsystems.onlypass.components.logging;

import org.synsystems.onlypass.components.AppScope;
import org.synsystems.onlypass.components.Environment;

import dagger.Module;
import dagger.Provides;

@Module
public class CrashlyticsTreeModule {
  @Provides
  @AppScope
  public CrashlyticsTree provideCrashlyticsTree(final Environment environment) {
    return CrashlyticsTree.withMinimumLevelFiltering(LogLevel.WARNING);
  }
}