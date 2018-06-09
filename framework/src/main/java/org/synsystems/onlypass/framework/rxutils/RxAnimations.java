package org.synsystems.onlypass.framework.rxutils;

import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Utility for performing Android view animations reactively.
 */
public class RxAnimations {
  /**
   * Transitions a view's alpha value from its current value to the target value in a fixed amount of time.
   *
   * @param view
   *     the view to animate
   * @param endAlpha
   *     the target alpha value
   * @param durationMilliseconds
   *     the duration of the animation, measured in milliseconds
   *
   * @return a new completable that performs the animation
   */
  public static Completable transitionAlpha(
      @NonNull final View view,
      final int endAlpha,
      final int durationMilliseconds,
      final boolean skipIfUnnecessary) {

    final Single<Boolean> skipAnimation = Single
        .fromCallable(view::getAlpha)
        .map(currentAlpha -> currentAlpha == endAlpha && skipIfUnnecessary);

    final Completable animation = Completable.create(emitter -> view
        .animate()
        .alpha(endAlpha)
        .setDuration(durationMilliseconds)
        .withEndAction(emitter::onComplete)
        .start());

    return skipAnimation.flatMapCompletable(skip -> skip ? Completable.complete() : animation);
  }
}