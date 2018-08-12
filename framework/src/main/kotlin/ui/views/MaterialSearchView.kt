package org.synsystems.onlypass.framework.ui.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff.Mode
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.miguelcatalan.materialsearchview.utils.AnimationUtil
import com.miguelcatalan.materialsearchview.utils.AnimationUtil.AnimationListener
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.subjects.PublishSubject
import org.synsystems.onlypass.framework.R
import org.synsystems.onlypass.framework.rxutils.Pulse

class MaterialSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialSearchView(context, attrs, defStyleAttr) {

  private val searchTerms = PublishSubject.create<String>()

  private val searchSubmitted = PublishSubject.create<Pulse>()

  private val searchOpened = PublishSubject.create<Pulse>()

  private val searchClosed = PublishSubject.create<Pulse>()

  private val searchLayout: View = findViewById(R.id.search_layout)

  private val searchTopBar: View = findViewById(R.id.search_top_bar)

  private val backButton: View = findViewById(R.id.action_up_btn)

  init {
    ContextCompat
        .getDrawable(context, R.drawable.icon_back_arrow_white)!!
        .apply { setColorFilter(Color.BLACK, Mode.SRC_IN) }
        .let { setBackIcon(it) }

    ContextCompat
        .getDrawable(context, R.drawable.ic_action_navigation_close)!!
        .apply { setColorFilter(Color.BLACK, Mode.SRC_IN) }
        .let { setCloseIcon(it) }

    setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        searchSubmitted.onNext(Pulse)
        hideKeyboard(this@MaterialSearchView)
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { searchTerms.onNext(it) }
        return true
      }
    })
  }

  fun observeSearchTerms() = searchTerms

  fun observeSearchSubmitted() = searchSubmitted

  fun observeSearchOpened() = searchOpened

  fun observeSearchClosed() = searchClosed

  override fun showSearch() = showSearchRx().blockingAwait()

  fun showSearchRx() = Completable.defer {
    if (isSearchOpen) {
      Completable.complete()
    } else {
      animateSearchOpening().doOnComplete { searchOpened.onNext(Pulse) }
    }
  }

  private fun animateSearchOpening() = Completable
      .create { emitter ->
        searchLayout.visibility = View.VISIBLE

        MaterialAnimationUtil.reveal(
            searchTopBar,
            routeAnimationEventsTo(emitter),
            width - 24,
            height / 2)
      }
      .doOnComplete { super.closeSearch() }

  override fun closeSearch() = closeSearchRx().blockingAwait()

  fun closeSearchRx() = Completable.defer {
    if (isSearchOpen) {
      animateSearchClosing().doOnComplete { searchClosed.onNext(Pulse) }
    } else {
      Completable.complete()
    }
  }

  private fun animateSearchClosing() = Completable
      .create { emitter ->
        searchLayout.visibility = View.INVISIBLE

        MaterialAnimationUtil.conceal(
            searchTopBar,
            routeAnimationEventsTo(emitter),
            (backButton.x + backButton.width / 2).toInt(),
            height / 2)
      }
      .doOnComplete { super.closeSearch() }

  private fun routeAnimationEventsTo(emitter: CompletableEmitter) = object : AnimationListener {
    override fun onAnimationStart(view: View) = false

    override fun onAnimationCancel(view: View): Boolean {
      emitter.onComplete()
      return false
    }

    override fun onAnimationEnd(view: View): Boolean {
      emitter.onComplete()
      return false
    }
  }

  /**
   * An [AnimationUtil] that uses a material reveal animation to show and hide the search bar.
   */
  internal object MaterialAnimationUtil {
    fun reveal(view: View, listener: AnimationListener, focalPointXPx: Int, focalPointYPx: Int) {
      val startRadius = 0
      val endRadius = Math.max(view.width, view.height)

      doCircularReveal(view, listener, focalPointXPx, focalPointYPx, startRadius, endRadius)
    }

    fun conceal(view: View, listener: AnimationListener, focalPointXPx: Int, focalPointYPx: Int) {
      val startRadius = Math.max(view.width, view.height)
      val endRadius = 0

      doCircularReveal(view, listener, focalPointXPx, focalPointYPx, startRadius, endRadius)
    }

    private fun doCircularReveal(
        view: View,
        listener: AnimationListener,
        focalPointXPx: Int,
        focalPointYPx: Int,
        startRadius: Int,
        endRadius: Int
    ) {

      val animation = ViewAnimationUtils.createCircularReveal(
          view,
          focalPointXPx,
          focalPointYPx,
          startRadius.toFloat(),
          endRadius.toFloat())

      animation.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
          listener.onAnimationStart(view)
        }

        override fun onAnimationEnd(animation: Animator) {
          listener.onAnimationEnd(view)
        }

        override fun onAnimationCancel(animation: Animator) {
          listener.onAnimationCancel(view)
        }
      })

      animation.start()
    }
  }
}