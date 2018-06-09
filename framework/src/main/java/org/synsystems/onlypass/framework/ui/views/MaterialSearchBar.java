package org.synsystems.onlypass.framework.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.utils.AnimationUtil;
import com.miguelcatalan.materialsearchview.utils.AnimationUtil.AnimationListener;

import org.synsystems.onlypass.framework.R;
import org.synsystems.onlypass.framework.rxutils.Pulse;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MaterialSearchBar extends MaterialSearchView {
  private final PublishSubject<String> searchTerms = PublishSubject.create();

  private final PublishSubject<Pulse> searchSubmitted = PublishSubject.create();

  private final PublishSubject<Pulse> searchOpened = PublishSubject.create();

  private final PublishSubject<Pulse> searchClosed = PublishSubject.create();

  private View searchLayout;

  private View searchTopBar;

  private View backButton;

  public MaterialSearchBar(final Context context) {
    super(context);
    init();
  }

  public MaterialSearchBar(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MaterialSearchBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @SuppressWarnings("ConstantConditions")
  private void init() {
    searchLayout = findViewById(R.id.search_layout);
    searchTopBar = findViewById(R.id.search_top_bar);
    backButton = findViewById(R.id.action_up_btn);

    final Drawable backArrowIcon = ContextCompat.getDrawable(getContext(), R.drawable.icon_back_arrow_white);
    backArrowIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
    setBackIcon(backArrowIcon);

    final Drawable crossIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_action_navigation_close);
    crossIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
    setCloseIcon(crossIcon);

    setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(final String query) {
        searchSubmitted.onNext(Pulse.getInstance());

        hideKeyboard(MaterialSearchBar.this);

        return true; // => handled
      }

      @Override
      public boolean onQueryTextChange(final String newText) {
        searchTerms.onNext(newText);
        return true; // => handled
      }
    });
  }

  public Observable<String> observeSearchTermChanged() {
    return searchTerms;
  }

  public Observable<Pulse> observeSearchSubmitted() {
    return searchSubmitted;
  }

  public Observable<Pulse> observeSearchOpened() {
    return searchOpened;
  }

  public Observable<Pulse> observeSearchClosed() {
    return searchClosed;
  }

  @Override
  public void showSearch() {
    showSearchRx().subscribe();
  }

  public Completable showSearchRx() {
    return Completable.create(emitter -> {
      if (isSearchOpen()) {
        emitter.onComplete();
        return;
      }

      final AnimationListener animationListener = new AnimationListener() {
        @Override
        public boolean onAnimationStart(final View view) {
          return false;
        }

        @Override
        public boolean onAnimationEnd(final View view) {
          MaterialSearchBar.super.showSearch(false);

          emitter.onComplete();

          searchOpened.onNext(Pulse.getInstance());

          return false;
        }

        @Override
        public boolean onAnimationCancel(final View view) {
          emitter.onComplete();
          return false;
        }
      };

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        searchLayout.setVisibility(View.VISIBLE);

        MaterialAnimationUtil.reveal(
            searchTopBar,
            animationListener,
            getWidth() - 24,
            getHeight() / 2);

      } else {
        searchLayout.setVisibility(VISIBLE);
        AnimationUtil.fadeOutView(searchLayout, AnimationUtil.ANIMATION_DURATION_MEDIUM, animationListener);
      }
    });
  }

  @Override
  public void closeSearch() {
    closeSearchRx().subscribe();
  }

  public Completable closeSearchRx() {
    return Completable.create(emitter -> {
      if (!isSearchOpen()) {
        emitter.onComplete();
        return;
      }

      final AnimationListener animationListener = new AnimationListener() {
        @Override
        public boolean onAnimationStart(final View view) {
          return false;
        }

        @Override
        public boolean onAnimationEnd(final View view) {
          MaterialSearchBar.super.closeSearch();

          emitter.onComplete();

          searchClosed.onNext(Pulse.getInstance());

          return false;
        }

        @Override
        public boolean onAnimationCancel(final View view) {
          emitter.onComplete();
          return false;
        }
      };

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        searchLayout.setVisibility(View.VISIBLE);

        MaterialAnimationUtil.conceal(
            searchTopBar,
            animationListener,
            (int) (backButton.getX() + backButton.getWidth() / 2),
            getHeight() / 2);

      } else {
        searchLayout.setVisibility(VISIBLE);
        AnimationUtil.fadeOutView(searchLayout, AnimationUtil.ANIMATION_DURATION_MEDIUM, animationListener);
      }
    });
  }
}

/**
 * An {@link AnimationUtil} that uses a material animation to hide the search bar.
 */
class MaterialAnimationUtil {
  @TargetApi(21)
  public static void reveal(
      final View view,
      final AnimationListener listener,
      final int revealPointXPx,
      final int revealPointYPx) {

    final int endRadius = Math.max(view.getWidth(), view.getHeight());

    doCircularReveal(view, listener, revealPointXPx, revealPointYPx, 0, endRadius);
  }

  @TargetApi(21)
  public static void conceal(
      final View view,
      final AnimationListener listener,
      final int concealPointXPx,
      final int concealPointYPx) {

    final int startRadius = Math.max(view.getWidth(), view.getHeight());

    doCircularReveal(view, listener, concealPointXPx, concealPointYPx, startRadius, 0);
  }

  @RequiresApi(21)
  private static void doCircularReveal(
      final View view,
      final AnimationListener listener,
      final int focalPointXPx,
      final int focalPointYPx,
      final int startRadius,
      final int endRadius) {

    final Animator animation = ViewAnimationUtils.createCircularReveal(
        view,
        focalPointXPx,
        focalPointYPx,
        startRadius,
        endRadius);

    animation.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(final Animator animation) {
        listener.onAnimationStart(view);
      }

      @Override
      public void onAnimationEnd(final Animator animation) {
        listener.onAnimationEnd(view);
      }

      @Override
      public void onAnimationCancel(final Animator animation) {
        listener.onAnimationCancel(view);
      }
    });

    animation.start();
  }
}