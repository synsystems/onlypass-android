package org.synsystems.onlypass.components

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.jakewharton.processphoenix.ProcessPhoenix
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.fabric.sdk.android.Fabric
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import org.synsystems.onlypass.components.logging.CrashlyticsTree
import org.synsystems.onlypass.components.logging.LoggerTree
import org.synsystems.onlypass.components.preferences.GlobalPreferences
import timber.log.Timber
import javax.inject.Inject

/**
 * The core application class.
 */
abstract class App : Application() {

  @Inject
  protected lateinit var environment: Environment

  @Inject
  protected lateinit var crashlytics: Crashlytics

  @Inject
  protected lateinit var crashlyticsTree: CrashlyticsTree

  @Inject
  protected lateinit var loggerTree: LoggerTree

  @Inject
  protected lateinit var globalPreferences: GlobalPreferences

  private lateinit var appComponent: AppComponent
  fun getAppComponent() = appComponent

  private lateinit var applicationTasks: Disposable

  protected abstract fun createAppComponent(): AppComponent

  override fun onCreate() {
    super.onCreate()

    val setupApplication = Completable.concatArray(
        setupDagger(),
        injectDependencies(),
        setupLocalLogging(),
        setupStetho())

    val handleOngoingEvents = handleRemoteLoggingPreferenceChanges()

    applicationTasks = setupApplication
        .andThen(handleOngoingEvents)
        .subscribe()
  }

  override fun onTerminate() {
    applicationTasks.dispose()

    super.onTerminate()
  }

  private fun setupDagger() = Completable.fromRunnable { appComponent = createAppComponent() }

  private fun injectDependencies() = Completable.fromRunnable { appComponent.inject(this) }

  private fun setupLocalLogging(): Completable {
    val installLogger = Completable.fromRunnable {
      val adapter = AndroidLogAdapter(
          PrettyFormatStrategy
              .newBuilder()
              .methodOffset(5) // Accounts for timber log format
              .tag("OnlyPass")
              .build())

      Logger.addLogAdapter(adapter)
      Timber.plant(loggerTree)
    }

    return Completable.defer { if (environment.isLocalLoggingEnabled) installLogger else Completable.complete() }
  }

  private fun handleRemoteLoggingPreferenceChanges(): Completable {
    val handleRemoteLoggingEnabled = globalPreferences
        .observeRemoteLoggingEnabled()
        .flatMapCompletable {
          Completable.fromRunnable {
            Fabric.with(this, crashlytics)
            Timber.plant(crashlyticsTree)
          }
        }

    // The only way to disable Fabric is to restart the app
    val handleRemoteLoggingDisabled = globalPreferences
        .observeRemoteLoggingDisabled()
        .flatMapCompletable { Completable.fromRunnable { ProcessPhoenix.triggerRebirth(this) } }

    return Completable.mergeArray(handleRemoteLoggingEnabled, handleRemoteLoggingDisabled)
  }

  private fun setupStetho(): Completable {
    return Completable.fromRunnable { if (environment.isStethoEnabled) Stetho.initializeWithDefaults(this) }
  }
}