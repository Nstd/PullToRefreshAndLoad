package com.jingchen.pulltorefreshandload.helper;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
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
public class DefaultLoadMoreViewHelper extends SimpleLoadMoreViewHelper {

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;
    // 上拉头
    private View loadmoreView;
    // 上拉的箭头
    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果图标
    private View loadStateImageView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    public DefaultLoadMoreViewHelper(Context context) {
        super(context);
        // 初始化上拉布局
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    public DefaultLoadMoreViewHelper(Context context, View view) {
        this(context);
        initView(view);
    }

    @Override
    public void initView(View loadmoreView) {
        this.loadmoreView = loadmoreView;
        this.pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        this.loadStateTextView = (TextView) this.loadmoreView
                .findViewById(R.id.loadstate_tv);
        this.loadingView = loadmoreView.findViewById(R.id.loading_icon);
        this.loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
    }

    @Override
    public void onLoadMoreResetView(PullToRefreshLayout pullToRefreshLayout) {
        loadStateImageView.setVisibility(View.GONE);
        loadStateTextView.setText(R.string.pullup_to_load);
        pullUpView.clearAnimation();
        pullUpView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadMoreReleased(PullToRefreshLayout pullToRefreshLayout) {
        loadStateTextView.setText(R.string.release_to_load);
        pullUpView.startAnimation(rotateAnimation);
    }

    @Override
    public void onLoadingMore(PullToRefreshLayout pullToRefreshLayout) {
        pullUpView.clearAnimation();
        loadingView.setVisibility(View.VISIBLE);
        pullUpView.setVisibility(View.INVISIBLE);
        loadingView.startAnimation(refreshingAnimation);
        loadStateTextView.setText(R.string.loading);
    }

    @Override
    public void onLoadMoreSuccess(PullToRefreshLayout pullToRefreshLayout) {
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        loadStateImageView.setVisibility(View.VISIBLE);
        loadStateTextView.setText(R.string.load_succeed);
        loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
    }

    @Override
    public void onLoadMoreFailed(PullToRefreshLayout pullToRefreshLayout) {
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        loadStateImageView.setVisibility(View.VISIBLE);
        loadStateTextView.setText(R.string.load_fail);
        loadStateImageView.setBackgroundResource(R.drawable.load_failed);
    }
}
