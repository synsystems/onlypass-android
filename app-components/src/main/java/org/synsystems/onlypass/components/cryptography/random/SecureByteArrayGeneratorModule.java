package org.synsystems.onlypass.components.cryptography.random;

import org.synsystems.onlypass.components.AppScope;

import java.security.SecureRandom;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class SecureByteArrayGeneratorModule {
  @Provides
  @AppScope
  public SecureByteArrayGenerator provideSecureByteArrayGenerator(final SecureRandom secureRandom) {
    return new SecureByteArrayGenerator(secureRandom);
  }
}