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
public class SimpleRefreshViewHelper implements PullToRefreshLayout.OnRefreshViewStatusChangedListener {

    // ������ͷ��ת180�㶯��
    protected RotateAnimation rotateAnimation;
    // ������ת����
    private RotateAnimation refreshingAnimation;
    // ����ͷ
    protected View refreshView;
    // �����ļ�ͷ
    protected View pullView;
    // ����ˢ�µ�ͼ��
    protected View refreshingView;
    // ˢ�½��ͼ��
    protected View refreshStateImageView;
    // ˢ�½�����ɹ���ʧ��
    protected TextView refreshStateTextView;

    protected Context mContext;

    public SimpleRefreshViewHelper(Activity context, int refreshViewResId) {
        // ��ʼ����������
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
        // �������ת������
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
