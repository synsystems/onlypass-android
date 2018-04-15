import org.synsystems.onlypass.components.App;
import org.synsystems.onlypass.components.AppComponent;

import org.synsystems.onlypass.components.DaggerInternalTestingAppComponent;

public class InternalTestingApp extends App {
  @Override
  protected AppComponent createAppComponent() {
    return DaggerInternalTestingAppComponent.create();
  }
}