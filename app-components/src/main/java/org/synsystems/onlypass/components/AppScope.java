package org.synsystems.onlypass.components;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Dagger scope for the entire app. This is the highest level scope, and anything within this scope should be
 * considered a singleton.
 */
@Scope
@Retention(RUNTIME)
public @interface AppScope {}