package org.synsystems.onlypass.components.cryptography.credentialhardening;

import io.reactivex.Single
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * A [CredentialHardener] that converts [CleartextPassword]s to [DerivedKey]s with the PBKDF2WithHmacSHA256 algorithm.
 */
class Pbkdf2WithHmacSha256CredentialHardener :
    CredentialHardener<CleartextPassword, DerivedKey, Pbkdf2WithHmacSha256Configuration> {

  private val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")

  override fun hardenCredential(
      insecureCredential: CleartextPassword,
      hardeningParameters: Pbkdf2WithHmacSha256Configuration
  ): Single<DerivedKey> {

    return Single
        .fromCallable(insecureCredential::password)
        .map(String::toCharArray)
        .map { passwordChars ->
          PBEKeySpec(
              passwordChars,
              hardeningParameters.salt.toByteArray(),
              hardeningParameters.iterationCount,
              hardeningParameters.derivedKeyBitlength)
        }
        .map(secretKeyFactory::generateSecret)
        .map(::DerivedKey)
  }
}