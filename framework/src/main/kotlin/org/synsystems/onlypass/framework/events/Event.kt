package org.synsystems.onlypass.framework.events

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Persistent record of an event that can happen during app execution.
 */
interface Event {
  /**
   * Whether or not the event has occurred.
   */
  fun hasOccurred(): Single<Boolean>

  /**
   * Records the event as having occurred.
   */
  fun recordOccurrence(): Completable

  /**
   * Clears any record of the event. Safe to call if the event hasn't been recorded.
   */
  fun clearRecord(): Completable
}