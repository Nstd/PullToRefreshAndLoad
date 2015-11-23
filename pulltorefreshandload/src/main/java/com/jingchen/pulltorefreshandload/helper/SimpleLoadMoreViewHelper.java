package com.jingchen.pulltorefreshandload.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.jingchen.pulltorefreshandload.PullToRefreshLayout;
import com.jingchen.pulltorefreshandload.R;

/**
 * Created by Nstd on 2015/11/19.
 */
public class SimpleLoadMoreViewHelper implements PullToRefreshLayout.OnLoadMoreViewStatusChangedListener{

    protected Context mContext;

    public SimpleLoadMoreViewHelper(Context context) {
        this.mContext = context;
    }

    @Override
    public void initView(View loadMoreView) {}

    @Override
    public void onLoadMoreResetView(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onLoadMoreReleased(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onLoadingMore(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onLoadMoreSuccess(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onLoadMoreFailed(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onNoMoreData(PullToRefreshLayout pullToRefreshLayout) {}
}
