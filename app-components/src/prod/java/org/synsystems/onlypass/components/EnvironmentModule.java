package org.synsystems.onlypass.components;

import dagger.Module;
import dagger.Provides;

@Module
public class EnvironmentModule {
  @Provides
  @AppScope
  public Environment provideEnvironment() {
    return Environment.PROD;
  }
}