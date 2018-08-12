package org.synsystems.onlypass.components

import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256CredentialHardenerModule
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProviderModule
import org.synsystems.onlypass.components.cryptography.encryption.aes.DerivedKeyCredentialConverterModule
import org.synsystems.onlypass.components.cryptography.random.SecureByteArrayGeneratorModule
import org.synsystems.onlypass.components.cryptography.random.SecureRandomModule
import org.synsystems.onlypass.components.logging.CrashlyticsTreeModule
import org.synsystems.onlypass.components.logging.LoggerTreeModule
import org.synsystems.onlypass.components.preferences.GlobalPreferencesModule

import dagger.Component

@Component(modules = {
  CrashlyticsTreeModule.class,
  LoggerTreeModule.class,
  CrashlyticsModule.class,
  GlobalPreferencesModule.class,
  Pbkdf2WithHmacSha256CredentialHardenerModule.class,
  AesProviderModule.class,
  DerivedKeyCredentialConverterModule.class,
  SecureByteArrayGeneratorModule.class,
  SecureRandomModule.class,
  EnvironmentModule.class
})
@AppScope
interface DevAppComponent : AppComponent {

  @Component.Builder
  interface Builder : AppComponent.Builder {

    fun setEnvironmentModule(module: EnvironmentModule): Builder

    fun build(): DevAppComponent
  }
}