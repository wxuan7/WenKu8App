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
import com.wux.wenku.model.Novels;

import java.util.List;

/**
 * 小说详情页的推荐adpter
 */
public class RVRecommendAdapter extends BaseRecyclerAdapter<RVRecommendAdapter.ViewHolder> {
    private Context context;
    private List<Novels> mList;
    private View parentView = null;

    public RVRecommendAdapter(Context context, List<Novels> list) {
        this.context = context;
        this.mList = list;
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View parentView,View view, Novels novel, int position);

        void onItemLongClickListener(View parentView,View view, Novels novel, int position);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false);
        if(parentView==null){
            parentView = parent;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
//        holder.sdvRvItem.setImageURI(Uri.parse(stringList.get(position)));
//        holder.sdvRvItem.set
        ControllerListenerUtil.setControllerListener(holder.sdvRvItem, mList.get(position).getnCoverImgUrl(), MyUtils.getScreenWidth(context) >> 1, context);
        holder.tv_title.setText(mList.get(position).getnTitle());
        if (onItemClickListener != null) {
            holder.sdvRvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickListener(parentView,v, mList.get(position), position);
                }
            });
            holder.sdvRvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClickListener(parentView,v, mList.get(position), position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView sdvRvItem;
        private TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            sdvRvItem = (SimpleDraweeView) itemView.findViewById(R.id.sdv_cover);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
