package org.synsystems.onlypass.components;

import org.synsystems.onlypass.components.cryptography.credentialhardening.PasswordBasedKeyDerivationConfigurationModule;
import org.synsystems.onlypass.components.cryptography.credentialhardening.PasswordBasedKeyDerivationCredentialHardenerModule;
import org.synsystems.onlypass.components.cryptography.encryption.aes.AesProviderModule;
import org.synsystems.onlypass.components.cryptography.encryption.aes.DerivedKeyCredentialConverterModule;
import org.synsystems.onlypass.components.logging.LoggingModule;
import org.synsystems.onlypass.components.preferences.PreferencesModule;

import dagger.Component;

@Component(modules = {
    AppModule.class,
    LoggingModule.class,
    PreferencesModule.class,
    PasswordBasedKeyDerivationConfigurationModule.class,
    PasswordBasedKeyDerivationCredentialHardenerModule.class,
    AesProviderModule.class,
    DerivedKeyCredentialConverterModule.class})
@AppScope
public interface AppComponent {
  public Environment getEnvironment();

  public void inject(App app);

  @Component.Builder
  public interface Builder {
    public Builder setAppModule(AppModule module);

    public Builder setLoggingModule(LoggingModule module);

    public Builder setPreferencesModule(PreferencesModule module);

    public Builder setPasswordBasedKeyDerivationConfigurationModule(
        PasswordBasedKeyDerivationConfigurationModule module);

    public Builder setPasswordBasedKeyDerivationCredentialHardenerModule(
        PasswordBasedKeyDerivationCredentialHardenerModule module);

    public Builder setAesProviderModule(AesProviderModule module);

    public Builder setDerivedKeyCredentialConverterModule(DerivedKeyCredentialConverterModule module);

    public AppComponent build();
  }
}