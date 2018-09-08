package org.synsystems.onlypass.components.preferences

import org.synsystems.onlypass.components.AppScope
import org.synsystems.onlypass.framework.rxutils.Pulse

import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class GlobalPreferencesModule {
  @Provides
  @AppScope
  fun provideGlobalPreferences(): GlobalPreferences {
    // TODO replace with actual implementation
    return object : GlobalPreferences {
      override fun observeRemoteLoggingEnabled(): Observable<Pulse> {
        return Observable.empty()
      }

      override fun observeRemoteLoggingDisabled(): Observable<Pulse> {
        return Observable.empty()
      }
    }
  }
}