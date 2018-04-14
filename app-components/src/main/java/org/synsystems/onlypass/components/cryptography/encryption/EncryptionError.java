package org.synsystems.onlypass.components.cryptography.encryption;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

public class EncryptionError extends RuntimeException {
  public EncryptionError() {
    super();
  }

  public EncryptionError(@Nullable final String message) {
    super(message);
  }

  public EncryptionError(@Nullable final String message, @Nullable final Throwable cause) {
    super(message, cause);
  }

  public EncryptionError(@Nullable final Throwable cause) {
    super(cause);
  }

  @RequiresApi(24)
  @TargetApi(24)
  protected EncryptionError(
      @Nullable final String message,
      @Nullable final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace) {

    super(message, cause, enableSuppression, writableStackTrace);
  }
}