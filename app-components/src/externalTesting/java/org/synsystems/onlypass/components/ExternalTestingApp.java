import org.synsystems.onlypass.components.App;
import org.synsystems.onlypass.components.AppComponent;

import org.synsystems.onlypass.components.DaggerExternalTestingAppComponent;

public class ExternalTestingApp extends App {
  @Override
  protected AppComponent createAppComponent() {
    return DaggerExternalTestingAppComponent.create();
  }
}