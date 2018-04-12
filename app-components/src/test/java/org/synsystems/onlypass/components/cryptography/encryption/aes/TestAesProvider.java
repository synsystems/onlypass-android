package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.junit.Before;
import org.junit.Test;
import org.synsystems.onlypass.components.cryptography.credentialhardening.CredentialHardener;
import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256CredentialHardener;
import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256Parameters;
import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword;
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey;
import org.synsystems.onlypass.components.cryptography.encryption.EncryptionError;
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProvider.CredentialConverter;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.NoSuchPaddingException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class TestAesProvider {
  private AesProvider<DerivedKey> aesProvider;

  private byte[] iv1;

  private byte[] iv2;

  private DerivedKey key1;

  private DerivedKey key2;

  @Before
  public void setup() throws NoSuchPaddingException, NoSuchAlgorithmException {
    final CredentialConverter<DerivedKey> credentialConverter = new DerivedKeyCredentialConverter();

    aesProvider = new AesProvider<>(credentialConverter);

    iv1 = new byte[128];
    iv2 = new byte[96];

    final CredentialHardener<CleartextPassword, DerivedKey, Pbkdf2WithHmacSha256Parameters> credentialHardener =
        new Pbkdf2WithHmacSha256CredentialHardener();

    final Pbkdf2WithHmacSha256Parameters hardeningParameters = Pbkdf2WithHmacSha256Parameters
        .builder()
        .setSalt(new byte[]{1})
        .setDerivedKeyBitlength(256)
        .setIterationCount(1000)
        .build();

    key1 = credentialHardener
        .hardenCredential(CleartextPassword.create("password"), hardeningParameters)
        .blockingGet();

    key2 = credentialHardener
        .hardenCredential(CleartextPassword.create("not password"), hardeningParameters)
        .blockingGet();
  }

  @Test(expected = RuntimeException.class)
  public void testConstructor_nullCredentialConverter() throws NoSuchPaddingException, NoSuchAlgorithmException {
    new AesProvider(null);
  }

  @Test(expected = RuntimeException.class)
  public void testEncrypt_nullCleartext() {
    aesProvider.encrypt(null, iv1, key1);
  }

  @Test(expected = RuntimeException.class)
  public void testEncrypt_nullInitialisationVector() {
    aesProvider.encrypt(new byte[0], null, key1);
  }

  @Test(expected = RuntimeException.class)
  public void testEncrypt_nullCredential() {
    aesProvider.encrypt(new byte[0], iv1, null);
  }

  @Test(expected = RuntimeException.class)
  public void testDecrypt_nullCiphertext() {
    aesProvider.decrypt(null, iv1, key1);
  }

  @Test(expected = RuntimeException.class)
  public void testDecrypt_nullInitialisationVector() {
    aesProvider.decrypt(new byte[0], null, key1);
  }

  @Test(expected = RuntimeException.class)
  public void testDecrypt_nullCredential() {
    aesProvider.decrypt(new byte[0], iv1, null);
  }

  @Test
  public void testEncryptThenDecrypt_emptyCiphertext() {
    final byte[] cleartext = new byte[0];

    aesProvider
        .encrypt(cleartext, iv1, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv1, key1))
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
        .assertError(EncryptionError.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testEncryptThenDecrypt_initialisationVectorsDoNotMatch() {
    final byte[] cleartext = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};

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

    aesProvider
        .encrypt(cleartext, iv1, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv1, key2))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertError(EncryptionError.class)
        .assertNoValues()
        .assertNotComplete();
  }

  @Test
  public void testEncryptThenDecrypt_initialisationVectorsAndCredentialsMatch() {
    final byte[] cleartext = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};

    aesProvider
        .encrypt(cleartext, iv1, key1)
        .flatMap(ciphertext -> aesProvider.decrypt(ciphertext, iv1, key1))
        .test()
        .awaitDone(200, MILLISECONDS)
        .assertNoErrors()
        .assertValue(decryptedData -> Arrays.equals(decryptedData, cleartext))
        .assertComplete();
  }
}
