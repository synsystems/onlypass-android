package org.synsystems.onlypass.components.cryptography.encryption.aes;

import org.synsystems.onlypass.components.AppScope;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class AesConfigurationModule {
  @Provides
  @AppScope
  public AesConfiguration provideAesConfiguration() {
    return AesConfiguration
        .builder()
        .setInitialisationVectorBitlength(128)
        .build();
  }
}