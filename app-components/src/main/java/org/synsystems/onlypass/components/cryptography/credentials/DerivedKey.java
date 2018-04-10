package org.synsystems.onlypass.components.cryptography.credentials;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

import javax.crypto.SecretKey;

/**
 * A key that has been derived from a password in such a way that the original password cannot feasibly be recovered
 * from the key.
 */
@AutoValue
public abstract class DerivedKey implements SecureCredential {
  /**
   * @return the key
   */
  @NonNull
  public abstract SecretKey getKey();

  /**
   * Constructs a new DerivedKey.
   *
   * @param key
   *     the key
   *
   * @return the new DerivedKey
   */
  @NonNull
  public static DerivedKey create(@NonNull final SecretKey key) {
    return new AutoValue_DerivedKey(key);
  }
}