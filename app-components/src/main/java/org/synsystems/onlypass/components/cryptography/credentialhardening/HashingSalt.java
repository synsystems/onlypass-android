package org.synsystems.onlypass.components.cryptography.credentialhardening;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

/**
 * A hashing salt for use in credential hardening.
 */
@AutoValue
public abstract class HashingSalt implements HardeningParameters {
  @SuppressWarnings("mutable")
  @NonNull
  public abstract byte[] getSalt();

  /**
   * Constructs a new HashingSalt.
   *
   * @param salt
   *     the salt
   *
   * @return the new HashingSalt
   */
  @NonNull
  public static HashingSalt create(@NonNull final byte[] salt) {
    return new AutoValue_HashingSalt(salt);
  }
}