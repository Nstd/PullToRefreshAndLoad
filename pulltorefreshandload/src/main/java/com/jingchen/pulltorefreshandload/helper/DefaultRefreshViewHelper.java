package com.jingchen.pulltorefreshandload.helper;

import android.content.Context;
import android.view.LayoutInflater;
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
public class DefaultRefreshViewHelper extends SimpleRefreshViewHelper {

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

    public DefaultRefreshViewHelper(Context context) {
        super(context);

        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    public DefaultRefreshViewHelper(Context context, View view) {
        this(context);
        initView(view);
    }

    @Override
    public void initView(View refreshView) {

        this.refreshView = refreshView;
        this.pullView = refreshView.findViewById(R.id.pull_icon);
        this.refreshStateTextView = (TextView) refreshView
                .findViewById(R.id.state_tv);
        this.refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        this.refreshStateImageView = refreshView.findViewById(R.id.state_iv);
    }

    @Override
    public void onRefreshResetView(PullToRefreshLayout pullToRefreshLayout) {
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
