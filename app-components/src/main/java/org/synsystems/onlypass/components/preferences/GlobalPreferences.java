package org.synsystems.onlypass.components.preferences;

import io.reactivex.Observable;

public interface GlobalPreferences {
  public Observable<Pulse> observeRemoteLoggingEnabled();

  public Observable<Pulse> observeRemoteLoggingDisabled();
}