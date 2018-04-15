package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.junit.Before;
import org.junit.Test;
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword;

import java.security.NoSuchAlgorithmException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ConstantConditions")
public class TestPbkdf2WithHmacSha256CredentialHardener {
  private Pbkdf2WithHmacSha256CredentialHardener credentialHardener;

  @Before
  public void setup() throws NoSuchAlgorithmException {
    credentialHardener = new Pbkdf2WithHmacSha256CredentialHardener();
  }

  @Test(expected = RuntimeException.class)
  public void testDeriveSecureCredential_nullInsecureCredential() {
    credentialHardener.hardenCredential(null, mock(Pbkdf2WithHmacSha256Configuration.class));
  }

  @Test(expected = RuntimeException.class)
  public void testDeriveSecureCredential_nullHashingSalt() {
    credentialHardener.hardenCredential(mock(CleartextPassword.class), null);
  }

  @Test
  public void testDeriveSecureCredential_allValidValues() {
    final Pbkdf2WithHmacSha256Configuration parameters = Pbkdf2WithHmacSha256Configuration
        .builder()
        .setSalt(new byte[]{1})
        .setDerivedKeyBitlength(256)
        .setIterationCount(1000)
        .build();

    credentialHardener
        .hardenCredential(CleartextPassword.create("password"), parameters)
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(value -> value != null)
        .assertComplete();
  }
}