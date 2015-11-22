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
public class SimpleRefreshViewHelper implements PullToRefreshLayout.OnRefreshViewStatusChangedListener {

    protected Context mContext;

    public SimpleRefreshViewHelper(Context context) {
        this.mContext = context;
    }

    @Override
    public void initView(View refreshView) {}

    @Override
    public void onRefreshResetView(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onRefreshReleased(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onRefreshing(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onRefreshSuccess(PullToRefreshLayout pullToRefreshLayout) {}

    @Override
    public void onRefreshFailed(PullToRefreshLayout pullToRefreshLayout) {}
}
