package org.synsystems.onlypass.components.cryptography.credentialhardening;

import android.support.annotation.NonNull;

import org.synsystems.onlypass.components.cryptography.credentials.CleartextPassword;
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link CredentialHardener} that converts {@link CleartextPassword}s to {@link DerivedKey}s with
 * the PBKDF2WithHmacSHA256 algorithm.
 */
public class Pbkdf2WithHmacSha256CredentialHardener implements CredentialHardener<
    CleartextPassword,
    DerivedKey,
    Pbkdf2WithHmacSha256Configuration> {

  @NonNull
  private final SecretKeyFactory secretKeyFactory;

  /**
   * Constructs a new Pbkdf2WithHmacSha256CredentialHardener.
   *
   * @throws NoSuchAlgorithmException
   *     if PBKDF2WithHmacSHA256 is not available at runtime
   */
  public Pbkdf2WithHmacSha256CredentialHardener() throws NoSuchAlgorithmException {
    // A hard dependency is ok in this case since we need a specific instance
    secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
  }

  @NonNull
  @Override
  public Single<DerivedKey> hardenCredential(
      @NonNull final CleartextPassword insecureCredential,
      @NonNull final Pbkdf2WithHmacSha256Configuration parameters) {

    checkNotNull(insecureCredential);
    checkNotNull(parameters);

    return Single
        .just(insecureCredential)
        .map(CleartextPassword::getPassword)
        .map(String::toCharArray)
        .map(passwordChars -> new PBEKeySpec(
            passwordChars,
            parameters.getSalt(),
            parameters.getIterationCount(),
            parameters.getDerivedKeyBitlength()))
        .map(secretKeyFactory::generateSecret)
        .map(DerivedKey::create);
  }
}