package org.synsystems.onlypass.components

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Dagger scope for the entire app. This is the highest level scope, and anything within this scope should be
 * considered a singleton.
 */
@Scope
@Retention(RUNTIME)
annotation class AppScope