package org.synsystems.onlypass.framework.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class ClickableFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private var hasListener = false

  override fun setOnClickListener(listener: OnClickListener?) {
    super.setOnClickListener(listener)

    hasListener = listener != null
  }

  override fun onInterceptTouchEvent(ev: MotionEvent) = hasListener
}