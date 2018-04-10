package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.junit.Before;
import org.junit.Test;
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword;

import java.security.NoSuchAlgorithmException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.mock;

@SuppressWarnings("ConstantConditions")
public class TestPasswordBasedKeyDerivationCredentialHardener {
  private static final PasswordBasedKeyDerivationConfiguration CONFIGURATION = PasswordBasedKeyDerivationConfiguration
      .builder()
      .setDerivedKeyBitlength(256)
      .setIterationCount(1000)
      .build();

  private PasswordBasedKeyDerivationCredentialHardener credentialHardener;

  @Before
  public void setup() throws NoSuchAlgorithmException {
    credentialHardener = new PasswordBasedKeyDerivationCredentialHardener(CONFIGURATION);
  }

  @Test(expected = RuntimeException.class)
  public void testConstructor_nullConfiguration() throws NoSuchAlgorithmException {
    new PasswordBasedKeyDerivationCredentialHardener(null);
  }

  @Test(expected = RuntimeException.class)
  public void testDeriveSecureCredential_nullInsecureCredential() {
    credentialHardener.hardenCredential(null, mock(HashingSalt.class));
  }

  @Test(expected = RuntimeException.class)
  public void testDeriveSecureCredential_nullHashingSalt() {
    credentialHardener.hardenCredential(mock(CleartextPassword.class), null);
  }

  @Test
  public void testDeriveSecureCredential_allValidValues() {
    credentialHardener
        .hardenCredential(CleartextPassword.create("password"), HashingSalt.create(new byte[]{0, 1, 2, 3}))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(value -> value != null)
        .assertComplete();
  }
}