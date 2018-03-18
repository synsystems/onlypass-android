package org.synsystems.onlypass.components;

import com.crashlytics.android.Crashlytics;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class AppModule {
  private static final Map<String, Environment> ENVIRONMENT_MAP = ImmutableMap
      .<String, Environment>builder()
      .put("dev", Environment.DEV)
      .put("internalTesting", Environment.INTERNAL_TESTING)
      .put("externalTesting", Environment.EXTERNAL_TESTING)
      .put("prod", Environment.PROD)
      .build();

  @Provides
  @AppScope
  public Environment provideEnvironment() {
    if (!ENVIRONMENT_MAP.containsKey(BuildConfig.BUILD_TYPE)) {
      throw new RuntimeException("Cannot provide environment. Unexpected build type found: " + BuildConfig.BUILD_TYPE);
    }

    return ENVIRONMENT_MAP.get(BuildConfig.BUILD_TYPE);
  }

  @Provides
  @AppScope
  public Crashlytics provideCrashlytics() {
    return new Crashlytics();
  }
}