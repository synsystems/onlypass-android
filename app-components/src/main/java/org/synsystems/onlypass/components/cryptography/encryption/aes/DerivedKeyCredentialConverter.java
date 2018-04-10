package org.synsystems.onlypass.components.cryptography.encryption.aes;

import android.support.annotation.NonNull;

import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey;
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProvider.CredentialConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Single;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link CredentialConverter} that operates on {@link DerivedKey}s.
 */
public class DerivedKeyCredentialConverter implements CredentialConverter<DerivedKey> {
  @NonNull
  @Override
  public Single<SecretKeySpec> toSecretKeySpec(@NonNull final DerivedKey credential) {
    checkNotNull(credential);

    return Single
        .fromCallable(credential::getKey)
        .map(SecretKey::getEncoded)
        .map(encodedKey -> new SecretKeySpec(encodedKey, "AES"));
  }
}