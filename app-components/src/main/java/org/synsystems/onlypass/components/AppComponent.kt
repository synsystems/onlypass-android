package org.synsystems.onlypass.components

import dagger.Component
import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256CredentialHardenerModule
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProviderModule
import org.synsystems.onlypass.components.cryptography.encryption.aes.DerivedKeyCredentialConverterModule
import org.synsystems.onlypass.components.cryptography.random.SecureByteArrayGeneratorModule
import org.synsystems.onlypass.components.cryptography.random.SecureRandomModule
import org.synsystems.onlypass.components.logging.CrashlyticsTreeModule
import org.synsystems.onlypass.components.logging.LoggerTreeModule
import org.synsystems.onlypass.components.preferences.GlobalPreferencesModule

@Component(
    modules = [
      EnvironmentModule::class,
      CrashlyticsTreeModule::class,
      LoggerTreeModule::class,
      CrashlyticsModule::class,
      GlobalPreferencesModule::class,
      Pbkdf2WithHmacSha256CredentialHardenerModule::class,
      AesProviderModule::class,
      DerivedKeyCredentialConverterModule::class,
      SecureByteArrayGeneratorModule::class,
      SecureRandomModule::class
    ])
@AppScope
interface AppComponent {

  fun inject(app: App)

  interface Builder {

    fun build(): AppComponent

    fun setEnvironmentModule(module: EnvironmentModule): Builder

    fun setCrashlyticsTreeModule(module: CrashlyticsTreeModule): Builder

    fun setLoggerTreeModule(module: LoggerTreeModule): Builder

    fun setCrashlyticsModule(module: CrashlyticsModule): Builder

    fun setPreferencesModule(module: GlobalPreferencesModule): Builder

    fun setPbkdf2WithHmacSha256CredentialHardenerModule(module: Pbkdf2WithHmacSha256CredentialHardenerModule): Builder

    fun setAesProviderModule(module: AesProviderModule): Builder

    fun setDerivedKeyCredentialConverterModule(module: DerivedKeyCredentialConverterModule): Builder

    fun setSecureByteArrayGeneratorModule(module: SecureByteArrayGeneratorModule): Builder

    fun setSecureRandomModule(module: SecureRandomModule): Builder
  }
}