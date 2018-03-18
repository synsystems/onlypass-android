package org.synsystems.onlypass.components;

import org.synsystems.onlypass.components.logging.LoggingModule;
import org.synsystems.onlypass.components.preferences.PreferencesModule;

import dagger.Component;

@Component(modules = {AppModule.class, LoggingModule.class, PreferencesModule.class})
@AppScope
public interface AppComponent {
  public Environment getEnvironment();

  public void inject(App app);

  @Component.Builder
  public interface Builder {
    public Builder setAppModule(AppModule appModule);

    public AppComponent build();
  }
}