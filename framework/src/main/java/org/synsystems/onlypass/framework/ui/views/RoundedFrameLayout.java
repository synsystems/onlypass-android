package org.synsystems.onlypass.framework.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.synsystems.onlypass.framework.R;

public class RoundedFrameLayout extends FrameLayout {
  private float cornerRadiusPx;

  private Path clipPath;

  private Rect clipBounds;

  private RectF clipBoundsF;

  public RoundedFrameLayout(final Context context) {
    super(context);
    init(null, 0, 0);
  }

  public RoundedFrameLayout(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0, 0);
  }

  public RoundedFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs, defStyleAttr, 0);
  }

  public RoundedFrameLayout(
      final Context context,
      final AttributeSet attrs,
      final int defStyleAttr,
      final int defStyleRes) {

    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    canvas.getClipBounds(clipBounds);

    clipBoundsF.set(clipBounds.left, clipBounds.top, clipBounds.right, clipBounds.bottom);

    clipPath.reset();
    clipPath.addRoundRect(clipBoundsF, cornerRadiusPx, cornerRadiusPx, Path.Direction.CW);

    canvas.clipPath(clipPath);

    super.onDraw(canvas);
  }

  public void setCornerRadiusPx(final float cornerRadiusPx) {
    this.cornerRadiusPx = cornerRadiusPx;
  }

  public void init(final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
    this.clipPath = new Path();
    this.clipBounds = new Rect();
    this.clipBoundsF = new RectF();

    final TypedArray parsedAttrs = getContext().obtainStyledAttributes(
        attrs,
        R.styleable.RoundedFrameLayout,
        defStyleAttr,
        defStyleRes);

    setCornerRadiusPx(parsedAttrs.getDimension(R.styleable.RoundedFrameLayout_rfl_corner_radius, 0));

    parsedAttrs.recycle();

    setClipChildren(true);
    setWillNotDraw(false);
  }
}