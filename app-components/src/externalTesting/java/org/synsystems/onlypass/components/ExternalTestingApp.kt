import org.synsystems.onlypass.components.App
import org.synsystems.onlypass.components.AppComponent
import org.synsystems.onlypass.components.DaggerExternalTestingAppComponent

class ExternalTestingApp : App() {

  @Override
  protected fun createAppComponent() = DaggerExternalTestingAppComponent.create()
}