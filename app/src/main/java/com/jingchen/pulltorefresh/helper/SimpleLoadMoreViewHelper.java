package com.jingchen.pulltorefresh.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.R;

/**
 * Created by Nstd on 2015/11/19.
 */
public class SimpleLoadMoreViewHelper implements PullToRefreshLayout.OnLoadMoreViewStatusChangedListener{

    // ������ͷ��ת180�㶯��
    private RotateAnimation rotateAnimation;
    // ������ת����
    private RotateAnimation refreshingAnimation;
    // ����ͷ
    private View loadmoreView;
    // �����ļ�ͷ
    private View pullUpView;
    // ���ڼ��ص�ͼ��
    private View loadingView;
    // ���ؽ��ͼ��
    private View loadStateImageView;
    // ���ؽ�����ɹ���ʧ��
    private TextView loadStateTextView;

    protected Context mContext;

    public SimpleLoadMoreViewHelper(Activity context, int loadMoreViewResId) {
        // ��ʼ����������
        this.mContext = context;
        this.loadmoreView = context.findViewById(loadMoreViewResId); //loadmoreView;
        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView
                .findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);

        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // �������ת������
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    @Override
    public void onLoadMoreInitView(PullToRefreshLayout pullToRefreshLayout) {
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
