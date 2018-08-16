package org.synsystems.onlypass.components.cryptography.random

import com.google.common.base.Preconditions.checkArgument
import io.reactivex.Single
import java.security.SecureRandom

/**
 * Generates secure random byte arrays. The generated arrays are random enough for use in cryptographic operations.
 */
class SecureByteArrayGenerator(private val secureRandom: SecureRandom) {
  /**
   * Generates a byte array with secure random contents. The contents are random enough for use in cryptographic
   * operations.
   *
   * @param lengthBytes
   * the length of the generated array
   *
   * @return a new single that emits the generated array
   */
  fun generateWithLength(lengthBytes: Int): Single<ByteArray> {
    checkArgument(lengthBytes >= 0)

    return Single.fromCallable {
      val salt = ByteArray(lengthBytes)

      secureRandom.nextBytes(salt)

      salt
    }
  }
}