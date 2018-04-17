package org.synsystems.onlypass.components;

import com.crashlytics.android.Crashlytics;

import dagger.Module;
import dagger.Provides;

@Module
public class CrashlyticsModule {
  @Provides
  @AppScope
  public Crashlytics provideCrashlytics() {
    return new Crashlytics();
  }
}