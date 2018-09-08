package org.synsystems.onlypass.components.cryptography.credentialhardening;

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.AppScope

@Module
class Pbkdf2WithHmacSha256CredentialHardenerModule {

  @Provides
  @AppScope
  fun providePasswordBasedKeyDerivationCredentialHardener() = Pbkdf2WithHmacSha256CredentialHardener()
}