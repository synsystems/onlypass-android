package org.synsystems.onlypass.framework.misc;

import android.support.annotation.NonNull;

import com.google.common.base.Supplier;

/**
 * Functional interface for a consumer.
 * <p>
 * This should have been in Guava alongside {@link Supplier}. Perhaps cpovirk was just being lazy
 * (<a href="http://github.com/google/guava/issues/2449">http://github.com/google/guava/issues/2449</a>).
 *
 * @param <T>
 *     the type of value accepted by the consumer
 */
public interface Consumer<T> {
  /**
   * Consumes the supplied value. Modification of the value is permitted.
   *
   * @param value
   *     the value to consume
   */
  public void accept(@NonNull final T value);
}