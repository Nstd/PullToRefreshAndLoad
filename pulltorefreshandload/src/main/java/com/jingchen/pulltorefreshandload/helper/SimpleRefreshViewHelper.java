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

    // 下拉箭头的转180°动画
    protected RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;
    // 下拉头
    protected View refreshView;
    // 下拉的箭头
    protected View pullView;
    // 正在刷新的图标
    protected View refreshingView;
    // 刷新结果图标
    protected View refreshStateImageView;
    // 刷新结果：成功或失败
    protected TextView refreshStateTextView;

    protected Context mContext;

    public SimpleRefreshViewHelper(Activity context, int refreshViewResId) {
        // 初始化下拉布局
        this.mContext = context;
        this.refreshView = context.findViewById(refreshViewResId);
        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) refreshView
                .findViewById(R.id.state_tv);
        refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        refreshStateImageView = refreshView.findViewById(R.id.state_iv);

        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    @Override
    public void onRefreshInitView(PullToRefreshLayout pullToRefreshLayout) {
        refreshStateImageView.setVisibility(View.GONE);
        refreshStateTextView.setText(R.string.pull_to_refresh);
        pullView.clearAnimation();
        pullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefreshReleased(PullToRefreshLayout pullToRefreshLayout) {
        refreshStateTextView.setText(R.string.release_to_refresh);
        pullView.startAnimation(rotateAnimation);
    }

    @Override
    public void onRefreshing(PullToRefreshLayout pullToRefreshLayout) {
        pullView.clearAnimation();
        refreshingView.setVisibility(View.VISIBLE);
        pullView.setVisibility(View.INVISIBLE);
        refreshingView.startAnimation(refreshingAnimation);
        refreshStateTextView.setText(R.string.refreshing);
    }

    @Override
    public void onRefreshSuccess(PullToRefreshLayout pullToRefreshLayout) {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        refreshStateImageView.setVisibility(View.VISIBLE);
        refreshStateTextView.setText(R.string.refresh_succeed);
        refreshStateImageView
                .setBackgroundResource(R.drawable.refresh_succeed);
    }

    @Override
    public void onRefreshFailed(PullToRefreshLayout pullToRefreshLayout) {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        refreshStateImageView.setVisibility(View.VISIBLE);
        refreshStateTextView.setText(R.string.refresh_fail);
        refreshStateImageView
                .setBackgroundResource(R.drawable.refresh_failed);
    }
}
