import org.synsystems.onlypass.components.App
import org.synsystems.onlypass.components.AppComponent
import org.synsystems.onlypass.components.DaggerProdAppComponent

class ProdApp : App() {

  @Override
  protected fun createAppComponent() = DaggerProdAppComponent.create()
}