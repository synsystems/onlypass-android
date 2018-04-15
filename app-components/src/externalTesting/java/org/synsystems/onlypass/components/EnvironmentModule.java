package org.synsystems.onlypass.components;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class EnvironmentModule {
  @Provides
  @AppScope
  public Environment provideEnvironment() {
    return Environment.EXTERNAL_TESTING;
  }
}