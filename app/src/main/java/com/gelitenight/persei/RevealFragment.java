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
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

public class RevealFragment extends Fragment {

    private ItemAdapter mAdapter;

    public static RevealFragment newInstance(int iconRes) {
        Bundle args = new Bundle();
        args.putInt("icon", iconRes);
        RevealFragment fragment = new RevealFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // The coordinate of the center of the animating circle
    private Point mRevealPoint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new RevealTransition());
        mAdapter = new ItemAdapter(getArguments().getInt("icon"));
    }

    public void setRevealPoint(Point revealPoint) {
        this.mRevealPoint = revealPoint;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setTransitionGroup(true);  // must set it as transition group, or the reveal animation will perform on each single item of recyclerview
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    class RevealTransition extends Visibility {
        @Override
        public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                TransitionValues endValues) {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view,
                    mRevealPoint.x,
                    mRevealPoint.y,
                    0,
                    (float) Math.hypot(view.getHeight() - mRevealPoint.y, view.getWidth()));
            animator.setDuration(Constant.FRAGMENT_REVEAL_TIME);
            animator.setInterpolator(new AccelerateInterpolator(1.5f));
            return animator;
        }
    }
}
