package org.synsystems.onlypass.components;

import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256CredentialHardenerModule;
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProviderModule;
import org.synsystems.onlypass.components.cryptography.encryption.aes.DerivedKeyCredentialConverterModule;
import org.synsystems.onlypass.components.cryptography.random.SecureByteArrayGeneratorModule;
import org.synsystems.onlypass.components.cryptography.random.SecureRandomModule;
import org.synsystems.onlypass.components.logging.CrashlyticsTreeModule;
import org.synsystems.onlypass.components.logging.LoggerTreeModule;
import org.synsystems.onlypass.components.preferences.PreferencesModule;

import dagger.Component;

@Component(modules = {
    CrashlyticsTreeModule.class,
    LoggerTreeModule.class,
    CrashlyticsModule.class,
    PreferencesModule.class,
    Pbkdf2WithHmacSha256CredentialHardenerModule.class,
    AesProviderModule.class,
    DerivedKeyCredentialConverterModule.class,
    SecureByteArrayGeneratorModule.class,
    SecureRandomModule.class})
@AppScope
public interface AppComponent {
  public Environment getEnvironment();

  public void inject(App app);

  @Component.Builder
  public interface Builder {
    public Builder setCrashlyticsTreeModule(CrashlyticsTreeModule module);

    public Builder setLoggerTreeModule(LoggerTreeModule module);

    public Builder setCrashlyticsModule(CrashlyticsModule module);

    public Builder setPreferencesModule(PreferencesModule module);

    public Builder setPasswordBasedKeyDerivationCredentialHardenerModule(Pbkdf2WithHmacSha256CredentialHardenerModule module);

    public Builder setAesProviderModule(AesProviderModule module);

    public Builder setDerivedKeyCredentialConverterModule(DerivedKeyCredentialConverterModule module);

    public Builder setSecureByteArrayGeneratorModule(SecureByteArrayGeneratorModule module);

    public Builder setSecureRandomModule(SecureRandomModule module);

    public AppComponent build();
  }
}