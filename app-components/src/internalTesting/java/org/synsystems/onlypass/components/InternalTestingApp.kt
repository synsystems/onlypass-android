import org.synsystems.onlypass.components.App
import org.synsystems.onlypass.components.AppComponent
import org.synsystems.onlypass.components.DaggerInternalTestingAppComponent

class InternalTestingApp : App() {

  @Override
  protected fun createAppComponent() = DaggerInternalTestingAppComponent.create()
}