package org.synsystems.onlypass.components.cryptography.credentialhardening;

import org.synsystems.onlypass.components.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class PasswordBasedKeyDerivationConfigurationModule {
  @Provides
  @AppScope
  public PasswordBasedKeyDerivationConfiguration providePasswordBasedKeyDerivationParameters() {
    return PasswordBasedKeyDerivationConfiguration
        .builder()
        .setDerivedKeyBitlength(256)
        .setIterationCount(100000)
        .build();
  }
}