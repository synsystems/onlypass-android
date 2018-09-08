package org.synsystems.onlypass.components.cryptography.credentialhardening;

/**
 * Configuration parameters for PBKDF2WithHmacSHA256.
 */
data class Pbkdf2WithHmacSha256Configuration(
    val salt: Array<Byte>,
    val iterationCount: Int,
    val derivedKeyBitlength: Int
) : HardeningParameters {

  init {
    if (salt.isEmpty()) {
      throw IllegalArgumentException("Salt cannot be empty.")
    }

    if (iterationCount < 1) {
      throw IllegalArgumentException("Iteration count must be at least 1.")
    }

    if (derivedKeyBitlength < 8) {
      throw IllegalStateException("Derived key bitlength must be at least 8.")
    }

    if (derivedKeyBitlength.rem(8) != 0) {
      throw IllegalStateException("Derived key bitlength must be a multiple of 8.")
    }
  }
}