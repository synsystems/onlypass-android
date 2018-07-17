package org.synsystems.onlypass.components.preferences;

import org.synsystems.onlypass.components.AppScope;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
public class GlobalPreferencesModule {
  @Provides
  @AppScope
  public GlobalPreferences provideGlobalPreferences() {
    // TODO replace with actual implementation
    return new GlobalPreferences() {
      @Override
      public Observable<Pulse> observeRemoteLoggingEnabled() {
        return Observable.empty();
      }

      @Override
      public Observable<Pulse> observeRemoteLoggingDisabled() {
        return Observable.empty();
      }
    };
  }
}