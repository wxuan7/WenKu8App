package com.wux.wenku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fanyafeng.wenku.util.ControllerListenerUtil;
import com.fanyafeng.wenku.util.MyUtils;
import com.wux.wenku.R;

import java.util.List;

/**
 * Created by 365rili on 16/6/14.
 */
public class RVAdapter extends BaseRecyclerAdapter<RVAdapter.ViewHolder> {
    private Context context;
    private List<String> stringList;
    private int type;//类别，1 图片；2 文本

    public RVAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
        this.type = 1;
    }

    public RVAdapter(Context context, List<String> stringList, int type) {
        this.context = context;
        this.stringList = stringList;
        this.type = type;
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, String string, int position);

        void onItemLongClickListener(View view, String string, int position);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = null;
        if (type == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_rv_layout, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_catalog, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
//        holder.sdvRvItem.setImageURI(Uri.parse(stringList.get(position)));
//        holder.sdvRvItem.set
        if (type == 1) {
            ControllerListenerUtil.setControllerListener(holder.sdvRvItem, stringList.get(position), MyUtils.getScreenWidth(context) >> 1, context);
            if (onItemClickListener != null) {
                holder.sdvRvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClickListener(v, stringList.get(position), position);
                    }
                });
                holder.sdvRvItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onItemLongClickListener(v, stringList.get(position), position);
                        return false;
                    }
                });
            }
        } else {
            holder.tv_catalog.setText(stringList.get(position));
            holder.tv_catalog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickListener(v, stringList.get(position), position);
                }
            });
            holder.tv_catalog.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClickListener(v, stringList.get(position), position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return stringList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdvRvItem;
        private TextView tv_catalog;

        public ViewHolder(View itemView) {
            super(itemView);
            sdvRvItem = (SimpleDraweeView) itemView.findViewById(R.id.sdvRvItem);
            tv_catalog = (TextView) itemView.findViewById(R.id.tv_catalog);
        }
    }
}
