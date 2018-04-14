package org.synsystems.onlypass.components.cryptography.encryption.aes;

import android.support.annotation.NonNull;

import org.synsystems.onlypass.components.cryptography.credentials.Credential;
import org.synsystems.onlypass.components.cryptography.encryption.EncryptionError;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Completable;
import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Performs AES encryption and decryption operations.
 *
 * @param <C>
 *     the type of credential used to encrypt and decrypt data
 */
public class AesProvider<C extends Credential> {
  @NonNull
  private final CredentialConverter<C> credentialConverter;

  @NonNull
  private final Cipher cipher;

  /**
   * Constructs a new AesProvider.
   *
   * @param credentialConverter
   *     converts credentials to {@link SecretKeySpec}s
   *
   * @throws NoSuchAlgorithmException
   *     if the AES-GCM is not available at runtime
   * @throws NoSuchPaddingException
   *     if PKCS5Padding is not available at runtime
   */
  public AesProvider(
      @NonNull final CredentialConverter<C> credentialConverter)
      throws NoSuchAlgorithmException, NoSuchPaddingException {

    this.credentialConverter = checkNotNull(credentialConverter);

    // A hard dependency is ok in this case since we need a specific instance for AES
    cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
  }

  /**
   * Encrypts cleartext with AES.
   *
   * @param cleartext
   *     the cleartext to encrypt
   * @param initialisationVector
   *     the initialisation vector to use
   * @param credential
   *     the credential to encrypt the data with
   *
   * @return a new single that emits the ciphertext
   */
  @NonNull
  public Single<byte[]> encrypt(
      @NonNull final byte[] cleartext,
      @NonNull final byte[] initialisationVector,
      @NonNull final C credential) {

    checkNotNull(cleartext);
    checkNotNull(initialisationVector);
    checkNotNull(credential);

    final Completable prepareForEncryption = Single
        .zip(
            credentialConverter.toSecretKeySpec(credential),
            Single.fromCallable(() -> new IvParameterSpec(initialisationVector)),
            Single.just(Cipher.ENCRYPT_MODE),
            this::prepareCipher)
        .flatMapCompletable(wrappedCompletable -> wrappedCompletable);

    return prepareForEncryption
        .andThen(performEncryption(cleartext))
        .onErrorResumeNext(error -> Single.error(new EncryptionError("Encryption failed.", error)));
  }

  /**
   * Decrypts ciphertext with AES. The operation will fail if the initialisation vector or the credential do not
   * match during encryption.
   *
   * @param ciphertext
   *     the ciphertext to decrypt
   * @param initialisationVector
   *     the initialisation vector to use
   * @param credential
   *     the credential to decrypt the data with
   *
   * @return a new single that emits the decrypted cleartext
   */
  @NonNull
  public Single<byte[]> decrypt(
      @NonNull final byte[] ciphertext,
      @NonNull final byte[] initialisationVector,
      @NonNull final C credential) {

    checkNotNull(ciphertext);
    checkNotNull(initialisationVector);
    checkNotNull(credential);

    final Completable prepareForDecryption = Single
        .zip(
            credentialConverter.toSecretKeySpec(credential),
            Single.fromCallable(() -> new IvParameterSpec(initialisationVector)),
            Single.just(Cipher.DECRYPT_MODE),
            this::prepareCipher)
        .flatMapCompletable(wrappedCompletable -> wrappedCompletable);

    return prepareForDecryption
        .andThen(performDecryption(ciphertext))
        .onErrorResumeNext(error -> Single.error(new EncryptionError("Decryption failed.", error)));
  }

  @NonNull
  private Completable prepareCipher(
      @NonNull final SecretKey key,
      @NonNull final IvParameterSpec initialisationVector,
      final int cipherMode) {

    return Single
        .zip(
            Single.fromCallable(() -> initialisationVector.getIV().length),
            Single.fromCallable(initialisationVector::getIV),
            GCMParameterSpec::new)
        .flatMapCompletable(gcmSpec -> Completable.fromAction(() -> cipher.init(
            cipherMode,
            key,
            gcmSpec)));
  }

  @NonNull
  private Single<byte[]> performEncryption(@NonNull final byte[] cleartext) {
    return Single.fromCallable(() -> cipher.doFinal(cleartext));
  }

  @NonNull
  private Single<byte[]> performDecryption(@NonNull final byte[] ciphertext) {
    return Single.fromCallable(() -> cipher.doFinal(ciphertext));
  }

  /**
   * Converts {@link Credential}s to {@link SecretKeySpec}s.
   *
   * @param <C>
   *     the type of credential that can be converted
   */
  public interface CredentialConverter<C extends Credential> {
    /**
     * Converts the supplied credential to a secret key spec that is configured for AES. Each call with the same
     * arguments must produce the same result.
     *
     * @param credential
     *     the credential to convert
     *
     * @return a new single that emits the secret key spec
     */
    @NonNull
    public Single<SecretKeySpec> toSecretKeySpec(@NonNull final C credential);
  }
}