package org.synsystems.onlypass.framework.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import org.synsystems.onlypass.framework.R

class RoundedFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

  private var cornerRadiusPx: Float = 0f

  private var clipPath: Path = Path()

  private var clipBoundsF: RectF = RectF()

  init {
    clipBounds = Rect()
    clipChildren = true
    setWillNotDraw(false)
  }

  init {
    val parsedAttrs = context.obtainStyledAttributes(
        attrs,
        R.styleable.RoundedFrameLayout,
        defStyleAttr,
        defStyleRes)

    setCornerRadiusPx(parsedAttrs.getDimension(R.styleable.RoundedFrameLayout_rfl_corner_radius, 0f))

    parsedAttrs.recycle()
  }

  override fun onDraw(canvas: Canvas) {
    canvas.getClipBounds(clipBounds)

    clipBoundsF.set(
        clipBounds.left.toFloat(),
        clipBounds.top.toFloat(),
        clipBounds.right.toFloat(),
        clipBounds.bottom.toFloat())

    clipPath.reset()
    clipPath.addRoundRect(clipBoundsF, cornerRadiusPx, cornerRadiusPx, Path.Direction.CW)

    canvas.clipPath(clipPath)

    super.onDraw(canvas)
  }

  fun setCornerRadiusPx(cornerRadiusPx: Float) {
    this.cornerRadiusPx = cornerRadiusPx
    invalidate()
  }
}