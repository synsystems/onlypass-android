package org.synsystems.onlypass.components.cryptography.encryption.aes

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.synsystems.onlypass.components.cryptography.credentials.Credential
import org.synsystems.onlypass.components.cryptography.encryption.EncryptionError
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Performs AES encryption and decryption operations.
 *
 * @param C the type of credential used to encrypt and decrypt data
 */
class AesProvider<C : Credential> constructor(private val credentialConverter: CredentialConverter<C>) {

  // A hard dependency is ok since we need a specific instance for AES
  private val cipher = Cipher.getInstance("AES/GCM/PKCS5Padding")

  /**
   * Encrypts [cleartext] with AES, using [initialisationVector] and [credential] for configuration.
   *
   * @return the ciphertext
   */
  fun encrypt(
      cleartext: ByteArray,
      initialisationVector: ByteArray,
      credential: C
  ): Single<ByteArray> {

    val prepareForEncryption = Single
        .zip(
            credentialConverter.toSecretKeySpec(credential),
            Single.fromCallable { IvParameterSpec(initialisationVector) },
            BiFunction<SecretKeySpec, IvParameterSpec, Completable> { key, iv -> prepareCipherForEncryption(key, iv) })
        .flatMapCompletable { wrappedCompletable -> wrappedCompletable }

    return prepareForEncryption
        .andThen(performEncryption(cleartext))
        .onErrorResumeNext { error -> Single.error(EncryptionError("Encryption failed.", error)) }
  }

  /**
   * Decrypts [ciphertext] with AES, using [initialisationVector] and [credential] for configuration The operation
   * will fail if the configuration does not match the one used during encryption.
   *
   * @return the cleartext
   */
  fun decrypt(
      ciphertext: ByteArray,
      initialisationVector: ByteArray,
      credential: C
  ): Single<ByteArray> {

    val prepareForDecryption = Single
        .zip(
            credentialConverter.toSecretKeySpec(credential),
            Single.fromCallable { IvParameterSpec(initialisationVector) },
            BiFunction<SecretKeySpec, IvParameterSpec, Completable> { key, iv -> prepareCipherForDecryption(key, iv) })
        .flatMapCompletable { wrappedCompletable -> wrappedCompletable }

    return prepareForDecryption
        .andThen(performDecryption(ciphertext))
        .onErrorResumeNext { error -> Single.error(EncryptionError("Decryption failed.", error)) }
  }

  private fun prepareCipherForDecryption(
      key: SecretKey,
      initialisationVector: IvParameterSpec
  ): Completable {

    return Completable.fromRunnable {
      GCMParameterSpec(initialisationVector.iv.size, initialisationVector.iv).let {
        cipher.init(Cipher.DECRYPT_MODE, key, it)
      }
    }
  }

  private fun prepareCipherForEncryption(
      key: SecretKey,
      initialisationVector: IvParameterSpec
  ) : Completable {

    return Completable.fromRunnable {
      GCMParameterSpec(initialisationVector.iv.size, initialisationVector.iv).let {
        cipher.init(Cipher.ENCRYPT_MODE, key, it)
      }
    }
  }

  private fun performEncryption(cleartext: ByteArray) = Single.fromCallable { cipher.doFinal(cleartext) }

  private fun performDecryption(ciphertext: ByteArray) = Single.fromCallable { cipher.doFinal(ciphertext) }

  /**
   * Converts [Credential]s to [SecretKeySpec]s.
   *
   * @param C the type of credential that can be converted
   */
  interface CredentialConverter<C : Credential> {
    /**
     * Converts [credential] to a secret key spec configured for AES. Calls must be idempotent.
     */
    fun toSecretKeySpec(credential: C): Single<SecretKeySpec>
  }
}