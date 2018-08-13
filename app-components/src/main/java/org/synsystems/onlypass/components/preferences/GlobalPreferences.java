package org.synsystems.onlypass.components.preferences;

import org.synsystems.onlypass.framework.rxutils.Pulse;

import io.reactivex.Observable;

public interface GlobalPreferences {
  public Observable<Pulse> observeRemoteLoggingEnabled();

  public Observable<Pulse> observeRemoteLoggingDisabled();
}