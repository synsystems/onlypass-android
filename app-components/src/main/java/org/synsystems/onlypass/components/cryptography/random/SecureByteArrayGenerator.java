package org.synsystems.onlypass.components.cryptography.random;

import android.support.annotation.NonNull;

import java.security.SecureRandom;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generates secure random byte arrays. The generated arrays are secure enough for use in cryptographic operations.
 */
public class SecureByteArrayGenerator {
  @NonNull
  private final SecureRandom secureRandom;

  /**
   * Constructs a new SecureByteArrayGenerator.
   *
   * @param secureRandom
   *     the random source for use in generation
   */
  public SecureByteArrayGenerator(@NonNull final SecureRandom secureRandom) {
    this.secureRandom = checkNotNull(secureRandom);
  }

  /**
   * Generates a byte array with random contents. The contents are sufficiently random such that they can be used in
   * cryptographic operations.
   *
   * @param lengthBytes
   *     the length of the generated array
   *
   * @return a new single that emits the generated array
   */
  @NonNull
  public Single<byte[]> generateWithLength(final int lengthBytes) {
    checkArgument(lengthBytes >= 0);

    return Single.fromCallable(() -> {
      final byte[] salt = new byte[lengthBytes];

      secureRandom.nextBytes(salt);

      return salt;
    });
  }
}