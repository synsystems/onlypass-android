package org.synsystems.onlypass.components;

import org.synsystems.onlypass.components.cryptography.credentialhardening.Pbkdf2WithHmacSha256CredentialHardenerModule;
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProviderModule;
import org.synsystems.onlypass.components.cryptography.encryption.aes.DerivedKeyCredentialConverterModule;
import org.synsystems.onlypass.components.cryptography.random.SecureByteArrayGeneratorModule;
import org.synsystems.onlypass.components.cryptography.random.SecureRandomModule;
import org.synsystems.onlypass.components.logging.LoggingModule;
import org.synsystems.onlypass.components.preferences.PreferencesModule;

import dagger.Component;

@Component(modules = {
    AppModule.class,
    LoggingModule.class,
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
    public Builder setAppModule(AppModule module);

    public Builder setLoggingModule(LoggingModule module);

    public Builder setPreferencesModule(PreferencesModule module);

    public Builder setPasswordBasedKeyDerivationCredentialHardenerModule(
        Pbkdf2WithHmacSha256CredentialHardenerModule module);

    public Builder setAesProviderModule(AesProviderModule module);

    public Builder setDerivedKeyCredentialConverterModule(DerivedKeyCredentialConverterModule module);

    public Builder setSecureByteArrayGeneratorModule(SecureByteArrayGeneratorModule module);

    public Builder setSecureRandomModule(SecureRandomModule module);

    public AppComponent build();
  }
}