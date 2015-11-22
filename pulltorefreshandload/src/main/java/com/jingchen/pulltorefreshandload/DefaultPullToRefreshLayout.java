package com.jingchen.pulltorefreshandload;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jingchen.pulltorefreshandload.helper.DefaultLoadMoreViewHelper;
import com.jingchen.pulltorefreshandload.helper.DefaultRefreshViewHelper;

/**
 * Created by Nstd on 2015/11/22 0022.
 */
public class DefaultPullToRefreshLayout extends PullToRefreshLayout {
    public DefaultPullToRefreshLayout(Context context) {
        super(context);
    }

    public DefaultPullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onRefreshViewGetted(View refreshView) {
        DefaultRefreshViewHelper helper = new DefaultRefreshViewHelper(this.mContext);
        helper.initView(refreshView);
        setOnRefreshViewStatusChangedListener(helper);
    }

    @Override
    protected void onLoadMoreViewGetted(View loadMoreView) {
        DefaultLoadMoreViewHelper helper = new DefaultLoadMoreViewHelper(this.mContext);
        helper.initView(loadMoreView);
        setOnLoadMoreViewStatusChangedListener(helper);
    }
}
