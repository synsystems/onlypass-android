package org.synsystems.onlypass.framework.ui.betterlisteners;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

import org.synsystems.onlypass.framework.misc.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A TextWatcher that doesn't force the consumer to implement three methods. This allows the use of lambda
 * expressions for brevity.
 */
public class TextChangedListener implements TextWatcher {
  private final Consumer<String> updateConsumer;

  /**
   * Constructs a new TextChangedListener.
   *
   * @param updateConsumer
   *     the consumer to call when the text is changed
   */
  public TextChangedListener(@NonNull final Consumer<String> updateConsumer) {
    this.updateConsumer = checkNotNull(updateConsumer);
  }

  @Override
  public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
    updateConsumer.accept(s.toString());
  }

  @Override
  public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
    // Unnecessary but forced to implement because someone at Google doesn't understand basic OOP
  }

  @Override
  public void afterTextChanged(final Editable s) {
    // Unnecessary but forced to implement because someone at Google doesn't understand basic OOP
  }
}