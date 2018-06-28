package org.synsystems.onlypass.framework.ui.helpers;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import io.reactivex.Completable;

/**
 * Helper class for working with {@link Toolbar}s.
 */
public class ToolbarHelper {
  /**
   * Tints the overflow menu button in a toolbar.
   *
   * @param toolbar
   *     the toolbar containing the button to tint
   * @param colour
   *     the colour to use for the tint, as an ARGB hex code
   *
   * @return a new completable that tints the button
   */
  public static Completable setOverflowIconColour(@NonNull final Toolbar toolbar, final int colour) {
    return Completable.fromRunnable(() -> {
      if (toolbar.getOverflowIcon() == null) {
        return;
      }

      toolbar
          .getOverflowIcon()
          .setColorFilter(colour, PorterDuff.Mode.MULTIPLY);
    });
  }
}