package org.synsystems.onlypass.components

import com.crashlytics.android.Crashlytics

import dagger.Module
import dagger.Provides

@Module
class CrashlyticsModule {

  @Provides
  @AppScope
  fun provideCrashlytics(): Crashlytics = Crashlytics()
}