package org.synsystems.onlypass.components

/**
 * The environment that the app is operating in. The environment determines the level of debugging.
 */
enum class Environment constructor(
    /**
     * @return true if local logging is enabled in this environment, false otherwise
     */
    val isLocalLoggingEnabled: Boolean,

    /**
     * @return true if remote logging and crash reporting are enabled by default in this environment, false otherwise
     */
    val isRemoteLoggingEnabledByDefault: Boolean,

    /**
     * @return true if stetho is enabled in this environment, false otherwise
     */
    val isStethoEnabled: Boolean
) {

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
  PROD(false, false, false)
}