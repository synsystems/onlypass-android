package org.synsystems.onlypass.components.cryptography.encryption.aes

import org.junit.Before
import org.junit.Test
import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256Configuration
import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256CredentialHardener
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey
import org.synsystems.onlypass.components.cryptography.encryption.EncryptionError
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.crypto.NoSuchPaddingException

class TestAesProvider {

  private lateinit var aesProvider: AesProvider<DerivedKey>

  private lateinit var iv1: ByteArray

  private lateinit var iv2: ByteArray

  private lateinit var key1: DerivedKey

  private lateinit var key2: DerivedKey

  @Before
  @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
  fun setup() {
    aesProvider = AesProvider(DerivedKeyCredentialConverter())

    val credentialHardener = Pbkdf2WithHmacSha256CredentialHardener()
    val hardeningParameters = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 256, 1000)

    iv1 = ByteArray(128)
    iv2 = ByteArray(96)

    key1 = credentialHardener
        .hardenCredential(CleartextPassword("password"), hardeningParameters)
        .blockingGet()

    key2 = credentialHardener
        .hardenCredential(CleartextPassword("not password"), hardeningParameters)
        .blockingGet()
  }

  @Test
  fun testEncryptThenDecrypt_emptyCiphertext() {
    val originalCleartext = ByteArray(0)

    aesProvider
        .encrypt(originalCleartext, iv1, key1)
        .flatMap { ciphertext -> aesProvider.decrypt(ciphertext, iv1, key1) }
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue { decryptedCleartext -> decryptedCleartext.contentEquals(originalCleartext) }
        .assertComplete()
  }

  // TODO break into encrypt and decrypt
  @Test
  fun testEncryptThenDecrypt_emptyInitialisationVector() {
    val originalCleartext = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
    val iv = ByteArray(0)

    aesProvider
        .encrypt(originalCleartext, iv, key1)
        .flatMap { ciphertext -> aesProvider.decrypt(ciphertext, iv, key1) }
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(EncryptionError::class.java)
        .assertNoValues()
        .assertNotComplete()
  }

  @Test
  fun testEncryptThenDecrypt_initialisationVectorsDoNotMatch() {
    val cleartext = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)

    aesProvider
        .encrypt(cleartext, iv1, key1)
        .flatMap { ciphertext -> aesProvider.decrypt(ciphertext, iv2, key1) }
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(EncryptionError::class.java)
        .assertNoValues()
        .assertNotComplete()
  }

  @Test
  fun testEncryptThenDecrypt_credentialsDoNotMatch() {
    val originalCleartext = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)

    aesProvider
        .encrypt(originalCleartext, iv1, key1)
        .flatMap { ciphertext -> aesProvider.decrypt(ciphertext, iv1, key2) }
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(EncryptionError::class.java)
        .assertNoValues()
        .assertNotComplete()
  }

  @Test
  fun testEncryptThenDecrypt_initialisationVectorsAndCredentialsMatch() {
    val originalCleartext = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7)

    aesProvider
        .encrypt(originalCleartext, iv1, key1)
        .flatMap { ciphertext -> aesProvider.decrypt(ciphertext, iv1, key1) }
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue { decryptedCleartext -> decryptedCleartext.contentEquals(originalCleartext) }
        .assertComplete()
  }
}
