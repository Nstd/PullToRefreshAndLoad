package com.jingchen.pulltorefreshandload.pullableview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by maoting on 2016/4/28.
 */
public class PullableRecycleView extends RecyclerView implements Pullable
{
    public PullableConfig pullableConfig = new PullableConfig();

    public PullableRecycleView(Context context)
    {
        super(context);
    }
    public PullableRecycleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public PullableRecycleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown()
    {
        if(!pullableConfig.canUserPullDown())
        {
            return false;
        }

        return !ViewCompat.canScrollVertically(this, -1);

//        LayoutManager lm = getLayoutManager();
//        Adapter adapter = getAdapter();
//        int count = -1;
//
//        if(lm != null && adapter != null) {
//            count = adapter.getItemCount();
//            if (count == 0) {
//                // 没有item的时候也可以下拉刷新
//                return true;
//            } else {
//                if(lm instanceof  LinearLayoutManager) {
//                    return ((LinearLayoutManager) lm).findFirstVisibleItemPosition() == 0 &&
//                            lm.getChildAt(0).getTop() == 0;
//                } else if(lm instanceof GridLayoutManager) {
//                    return ((GridLayoutManager) lm).findFirstVisibleItemPosition() == 0 &&
//                            lm.getChildAt(0).getTop() == 0;
//                } else if(lm instanceof StaggeredGridLayoutManager) {
//                    int[] p = ((StaggeredGridLayoutManager) lm).findFirstVisibleItemPositions(null);
//                    if(p != null && p.length > 0) {
//                        for(int i = 0; i < p.length; i++) {
//                            if(p[i] == 0 && lm.getChildAt(0).getTop() == 0) {
//                                return true;
//                            }
//                        }
//                        return false;
//                    }
//                }
//            }
//        }
//        return false;
    }

    @Override
    public boolean canPullUp()
    {
        if(!pullableConfig.canUserPullUp()) {
            return false;
        }

//        return !ViewCompat.canScrollVertically(this, 1);

        LayoutManager lm = getLayoutManager();
        Adapter adapter = getAdapter();
        int count = -1;

        if(lm != null && adapter != null) {
            count = adapter.getItemCount();
            if (count == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else {
                // position 是 size - 1
                count --;
                int pHeight = getMeasuredHeight();
                int lastPosition;
                if(lm instanceof  LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) lm).findLastCompletelyVisibleItemPosition();
                    if(lastPosition == count) {
                        View child = lm.getChildAt(lastPosition - ((LinearLayoutManager) lm).findFirstVisibleItemPosition());
                        return child != null && child.getBottom() <= pHeight;
                    }
                } else if(lm instanceof GridLayoutManager) {
                    lastPosition = ((GridLayoutManager) lm).findLastCompletelyVisibleItemPosition();
                    if(lastPosition == count) {
                        View child = lm.getChildAt(lastPosition - ((GridLayoutManager) lm).findFirstVisibleItemPosition());
                        return child != null && child.getBottom() <= pHeight;
                    }
                } else if(lm instanceof StaggeredGridLayoutManager) {
                    int[] pFirst= ((StaggeredGridLayoutManager) lm).findFirstVisibleItemPositions(null);
                    int[] pLast = ((StaggeredGridLayoutManager) lm).findLastCompletelyVisibleItemPositions(null);
                    boolean isLastRow = false;
                    boolean isAllCanSee = true;
                    if(pLast != null && pLast.length > 0 && pFirst != null && pFirst.length > 0) {
                        for(int i = 0; i < pLast.length; i++) {
                            if(pLast[i] == count) {
                                isLastRow = true;
                            }
                            if(lm.getChildAt(pLast[i] - pFirst[i]).getBottom() > pHeight) {
                                isAllCanSee = false;
                            }
                        }
                        return isLastRow && isAllCanSee;
                    }
                }
            }
        }
        return false;
    }
}
