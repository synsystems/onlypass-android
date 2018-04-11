package org.synsystems.onlypass.components;

import org.synsystems.onlypass.components.logging.CrashlyticsTreeModule;
import org.synsystems.onlypass.components.logging.LoggerTreeModule;
import org.synsystems.onlypass.components.preferences.PreferencesModule;

import dagger.Component;

@Component(modules = {AppModule.class, CrashlyticsTreeModule.class, LoggerTreeModule.class, PreferencesModule.class})
@AppScope
public interface AppComponent {
  public Environment getEnvironment();

  public void inject(App app);

  @Component.Builder
  public interface Builder {
    public Builder setAppModule(AppModule appModule);

    public Builder setLoggingModule(LoggingModule loggingModule);

    public Builder setPreferencesModule(PreferencesModule preferencesModule);

    public AppComponent build();
  }
}