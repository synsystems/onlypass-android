package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.synsystems.onlypass.components.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DerivedKeyCredentialConverterModule {
  @Provides
  @AppScope
  public DerivedKeyCredentialConverter provideDerivedKeyCredentialConverter() {
    return new DerivedKeyCredentialConverter();
  }
}