import org.synsystems.onlypass.components.App;
import org.synsystems.onlypass.components.AppComponent;
import org.synsystems.onlypass.components.DaggerProdAppComponent;

public class ProdApp extends App {
  @Override
  protected AppComponent createAppComponent() {
    return DaggerProdAppComponent.create();
  }
}