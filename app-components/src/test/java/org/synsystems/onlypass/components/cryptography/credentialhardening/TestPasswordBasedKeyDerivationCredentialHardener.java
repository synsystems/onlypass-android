package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.junit.Before;
import org.junit.Test;
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword;

import java.security.NoSuchAlgorithmException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ConstantConditions")
public class TestPasswordBasedKeyDerivationCredentialHardener {
  private PasswordBasedKeyDerivationCredentialHardener credentialHardener;

  @Before
  public void setup() throws NoSuchAlgorithmException {
    credentialHardener = new PasswordBasedKeyDerivationCredentialHardener();
  }

  @Test(expected = RuntimeException.class)
  public void testDeriveSecureCredential_nullInsecureCredential() {
    credentialHardener.hardenCredential(null, mock(PasswordBasedKeyDerivationParameters.class));
  }

  @Test(expected = RuntimeException.class)
  public void testDeriveSecureCredential_nullHashingSalt() {
    credentialHardener.hardenCredential(mock(CleartextPassword.class), null);
  }

  @Test
  public void testDeriveSecureCredential_allValidValues() {
    final PasswordBasedKeyDerivationParameters parameters = PasswordBasedKeyDerivationParameters
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