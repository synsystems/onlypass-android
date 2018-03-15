package org.synsystems.onlypass.components;

public enum Environment {
  /**
   * Environment for active development. Local logging and stetho are enabled, but remote logging is disabled by
   * default.
   */
  DEV(true, false, true),

  /**
   * Environment for testing before release to beta. Local logging and stetho are enabled, and remote logging is
   * enabled by default.
   */
  INTERNAL_TESTING(true, true, true),

  /**
   * Environment for beta testing. Remote logging is enabled by default, but local logging and stetho are disabled.
   */
  EXTERNAL_TESTING(false, true, false),

  /**
   * Environment for release to the general public. Local logging and stetho are disabled, and remote logging is
   * disabled by default.
   */
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