package org.synsystems.onlypass.framework.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ClickableFrameLayout extends FrameLayout {
	private boolean onClickListenerCurrentlySet;

	public ClickableFrameLayout(final Context context) {
		super(context);
	}

	public ClickableFrameLayout(
			final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public ClickableFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@RequiresApi(21)
	@TargetApi(21)
	public ClickableFrameLayout(
			final Context context,
			final AttributeSet attrs,
			final int defStyleAttr,
			final int defStyleRes) {

		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setOnClickListener(final OnClickListener listener) {
		super.setOnClickListener(listener);

		onClickListenerCurrentlySet = listener != null;
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		return onClickListenerCurrentlySet;
	}
}