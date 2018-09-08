package org.synsystems.onlypass.components

import dagger.Module
import dagger.Provides

@Module
class EnvironmentModule {

  @Provides
  @AppScope
  fun provideEnvironment() = Environment.PROD
}