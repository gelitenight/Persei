package com.gelitenight.persei;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * a view to show ripple animation
 */
public class RippleView extends View {
    private Point mCenter;
    private float mRadius;
    private Paint mPaint;

    public RippleView(Context context) {
        super(context);
        init();
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#88FFFFFF"));
        mPaint.setAntiAlias(true);
    }

    // start ripple animation
    public void ripple(Point centerPoint, int maxRadius) {
        mCenter = centerPoint;
        mRadius = 0;
        mPaint.setAlpha(255);

        final ValueAnimator animator = ValueAnimator.ofFloat(0, maxRadius);
        animator.setDuration(500);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadius = (float) animation.getAnimatedValue();
                mPaint.setAlpha((int) (255 * (1 - animation.getAnimatedFraction())));
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator.removeListener(this);
                mCenter = null;
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCenter != null) {
            canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
        }
    }
}
