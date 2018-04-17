package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.synsystems.onlypass.components.AppScope;

import java.security.NoSuchAlgorithmException;

import dagger.Module;
import dagger.Provides;

@Module
public class Pbkdf2WithHmacSha256CredentialHardenerModule {
  @Provides
  @AppScope
  public Pbkdf2WithHmacSha256CredentialHardener providePasswordBasedKeyDerivationCredentialHardener() {
    try {
      return new Pbkdf2WithHmacSha256CredentialHardener();
    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException("Unable to provide Pbkdf2WithHmacSha256CredentialHardener.", e);
    }
  }
}