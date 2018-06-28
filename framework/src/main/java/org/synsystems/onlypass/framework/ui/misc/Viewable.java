package org.synsystems.onlypass.framework.ui.misc;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Something that can be displayed as an Android view.
 */
public interface Viewable {
  /**
   * @return this as a {@link View}
   */
  @NonNull
  public View asView();
}