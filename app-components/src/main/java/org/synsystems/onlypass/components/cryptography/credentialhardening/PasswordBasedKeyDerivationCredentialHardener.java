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
 * the BKDF2WithHmacSHA256 algorithm.
 */
public class PasswordBasedKeyDerivationCredentialHardener implements CredentialHardener<
    CleartextPassword,
    DerivedKey,
    HashingSalt> {

  @NonNull
  private final PasswordBasedKeyDerivationConfiguration parameters;

  @NonNull
  private final SecretKeyFactory secretKeyFactory;

  /**
   * Constructs a new PasswordBasedKeyDerivationCredentialHardener.
   *
   * @param configuration
   *     the configuration to use throughout all hardening operations
   *
   * @throws NoSuchAlgorithmException
   *     if the PBKDF2WithHmacSHA256 algorithm is not available at runtime
   */
  public PasswordBasedKeyDerivationCredentialHardener(
      @NonNull final PasswordBasedKeyDerivationConfiguration configuration)
      throws NoSuchAlgorithmException {

    this.parameters = checkNotNull(configuration);

    // A hard dependency is ok in this case since we need a specific instance
    secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
  }

  @NonNull
  @Override
  public Single<DerivedKey> hardenCredential(
      @NonNull final CleartextPassword insecureCredential,
      @NonNull final HashingSalt hashingSalt) {

    checkNotNull(insecureCredential);
    checkNotNull(hashingSalt);

    return Single
        .just(insecureCredential)
        .map(CleartextPassword::getPassword)
        .map(String::toCharArray)
        .map(passwordChars -> new PBEKeySpec(
            passwordChars,
            hashingSalt.getSalt(),
            parameters.getIterationCount(),
            parameters.getDerivedKeyBitlength()))
        .map(secretKeyFactory::generateSecret)
        .map(DerivedKey::create);
  }
}