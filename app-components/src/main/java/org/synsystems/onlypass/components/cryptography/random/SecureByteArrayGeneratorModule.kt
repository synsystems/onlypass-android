package org.synsystems.onlypass.components.cryptography.random

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.AppScope
import java.security.SecureRandom

@Module
class SecureByteArrayGeneratorModule {
  @Provides
  @AppScope
  fun provideSecureByteArrayGenerator(secureRandom: SecureRandom) = SecureByteArrayGenerator(secureRandom)
}