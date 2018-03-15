package org.synsystems.onlypass.components;

public enum Environment {
  // Local logging only. Stetho enabled.
  DEV(true, false, true),

  // Local and remote logging. Stetho enabled.
  INTERNAL_TESTING(true, true, true),

  // Remote logging only. Stetho disabled.
  EXTERNAL_TESTING(false, true, false),

  // All logging disabled. Stetho disabled.
  PROD(false, false, false);

  private boolean localLoggingEnabled;

  private boolean remoteLoggingEnabledByDefault;

  private boolean stethoEnabled;

  Environment(
      final boolean localLoggingEnabled,
      final boolean remoteLoggingEnabledByDefault,
      final boolean stethoEnabled) {

    this.localLoggingEnabled = localLoggingEnabled;
    this.remoteLoggingEnabledByDefault = remoteLoggingEnabledByDefault;
    this.stethoEnabled = stethoEnabled;
  }

  public boolean isLocalLoggingEnabled() {
    return localLoggingEnabled;
  }

  public boolean isRemoteLoggingEnabledByDefault() {
    return remoteLoggingEnabledByDefault;
  }

  public boolean isStethoEnabled() {
    return stethoEnabled;
  }
}