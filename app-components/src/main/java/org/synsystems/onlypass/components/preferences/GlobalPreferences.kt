package org.synsystems.onlypass.components.preferences

import io.reactivex.Observable
import org.synsystems.onlypass.framework.rxutils.Pulse

interface GlobalPreferences {
  fun observeRemoteLoggingEnabled(): Observable<Pulse>

  fun observeRemoteLoggingDisabled(): Observable<Pulse>
}