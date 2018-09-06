package org.synsystems.onlypass.components.cryptography.credentialhardening

import org.junit.Before
import org.junit.Test
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit.MILLISECONDS

class TestPbkdf2WithHmacSha256CredentialHardener {
  private lateinit var credentialHardener: Pbkdf2WithHmacSha256CredentialHardener

  @Before
  @Throws(NoSuchAlgorithmException::class)
  fun setup() {
    credentialHardener = Pbkdf2WithHmacSha256CredentialHardener()
  }

  @Test
  fun testDeriveSecureCredential_allValidValues() {
    val parameters = Pbkdf2WithHmacSha256Configuration(arrayOf(1), 256, 1000)
    val originalPassword = CleartextPassword("password")

    credentialHardener
        .hardenCredential(originalPassword, parameters)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertComplete()
  }
}