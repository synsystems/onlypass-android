package org.synsystems.onlypass.framework.ui.viewextensions

import android.graphics.PorterDuff
import android.support.v7.widget.Toolbar
import io.reactivex.Completable

fun Toolbar.setOverflowIconColor(color: Int) = Completable.fromRunnable {
  overflowIcon?.apply { this.setColorFilter(color, PorterDuff.Mode.MULTIPLY) }
}