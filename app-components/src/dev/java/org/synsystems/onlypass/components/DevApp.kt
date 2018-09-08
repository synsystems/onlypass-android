import org.synsystems.onlypass.components.App
import org.synsystems.onlypass.components.AppComponent
import org.synsystems.onlypass.components.DaggerDevAppComponent

class DevApp : App() {

  @Override
  protected fun createAppComponent(): = DaggerDevAppComponent.create()
}