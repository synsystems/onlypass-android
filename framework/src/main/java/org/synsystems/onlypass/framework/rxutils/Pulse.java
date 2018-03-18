package org.synsystems.onlypass.framework.rxutils;

import io.reactivex.Observable;

/**
 * A pulse is an event without data. The class is intended to be emitted by {@link Observable} instances to signal
 * the occurrence of events that carry no additional data. This class is implemented as a singleton for performance.
 */
public class Pulse {
  private static final Pulse INSTANCE = new Pulse();

  private Pulse() {}

  /**
   * @return the singleton instance
   */
  public static Pulse getInstance() {
    return INSTANCE;
  }
}