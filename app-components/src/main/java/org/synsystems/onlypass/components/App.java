package org.synsystems.onlypass.components;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.synsystems.onlypass.components.logging.CrashlyticsTree;
import org.synsystems.onlypass.components.logging.LoggerTree;
import org.synsystems.onlypass.components.preferences.GlobalPreferences;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Boolean.TRUE;

/**
 * The core application class. For convenience, activities running under this application can get an instance via
 * {@link #getFromContext(Context)}.
 */
public abstract class App extends Application {
  @Inject
  protected Environment environment;

  @Inject
  protected Crashlytics crashlytics;

  @Inject
  protected CrashlyticsTree crashlyticsTree;

  @Inject
  protected LoggerTree loggerTree;

  @Inject
  protected GlobalPreferences globalPreferences;

  private AppComponent appComponent;

  private Disposable applicationTasks;

  protected abstract AppComponent createAppComponent();

  @Override
  public void onCreate() {
    super.onCreate();

    final Completable setupApplication = Completable.concatArray(
        setupDagger(),
        injectDependencies(),
        setupLocalLogging(),
        setupStetho());

    final Completable handleOngoingEvents = handleRemoteLoggingPreferenceChanges();

    applicationTasks = setupApplication
        .andThen(handleOngoingEvents)
        .subscribe();
  }

  @Override
  public void onTerminate() {
    if (applicationTasks != null) {
      applicationTasks.dispose();
    }

    super.onTerminate();
  }

  /**
   * Convenience method to get the current {@link App} instance from an activity context.
   *
   * @param context
   *     an activity context
   *
   * @return the current app instance
   */
  @NonNull
  public static App getFromContext(@NonNull final Context context) {
    checkNotNull(context);

    return (App) context.getApplicationContext();
  }

  /**
   * Gets the core app component. Each call returns the same instance. This method must not be called before
   * {@link #onCreate()} has completed.
   *
   * @return the core app component
   */
  @NonNull
  public AppComponent getAppComponent() {
    if (appComponent == null) {
      throw new IllegalStateException("App component is not available until onCreate() has completed.");
    }

    return appComponent;
  }

  private Completable setupDagger() {
    return Completable.fromRunnable(() -> appComponent = createAppComponent());
  }

  private Completable injectDependencies() {
    return Completable.fromRunnable(() -> appComponent.inject(this));
  }

  private Completable setupLocalLogging() {
    final Completable installLogger = Completable.fromRunnable(() -> {
      final PrettyFormatStrategy strategy = PrettyFormatStrategy
          .newBuilder()
          .methodOffset(5) // Accounts for timber log format
          .tag("OnlyPass")
          .build();

      final AndroidLogAdapter adapter = new AndroidLogAdapter(strategy);

      Logger.addLogAdapter(adapter);
      Timber.plant(loggerTree);
    });

    return Single
        .fromCallable(environment::isLocalLoggingEnabled)
        .filter(TRUE::equals)
        .flatMapCompletable(pulse -> installLogger);
  }

  private Completable handleRemoteLoggingPreferenceChanges() {
    final Completable handleRemoteLoggingEnabled = globalPreferences
        .observeRemoteLoggingEnabled()
        .flatMapCompletable(pulse -> Completable.fromRunnable(() -> {
          Fabric.with(this, crashlytics);
          Timber.plant(crashlyticsTree);
        }));

    // The only way to disable Fabric is to restart the app
    final Completable handleRemoteLoggingDisabled = globalPreferences
        .observeRemoteLoggingDisabled()
        .flatMapCompletable(pulse -> Completable.fromRunnable(() -> ProcessPhoenix.triggerRebirth(this)));

    return Completable.mergeArray(handleRemoteLoggingEnabled, handleRemoteLoggingDisabled);
  }

  private Completable setupStetho() {
    return Single
        .just(environment)
        .map(Environment::isStethoEnabled)
        .flatMapCompletable(stethoEnabled -> stethoEnabled ?
            Completable.fromRunnable(() -> Stetho.initializeWithDefaults(this)) :
            Completable.complete());
  }
}