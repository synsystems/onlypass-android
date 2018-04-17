package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.synsystems.onlypass.components.AppScope;
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import dagger.Module;
import dagger.Provides;

@Module
public class AesProviderModule {
  @Provides
  @AppScope
  public AesProvider<DerivedKey> provideAesProvider(final DerivedKeyCredentialConverter derivedKeyCredentialConverter) {
    try {
      return new AesProvider<>(derivedKeyCredentialConverter);
    } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException("Unable to provide AesProvider.", e);
    }
  }
}