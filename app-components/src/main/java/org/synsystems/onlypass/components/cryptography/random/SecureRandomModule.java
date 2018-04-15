package org.synsystems.onlypass.components.cryptography.random;

import org.synsystems.onlypass.components.AppScope;

import java.security.SecureRandom;

import dagger.Module;
import dagger.Provides;

@Module
@AppScope
public class SecureRandomModule {
  @Provides
  @AppScope
  public SecureRandom provideSecureRandom() {
    return new SecureRandom();
  }
}
