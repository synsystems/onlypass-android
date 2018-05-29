package org.synsystems.onlypass.framework.misc;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A key value pair.
 *
 * @param <K>
 *     the type of the key
 * @param <V>
 *     the type of the value
 */
@AutoValue
public abstract class Pair<K, V> {
  /**
   * @return the key
   */
  @NonNull
  public abstract K getKey();

  /**
   * @return the value
   */
  @NonNull
  public abstract V getValue();

  /**
   * @return a new single that emits the key
   */
  @NonNull
  public Single<K> getKeyRx() {
    return Single.fromCallable(this::getKey);
  }

  /**
   * @return a new single that emits the value
   */
  @NonNull
  public Single<V> getValueRx() {
    return Single.fromCallable(this::getValue);
  }

  /**
   * Constructs a new Pair.
   *
   * @param key
   *     the key
   * @param value
   *     the value
   * @param <K>
   *     the type of the key
   * @param <V>
   *     the type of the value
   *
   * @return the new Pair
   */
  @NonNull
  public static <K, V> Pair<K, V> create(@NonNull final K key, @NonNull final V value) {
    checkNotNull(key);
    checkNotNull(value);

    return new AutoValue_Pair<>(key, value);
  }
}