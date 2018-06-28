package org.synsystems.onlypass.framework.ui.misc;

import com.google.common.base.Optional;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Something capable of consuming a back press by exposing pending back actions.
 */
public interface BackActionSource {
  /**
   * Creates an observable that emits the pending back actions as optional completables. An empty optional indicates
   * the lack of a pending back action. Emitted back actions should not be executed if they have been superseded by
   * another emission.
   *
   * @return a new observable that emits the pending back actions
   */
  public Observable<Optional<Completable>> observePendingBackActions();
}