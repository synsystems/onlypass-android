package org.synsystems.onlypass.components;

import dagger.Component;

@Component(modules = AppModule.class)
@AppScope
public interface AppComponent {
  public Environment getEnvironment();

  @Component.Builder
  public interface Builder {
    public Builder setAppModule(AppModule appModule);

    public AppComponent build();
  }
}