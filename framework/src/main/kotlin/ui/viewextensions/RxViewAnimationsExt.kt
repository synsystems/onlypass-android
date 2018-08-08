package org.synsystems.onlypass.framework.ui.viewextensions

import android.view.View
import io.reactivex.Completable
import org.joda.time.Period

/**
 * Transitions the alpha value of this view to [alpha] in [duration]. The animation will complete immediately if the
 * alpha value does not need to change.
 */
fun View.transitionAlpha(alpha: Float, duration: Period) = Completable.defer {
  val animation = Completable.create { emitter ->
    animate()
        .alpha(alpha)
        .setDuration(duration.millis.toLong())
        .withEndAction { emitter.onComplete() }
        .start()
  }

  if (this.alpha == alpha) Completable.complete() else animation
}