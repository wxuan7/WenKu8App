/*
 * Copyright (C) 2014 Lucas Rocha
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

package com.wux.wenku.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wux.wenku.R;
import com.wux.wenku.ui.ReadTextView;

import java.util.ArrayList;
import java.util.List;

public class PageLayoutAdapter extends RecyclerView.Adapter<PageLayoutAdapter.SimpleViewHolder> {
    private static final int DEFAULT_ITEM_COUNT = 5;

    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;
    private String _FileUrl = null;
    private ReadTextView tv_content;

    private int[] imageIds = {R.mipmap.card_cover1, R.mipmap.card_cover2, R.mipmap.card_cover3,
            R.mipmap.card_cover4, R.mipmap.card_cover5, R.mipmap.card_cover6,
            R.mipmap.card_cover7, R.mipmap.card_cover8};

    public  class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ReadTextView title;
        public final ImageView myImage;

        public SimpleViewHolder(View view) {
            super(view);
            title = (ReadTextView) view.findViewById(R.id.tv_content);
            myImage = (ImageView) view.findViewById(R.id.iv_image);
            if(tv_content==null){
                tv_content=title;
            }
        }
    }

    public PageLayoutAdapter(Context context, RecyclerView recyclerView, String fileurl) {
        this(context, recyclerView, DEFAULT_ITEM_COUNT, fileurl);
    }


    public PageLayoutAdapter(Context context, RecyclerView recyclerView, int itemCount, String fileurl) {
        mContext = context;
        _FileUrl = fileurl;
        mItems = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            addItem(i);
        }
        if(tv_content!=null){

        }

        mRecyclerView = recyclerView;
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void getMore() {
        int size = getItemCount();
        for (int i = size; i < size + 5; i++) {
            addItem(i);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_page_content, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.title.setText("Index " + mItems.get(position));
        holder.myImage.setImageResource(imageIds[position % imageIds.length]);

        final View itemView = holder.itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
            }
        });
        final int itemId = mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public int[] getPage(TextView textView) {
        int count = textView.getLineCount();
//        textView.setText(mContent);
        int pCount = getPageLineCount(textView);//获取行数
        int pageNum = count / pCount;//获取页数
        int page[] = new int[pageNum];
        for (int i = 0; i < pageNum; i++) {
            page[i] = textView.getLayout().getLineEnd((i + 1) * pCount - 1);
        }
        return page;
    }

    /**
     * 计算textview能显示的文本行数
     *
     * @param view
     * @return
     */
    private int getPageLineCount(TextView view) {
        /*
        * The first row's height is different from other row.
         */
        int h = view.getBottom() - view.getTop() - view.getPaddingTop();
        int firstH = getLineHeight(0, view);
        int otherH = getLineHeight(1, view);
        return (h - firstH) / otherH + 1;

    }

    /**
     * getLineBounds得到每行的高度
     *
     * @param line
     * @param view
     * @return
     */
    private int getLineHeight(int line, TextView view) {
        Rect rect = new Rect();
        view.getLineBounds(line, rect);
        return rect.bottom - rect.top;
    }
}
