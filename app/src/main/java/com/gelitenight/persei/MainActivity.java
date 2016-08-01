package com.gelitenight.persei;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TabHost.OnTabChangeListener;

import com.gelitenight.persei.TabLayout.OnTabSelectedListener;
import com.gelitenight.persei.TabLayout.Tab;

public class MainActivity extends AppCompatActivity {
    private static int[] icons = {
            R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5,
            R.drawable.a6, R.drawable.a7
    };

    private static String[] title = {
            "BUSINESS", "HEALTH", "TRAVEL", "FOOD", "SHOPPING",
            "FINANCIAL", "FASHION"
    };

    Toolbar mToolbar;
    AppBarLayout mAppBarLayout;
    TabLayout mTabIndicator;
    RevealTabHost mTabHost;

    /**
     * use this view to show fake click ripple effect
     */
    RippleView mRippleView;

    /**
     * record the point for ACTION_DOWN event which triggers tab change,
     * so we can use it to show ripple and reveal animation.
     */
    Point mRipplePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mTabHost = (RevealTabHost) findViewById(android.R.id.tabhost);
        mTabIndicator = (TabLayout) findViewById(R.id.indicator);
        mRippleView = (RippleView) findViewById(R.id.rippleView);

        // init toolbar
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppBarLayout.setExpanded(true, true);
            }
        });

        // expansion & collapse animation
        mAppBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mTabIndicator.setPivotY(appBarLayout.getHeight() / 2);
                mTabIndicator.setPivotX(appBarLayout.getWidth() / 2);
                float rotationX = (float) -verticalOffset / appBarLayout.getHeight() * 60;
                mTabIndicator.setRotationX(rotationX);
            }
        });

        // record the point for ACTION_DOWN event which triggers tab change
        mTabIndicator.setOnInterceptTouchEvent(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRipplePoint = new Point((int) event.getX(), (int) event.getY());
                }
                return false;
            }
        });

        mTabIndicator.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(final Tab tab) {
                if (mRipplePoint == null) return;

                // ripple animation
                mRippleView.ripple(mRipplePoint, mTabIndicator.getHeight() / 2);

                // show new tab with reveal animation
                mTabHost.setRevealPoint(
                        new Point(mRipplePoint.x, mRipplePoint.y - mTabIndicator.getHeight()));
                mTabHost.setCurrentTabByTag((String) tab.getTag());

                // collapse the mAppBarLayout when ripple animation is about to end
                mTabHost.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAppBarLayout.setExpanded(false, true);
                    }
                }, 400);
            }

            @Override
            public void onTabUnselected(Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(Tab tab) {
                // do nothing
            }
        });

        // setup tab indicator and content
        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                mToolbar.setTitle(s);
            }
        });
        for (int i = 0; i < icons.length; i++) {
            String tabTag = title[i];
            Bundle bundle = new Bundle();
            bundle.putInt("icon", icons[i]);

            mTabHost.addTab(mTabHost.newTabSpec(tabTag).setIndicator(tabTag),
                    RevealFragment.class, bundle);
            mTabIndicator.addTab(mTabIndicator.newTab().setTag(tabTag)
                    .setText(tabTag).setIcon(icons[i]).setCustomView(R.layout.category_item));
        }
    }
}
