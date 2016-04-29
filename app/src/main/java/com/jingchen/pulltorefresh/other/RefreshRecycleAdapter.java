package com.jingchen.pulltorefresh.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingchen.pulltorefresh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nstd on 2016/4/29.
 */
public class RefreshRecycleAdapter extends RecyclerView.Adapter<RefreshRecycleAdapter.ViewHolder> {

    public static final int LAYOUT_TYPE_LINEAR = 0;
    public static final int LAYOUT_TYPE_GRID   = 1;
    public static final int LAYOUT_TYPE_STAGGERED_GRID = 2;

    private LayoutInflater mInflater;
    private List<String> mTitles=null;
    private int mLayoutType;
    private Context mContext;

    public RefreshRecycleAdapter(Context context, int type){
        this.mInflater=LayoutInflater.from(context);
        this.mTitles=new ArrayList<String>();
        for (int i=0;i<20;i++){
            int index=i+1;
            mTitles.add("item"+index);
        }
        this.mLayoutType = type;
        this.mContext = context;
    }
    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = mLayoutType == LAYOUT_TYPE_LINEAR ? R.layout.item_recycleview_linear_layout : R.layout.item_recycleview_grid_layout;
        final View view =mInflater.inflate(layout,parent,false);
        //这边可以做一些属性设置，甚至事件监听绑定
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder=new ViewHolder(view);
        if(mLayoutType == LAYOUT_TYPE_STAGGERED_GRID) {
            viewHolder.padding = (int)Math.round(Math.random() * 100);
        }
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_tv.setText(mTitles.get(position));
        holder.itemView.setTag(position);
        if(mLayoutType == LAYOUT_TYPE_STAGGERED_GRID) {
            holder.item_tv.setPadding(0, holder.padding, 0, holder.padding);
            holder.item_tv.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_tv;
        public int padding;
        public ViewHolder(View view){
            super(view);
            item_tv = (TextView)view.findViewById(R.id.item_tv);
        }

        public void setPadding(int padding) {
            this.padding = padding;
        }
    }

    //添加数据
    public void addItem(List<String> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mTitles);
        mTitles.removeAll(mTitles);
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<String> newDatas) {
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }
}