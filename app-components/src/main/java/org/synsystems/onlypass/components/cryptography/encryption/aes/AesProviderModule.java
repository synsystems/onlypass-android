package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.synsystems.onlypass.components.AppScope;
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey;
import org.synsystems.onlypass.components.main.data.vault.io.AesConfiguration;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class AesProviderModule {
  @Provides
  @AppScope
  public AesProvider<DerivedKey> provideAesProvider(
      final AesConfiguration configuration,
      final DerivedKeyCredentialConverter derivedKeyCredentialConverter) {

    try {
      return new AesProvider<>(configuration, derivedKeyCredentialConverter);
    } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException("Unable to provide AesProvider.", e);
    }
  }
}