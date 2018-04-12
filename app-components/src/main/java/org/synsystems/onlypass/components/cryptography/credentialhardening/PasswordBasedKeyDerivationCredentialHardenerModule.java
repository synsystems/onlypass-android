package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.synsystems.onlypass.components.AppScope;

import java.security.NoSuchAlgorithmException;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class PasswordBasedKeyDerivationCredentialHardenerModule {
  @Provides
  @AppScope
  public PasswordBasedKeyDerivationCredentialHardener providePasswordBasedKeyDerivationCredentialHardener() {
    try {
      return new PasswordBasedKeyDerivationCredentialHardener();
    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException("Unable to provide PasswordBasedKeyDerivationCredentialHardener.", e);
    }
  }
}