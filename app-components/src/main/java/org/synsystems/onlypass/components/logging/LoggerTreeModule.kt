package org.synsystems.onlypass.components.logging

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.AppScope

@Module
class LoggerTreeModule {

  @Provides
  @AppScope
  fun provideLoggerTree() = LoggerTree()
}