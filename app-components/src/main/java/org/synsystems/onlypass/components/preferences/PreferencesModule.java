package org.synsystems.onlypass.components.preferences;

import org.synsystems.onlypass.components.AppScope;
import org.synsystems.onlypass.framework.events.Event;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;

@Module
@AppScope
public class PreferencesModule {
  @Provides
  @AppScope
  public GlobalPreferences provideGlobalPreferences() {
    // TODO replace with actual implementation
    return new GlobalPreferences() {
      @Override
      public Observable<Event> observeRemoteLoggingEnabled() {
        return Observable.never();
      }

      @Override
      public Observable<Event> observeRemoteLoggingDisabled() {
        return Observable.never();
      }
    };
  }
}