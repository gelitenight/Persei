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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gelitenight.persei.ItemAdapter.VH;

import java.util.Random;

public class ItemAdapter extends RecyclerView.Adapter<VH> {
    private static final int[] sColors = new int[]{
            0xff8f01a1,
            0xff61d7e7,
            0xffdc7901,
            0xff6b5456,
            0xff04c732,
            0xff5c2344,
            0xff6964eb,
            0xff4e62ce,
            0xff25760c,
            0xff95332f,
            0xff46bffb
    };
    private int mIcon;
    private int[] mColors;

    public ItemAdapter(int iconRes) {
        mIcon = iconRes;

        Random random = new Random();
        mColors = new int[]{
                sColors[random.nextInt(sColors.length)],
                sColors[random.nextInt(sColors.length)],
                sColors[random.nextInt(sColors.length)]
        };
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mIcon, "position " + position, mColors[position % mColors.length]);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView mText;
        ImageView mIcon;

        public VH(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.image);
            mText = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(int iconRes, String content, int bgColor) {
            itemView.setBackgroundColor(bgColor);
            mIcon.setImageResource(iconRes);
            mText.setText(content);
        }
    }
}
