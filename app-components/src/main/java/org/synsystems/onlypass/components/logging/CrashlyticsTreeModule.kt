package org.synsystems.onlypass.components.logging

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.AppScope
import org.synsystems.onlypass.components.Environment

@Module
class CrashlyticsTreeModule {

  @Provides
  @AppScope
  fun provideCrashlyticsTree(environment: Environment) = CrashlyticsTree(LogLevel.WARNING)
}