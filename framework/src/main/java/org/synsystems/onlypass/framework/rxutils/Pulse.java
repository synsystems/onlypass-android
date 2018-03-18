package org.synsystems.onlypass.framework.rxutils;

public class Pulse {
  private static final Pulse INSTANCE = new Pulse();

  private Pulse() {}

  public static Pulse getInstance() {
    return INSTANCE;
  }
}