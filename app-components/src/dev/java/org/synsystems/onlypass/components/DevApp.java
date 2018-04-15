import org.synsystems.onlypass.components.App;
import org.synsystems.onlypass.components.AppComponent;
import org.synsystems.onlypass.components.DaggerDevAppComponent;

public class DevApp extends App {
  @Override
  protected AppComponent createAppComponent() {
    return DaggerDevAppComponent.create();
  }
}