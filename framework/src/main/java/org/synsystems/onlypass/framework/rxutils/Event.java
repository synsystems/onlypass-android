package org.synsystems.onlypass.framework.rxutils;

public class Event {
  private static final Event INSTANCE = new Event();

  private Event() {}

  public static Event getInstance() {
    return INSTANCE;
  }
}