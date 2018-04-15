package org.synsystems.onlypass.components;

import org.synsystems.onlypass.components.logging.CrashlyticsTreeModule;
import org.synsystems.onlypass.components.logging.LoggerTreeModule;
import org.synsystems.onlypass.components.preferences.PreferencesModule;

import dagger.Component;

@Component(modules = {
    CrashlyticsTreeModule.class,
    LoggerTreeModule.class,
    CrashlyticsModule.class,
    PreferencesModule.class})
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

    public AppComponent build();
  }
}