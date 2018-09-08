package org.synsystems.onlypass.components.cryptography.encryption.aes

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.AppScope
import org.synsystems.onlypass.components.cryptography.credentials.DerivedKey

@Module
class AesProviderModule {

  @Provides
  @AppScope
  fun provideAesProvider(derivedKeyCredentialConverter: DerivedKeyCredentialConverter): AesProvider<DerivedKey> {
    return AesProvider(derivedKeyCredentialConverter)
  }
}