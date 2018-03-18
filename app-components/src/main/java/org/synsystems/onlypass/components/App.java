package org.synsystems.onlypass.components;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.google.common.collect.ImmutableList;
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

public class App extends Application {
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

  private Disposable ongoingAppTasks;

  @Override
  public void onCreate() {
    super.onCreate();

    final Completable ongoingTasks = Completable.merge(ImmutableList.of(manageRemoteLogging()));

    ongoingAppTasks = setupDagger()
        .andThen(injectDependencies())
        .andThen(setupLocalLogging())
        .andThen(ongoingTasks)
        .subscribe();
  }

  @Override
  public void onTerminate() {
    ongoingAppTasks.dispose();

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
    return (App) context.getApplicationContext();
  }

  /**
   * Gets the core app component. Each call returns the same instance.
   *
   * @return the core app component
   */
  public AppComponent getAppComponent() {
    return appComponent;
  }

  private Completable setupDagger() {
    return Completable.fromRunnable(() -> appComponent = DaggerAppComponent.create());
  }

  private Completable injectDependencies() {
    return Completable.fromRunnable(() -> appComponent.inject(this));
  }

  private Completable setupLocalLogging() {
    final Completable enableLogging = Single
        .fromCallable(() -> PrettyFormatStrategy
            .newBuilder()
            .methodOffset(5)
            .tag("OnlyPass")
            .build())
        .map(AndroidLogAdapter::new)
        .flatMapCompletable(logAdapter -> Completable.fromRunnable(() -> {
          Logger.addLogAdapter(logAdapter);
          Timber.plant(loggerTree);
        }));

    return Single
        .fromCallable(() -> environment.isLocalLoggingEnabled())
        .flatMapCompletable(localLoggingEnabled -> localLoggingEnabled ?
            enableLogging :
            Completable.complete());
  }

  private Completable manageRemoteLogging() {
    final Completable enableRemoteLogging = globalPreferences
        .observeRemoteLoggingEnabled()
        .take(1)
        .flatMapCompletable(event -> Completable.fromRunnable(() -> {
          Fabric.with(this, crashlytics);
          Timber.plant(crashlyticsTree);
        }));

    // The only way to disable Fabric is to restart the app
    final Completable disableRemoteLogging = globalPreferences
        .observeRemoteLoggingDisabled()
        .flatMapCompletable(event -> Completable.fromRunnable(() -> ProcessPhoenix.triggerRebirth(this)));

    return Completable.merge(ImmutableList.of(enableRemoteLogging, disableRemoteLogging));
  }
}