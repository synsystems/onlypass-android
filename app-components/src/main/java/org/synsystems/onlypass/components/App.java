package org.synsystems.onlypass.components;

import android.app.Application;

public class App extends Application {
  private AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    appComponent = DaggerAppComponent.create();
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }
}