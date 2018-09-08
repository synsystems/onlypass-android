package org.synsystems.onlypass.components.cryptography.encryption.aes

import io.reactivex.Single
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProvider.CredentialConverter
import javax.crypto.spec.SecretKeySpec

/**
 * A [CredentialConverter] for [DerivedKey]s.
 */
class DerivedKeyCredentialConverter : CredentialConverter<DerivedKey> {

  override fun toSecretKeySpec(credential: DerivedKey): Single<SecretKeySpec> {
    return Single.fromCallable { SecretKeySpec(credential.key.encoded, "AES") }
  }
}