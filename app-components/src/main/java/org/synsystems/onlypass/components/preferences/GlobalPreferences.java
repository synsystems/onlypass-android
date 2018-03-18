package org.synsystems.onlypass.components.preferences;

import org.synsystems.onlypass.framework.events.Event;

import io.reactivex.Observable;

public interface GlobalPreferences {
  public Observable<Event> observeRemoteLoggingEnabled();

  public Observable<Event> observeRemoteLoggingDisabled();
}