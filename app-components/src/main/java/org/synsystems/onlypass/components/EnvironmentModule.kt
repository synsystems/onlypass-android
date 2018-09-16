package org.synsystems.onlypass.components

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.Environment.DEV
import org.synsystems.onlypass.components.Environment.EXTERNAL_TESTING
import org.synsystems.onlypass.components.Environment.INTERNAL_TESTING
import org.synsystems.onlypass.components.Environment.PROD

@Module
class EnvironmentModule {

  @Provides
  @AppScope
  fun provideEnvironment(): Environment {
    return when (BuildConfig.BUILD_TYPE) {
      "dev" -> DEV
      "internalTesting" -> INTERNAL_TESTING
      "externalTesting" -> EXTERNAL_TESTING
      "prod" -> PROD
      else -> throw RuntimeException("Unhandled build type ${BuildConfig.BUILD_TYPE}.")
    }
  }
}