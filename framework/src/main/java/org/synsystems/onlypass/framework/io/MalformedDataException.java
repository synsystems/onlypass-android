package org.synsystems.onlypass.framework.io;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;

/**
 * Exception to indicate that data is incorrectly formatted/structured.
 */
public class MalformedDataException extends RuntimeException {
  /**
   * Constructs a MalformedDataException with no message and no cause.
   */
  public MalformedDataException() {
    super();
  }

  /**
   * Constructs a new MalformedDataException with a message but no cause.
   *
   * @param message
   *     a message describing the exception
   */
  public MalformedDataException(final String message) {
    super(message);
  }

  /**
   * Constructs a new MalformedDataException with a message and a cause.
   *
   * @param message
   *     a message describing the exception
   * @param cause
   *     the cause of the error
   */
  public MalformedDataException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new MalformedDataException with a cause but no message.
   *
   * @param cause
   *     the cause of the error
   */
  public MalformedDataException(final Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new MalformedDataException.
   *
   * @param message
   *     a message describing the exception
   * @param cause
   *     the cause of the error
   * @param enableSuppression
   *     whether or not suppression is enabled
   * @param writableStackTrace
   *     whether or not the stacktrace should be writable
   */
  @RequiresApi(24)
  @TargetApi(24)
  protected MalformedDataException(
      final String message,
      final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace) {

    super(message, cause, enableSuppression, writableStackTrace);
  }
}