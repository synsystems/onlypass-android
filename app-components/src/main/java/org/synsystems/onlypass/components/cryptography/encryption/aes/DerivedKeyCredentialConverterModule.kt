package org.synsystems.onlypass.components.cryptography.encryption.aes

import dagger.Module
import dagger.Provides
import org.synsystems.onlypass.components.AppScope

@Module
class DerivedKeyCredentialConverterModule {

  @Provides
  @AppScope
  fun provideDerivedKeyCredentialConverter() = DerivedKeyCredentialConverter()
}