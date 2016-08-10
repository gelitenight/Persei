/*
 * Copyright (C) 2016, gelitenight(gelitenight@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gelitenight.persei;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
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
    public void ripple(Point centerPoint, int maxRadius, final AnimatorListener listener) {
        mCenter = centerPoint;
        mRadius = 0;
        mPaint.setAlpha(255);

        ValueAnimator rippleAnim = ValueAnimator.ofFloat(0, maxRadius);
        rippleAnim.setDuration(500);
        rippleAnim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRadius = (float) animation.getAnimatedValue();
                mPaint.setAlpha((int) (255 * (1 - animation.getAnimatedFraction())));
                invalidate();
            }
        });
        rippleAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (listener != null) listener.onAnimationStart(animator);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeListener(this);
                mCenter = null;
                if (listener != null) listener.onAnimationEnd(animator);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animator.removeListener(this);
                mCenter = null;
                if (listener != null) listener.onAnimationCancel(animator);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                if (listener != null) listener.onAnimationRepeat(animator);
            }
        });
        rippleAnim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCenter != null) {
            canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
        }
    }
}
