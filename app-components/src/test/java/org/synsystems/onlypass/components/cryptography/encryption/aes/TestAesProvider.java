package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.junit.Before;
import org.junit.Test;
import org.synsystems.onlypass.components.cryptography.credentialhardening.CredentialHardener;
import org.synsystems.onlypass.components.cryptography.credentialhardening.HashingSalt;
import org.synsystems.onlypass.components.cryptography.credentialhardening.PasswordBasedKeyDerivationConfiguration;
import org.synsystems.onlypass.components.cryptography.credentialhardening.PasswordBasedKeyDerivationCredentialHardener;
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword;
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey;
import org.synsystems.onlypass.components.cryptography.encryption.EncryptionError;
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProvider.CredentialConverter;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class TestAesProvider {
  private AesProvider<DerivedKey> aesProvider;

  private DerivedKey key1;

  private DerivedKey key2;

  @Before
  public void setup() throws NoSuchPaddingException, NoSuchAlgorithmException {
    final AesConfiguration aesConfiguration = AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(128)
        .build();

    final CredentialConverter<DerivedKey> credentialConverter = new DerivedKeyCredentialConverter();

    aesProvider = new AesProvider<>(aesConfiguration, credentialConverter);

    final CredentialHardener<CleartextPassword, DerivedKey, HashingSalt> credentialHardener = new
        PasswordBasedKeyDerivationCredentialHardener(
        PasswordBasedKeyDerivationConfiguration
            .builder()
            .setDerivedKeyBitlength(256)
            .setIterationCount(100)
            .build());

    key1 = credentialHardener
        .hardenCredential(CleartextPassword.create("password"), HashingSalt.create(new byte[]{0, 1, 2, 3, 4}))
        .blockingGet();

    key2 = credentialHardener
        .hardenCredential(CleartextPassword.create("not password"), HashingSalt.create(new byte[]{0, 1, 2, 3, 4}))
        .blockingGet();
  }

  @Test(expected = RuntimeException.class)
  public void testConstructor_nullAesConfiguration() throws NoSuchPaddingException, NoSuchAlgorithmException {
    new AesProvider(null, mock(CredentialConverter.class));
  }

  @Test(expected = RuntimeException.class)
  public void testConstructor_nullCredentialConverter() throws NoSuchPaddingException, NoSuchAlgorithmException {
    new AesProvider(mock(AesConfiguration.class), null);
  }

  @Test(expected = RuntimeException.class)
  public void testEncrypt_nullCleartext() {
    aesProvider.encrypt(null, new byte[0], mock(DerivedKey.class));
  }

  @Test(expected = RuntimeException.class)
  public void testEncrypt_nullInitialisationVector() {
    aesProvider.encrypt(new byte[0], null, mock(DerivedKey.class));
  }

  @Test(expected = RuntimeException.class)
  public void testEncrypt_nullCredential() {
    aesProvider.encrypt(new byte[0], new byte[0], null);
  }

  @Test(expected = RuntimeException.class)
  public void testDecrypt_nullCiphertext() {
    aesProvider.decrypt(null, new byte[0], mock(DerivedKey.class));
  }

  @Test(expected = RuntimeException.class)
  public void testDecrypt_nullInitialisationVector() {
    aesProvider.decrypt(new byte[0], null, mock(DerivedKey.class));
  }

  @Test(expected = RuntimeException.class)
  public void testDecrypt_nullCredential() {
    aesProvider.decrypt(new byte[0], new byte[0], null);
  }

  @Test
  public void testEncryptThenDecrypt_emptyCiphertext() {
    final byte[] cleartext = new byte[0];
    final byte[] iv = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};

    aesProvider
        .encrypt(cleartext, iv, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv, key1))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(decryptedCleartext -> Arrays.equals(decryptedCleartext, cleartext))
        .assertComplete();
  }

  @Test
  public void testEncryptThenDecrypt_emptyInitialisationVector() {
    final byte[] cleartext = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
    final byte[] iv = new byte[0];

    aesProvider
        .encrypt(cleartext, iv, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv, key1))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(decryptedData -> Arrays.equals(decryptedData, cleartext))
        .assertComplete();
  }

  @Test
  public void testEncryptThenDecrypt_initialisationVectorsDoNotMatch() {
    final byte[] cleartext = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
    final byte[] iv1 = new byte[]{8, 9, 10, 11, 12, 13, 14, 15};
    final byte[] iv2 = new byte[]{16, 17, 18, 19, 20, 21, 22, 23};

    aesProvider
        .encrypt(cleartext, iv1, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv2, key1))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(EncryptionError.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testEncryptThenDecrypt_credentialsDoNotMatch() {
    final byte[] cleartext = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
    final byte[] iv = new byte[]{8, 9, 10, 11, 12, 13, 14, 15};

    aesProvider
        .encrypt(cleartext, iv, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv, key2))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(EncryptionError.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testEncryptThenDecrypt_initialisationVectorAndCredentialsMatch() {
    final byte[] cleartext = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
    final byte[] iv = new byte[]{8, 9, 10, 11, 12, 13, 14, 15};

    aesProvider
        .encrypt(cleartext, iv, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv, key1))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(decryptedData -> Arrays.equals(decryptedData, cleartext))
        .assertComplete();
  }
}
