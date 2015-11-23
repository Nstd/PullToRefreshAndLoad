package com.jingchen.pulltorefreshandload;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jingchen.pulltorefreshandload.pullableview.Pullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 * 
 * @author 陈靖
 */
public class PullToRefreshLayout extends RelativeLayout
{
	public static final String TAG = "PullToRefreshLayout";
	// 初始状态
	public static final int INIT = 0;
	// 释放刷新
	public static final int RELEASE_TO_REFRESH = 1;
	// 正在刷新
	public static final int REFRESHING = 2;
	// 释放加载
	public static final int RELEASE_TO_LOAD = 3;
	// 正在加载
	public static final int LOADING = 4;
	// 操作完毕
	public static final int DONE = 5;
	// 默认的刷新完成的停留时间
	public static final int DEFAUTL_REFRESH_FINISHED_VIEW_REMAIN_TIME_IN_MILLISECOND = 1000;
	// 当前状态
	private int state = INIT;
	// 刷新回调接口
	private OnRefreshListener mListener;
	// 下拉刷新view状态更新接口
	private OnRefreshViewStatusChangedListener mRefreshViewChangedListener;
	// 上拉加载view状态更新接口
	private OnLoadMoreViewStatusChangedListener mLoadMoreViewChangedListener;
	// 刷新成功
	public static final int SUCCEED = 0;
	// 刷新失败
	public static final int FAIL = 1;
	// 没有数据了
	public static final int NO_MORE_DATA = 2;
	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY;

	// 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
	public float pullDownY = 0;
	// 上拉的距离
	private float pullUpY = 0;

	// 释放刷新的距离
	private float refreshDist = 200;
	// 释放加载的距离
	private float loadmoreDist = 200;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 第一次执行布局
	private boolean isLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;

//	// 下拉头
	private View refreshView;
//	// 上拉头
	private View loadmoreView;

	// 实现了Pullable接口的View
	private View pullableView;
	// 过滤多点触碰
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	private boolean canUserPullDown = true;
	private boolean canUserPullUp = true;
	protected Context mContext;
	private int mFinishedViewRemainTimeInMillisecond = DEFAUTL_REFRESH_FINISHED_VIEW_REMAIN_TIME_IN_MILLISECOND;

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch)
			{
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDist)
				{
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadmoreDist)
				{
					pullUpY = -loadmoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0)
				pullDownY -= MOVE_SPEED;
			else if (pullUpY < 0)
				pullUpY += MOVE_SPEED;
			if (pullDownY < 0)
			{
				// 已完成回弹
				pullDownY = 0;
//				pullView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
				requestLayout();
			}
			if (pullUpY > 0)
			{
				// 已完成回弹
				pullUpY = 0;
//				pullUpView.clearAnimation();
				// 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING)
					changeState(INIT);
				timer.cancel();
				requestLayout();
			}
			Log.d("handle", "handle");
			// 刷新布局,会自动调用onLayout
			requestLayout();
			// 没有拖拉或者回弹完成
			if (pullDownY + Math.abs(pullUpY) == 0)
				timer.cancel();
		}

	};

	/**
	 * 设置加载完成以后，提示的停留时间，默认1000ms
	 * @param timeInMillisecond
	 */
	public void setFinishedViewRemainTime(int timeInMillisecond) {
		this.mFinishedViewRemainTimeInMillisecond = timeInMillisecond;
	}

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		mListener = listener;
	}

	public void setOnRefreshViewStatusChangedListener(OnRefreshViewStatusChangedListener listener) {
		mRefreshViewChangedListener = listener;
	}

	public void setOnLoadMoreViewStatusChangedListener(OnLoadMoreViewStatusChangedListener listener) {
		mLoadMoreViewChangedListener = listener;
	}

	public PullToRefreshLayout(Context context)
	{
		super(context);
		initView(context, null);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}

	public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs)
	{
		mContext = context;
		timer = new MyTimer(updateHandler);
	}

	private void hide()
	{
		timer.schedule(5);
	}

	/**
	 * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
	 */
	/**
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void refreshFinish(int refreshResult)
	{
		switch (refreshResult)
		{
		case SUCCEED:
			// 刷新成功
			if(mRefreshViewChangedListener != null) mRefreshViewChangedListener.onRefreshSuccess(this);
			break;
		case NO_MORE_DATA:
			//没有更多数据了
			if(mRefreshViewChangedListener != null) mRefreshViewChangedListener.onNoMoreData(this);
			break;
		case FAIL:
		default:
			// 刷新失败
			if(mRefreshViewChangedListener != null) mRefreshViewChangedListener.onRefreshFailed(this);
			break;
		}

		if (pullDownY > 0)
		{
			sendRefreshFinishedMessage();
		}
		else
		{
			changeState(DONE);
			hide();
		}
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 * 
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void loadmoreFinish(int refreshResult)
	{
//		loadingView.clearAnimation();
//		loadingView.setVisibility(View.GONE);
		switch (refreshResult)
		{
		case SUCCEED:
			// 加载成功
			if(mLoadMoreViewChangedListener != null) mLoadMoreViewChangedListener.onLoadMoreSuccess(this);
			break;
		case NO_MORE_DATA:
			//没有更多数据了
			if(mLoadMoreViewChangedListener != null) mLoadMoreViewChangedListener.onNoMoreData(this);
			break;
		case FAIL:
		default:
			// 加载失败
			if(mLoadMoreViewChangedListener != null) mLoadMoreViewChangedListener.onLoadMoreFailed(this);
			break;
		}

		if (pullUpY < 0)
		{
			sendRefreshFinishedMessage();
		}
		else
		{
			changeState(DONE);
			hide();
		}
	}

	private void sendRefreshFinishedMessage() {
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				changeState(DONE);
				hide();
			}
		};
		if(mFinishedViewRemainTimeInMillisecond > 0) {
			handler.sendEmptyMessageDelayed(0, mFinishedViewRemainTimeInMillisecond);
		} else {
			handler.sendEmptyMessage(0);
		}
	}

	private void changeState(int to)
	{
		state = to;
		switch (state)
		{
		case INIT:
			// 下拉布局初始状态
			if(mRefreshViewChangedListener != null) mRefreshViewChangedListener.onRefreshResetView(this);
			// 上拉布局初始状态
			if(mLoadMoreViewChangedListener != null) mLoadMoreViewChangedListener.onLoadMoreResetView(this);
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			if(mRefreshViewChangedListener != null) mRefreshViewChangedListener.onRefreshReleased(this);
			break;
		case REFRESHING:
			// 正在刷新状态
			if(mRefreshViewChangedListener != null) mRefreshViewChangedListener.onRefreshing(this);
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			if(mLoadMoreViewChangedListener != null) mLoadMoreViewChangedListener.onLoadMoreReleased(this);
			break;
		case LOADING:
			// 正在加载状态
			if(mLoadMoreViewChangedListener != null) mLoadMoreViewChangedListener.onLoadingMore(this);
			break;
		case DONE:
			// 刷新或加载完毕，啥都不做
			break;
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull()
	{
		canPullDown = true;
		canPullUp = true;
	}

	/*
	 * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0)
			{
				if (pullDownY > 0
						|| (((Pullable) pullableView).canPullDown()
								&& canPullDown && canUserPullDown && state != LOADING))
				{
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0)
					{
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == REFRESHING)
					{
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (pullUpY < 0
						|| (((Pullable) pullableView).canPullUp() && canPullUp && canUserPullUp && state != REFRESHING))
				{
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0)
					{
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight())
						pullUpY = -getMeasuredHeight();
					if (state == LOADING)
					{
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			if (pullDownY > 0 || pullUpY < 0)
				requestLayout();
			if (pullDownY > 0)
			{
				if (pullDownY <= refreshDist
						&& (state == RELEASE_TO_REFRESH || state == DONE))
				{
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(INIT);
				}
				if (pullDownY >= refreshDist && state == INIT)
				{
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(RELEASE_TO_REFRESH);
				}
			} else if (pullUpY < 0)
			{
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadmoreDist
						&& (state == RELEASE_TO_LOAD || state == DONE))
				{
					changeState(INIT);
				}
				// 上拉操作
				if (-pullUpY >= loadmoreDist && state == INIT)
				{
					changeState(RELEASE_TO_LOAD);
				}

			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8)
			{
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
			// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
			{
				isTouch = false;
			}
			if (state == RELEASE_TO_REFRESH)
			{
				changeState(REFRESHING);
				// 刷新操作
				if (mListener != null)
					mListener.onRefresh(this);
			} else if (state == RELEASE_TO_LOAD)
			{
				changeState(LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	/**
	 * @author chenjing 自动模拟手指滑动的task
	 * 
	 */
	private class AutoRefreshAndLoadTask extends
			AsyncTask<Integer, Float, String>
	{

		@Override
		protected String doInBackground(Integer... params)
		{
			while (pullDownY < 4 / 3 * refreshDist)
			{
				pullDownY += MOVE_SPEED;
				publishProgress(pullDownY);
				try
				{
					Thread.sleep(params[0]);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			changeState(REFRESHING);
			// 刷新操作
			if (mListener != null)
				mListener.onRefresh(PullToRefreshLayout.this);
			hide();
		}

		@Override
		protected void onProgressUpdate(Float... values)
		{
			if (pullDownY > refreshDist)
				changeState(RELEASE_TO_REFRESH);
			requestLayout();
		}

	}

	/**
	 * 自动刷新
	 */
	public void autoRefresh()
	{
		if(canUserPullDown) {
			AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
			task.execute(20);
		}
	}

	/**
	 * 自动加载
	 */
	public void autoLoad()
	{
		if(canUserPullUp) {
			pullUpY = -loadmoreDist;
			requestLayout();
			changeState(LOADING);
			// 加载操作
			if (mListener != null)
				mListener.onLoadMore(this);
		}
	}

	private void initView()
	{
		// 初始化下拉布局
		if(mRefreshViewChangedListener != null) {
			mRefreshViewChangedListener.onRefreshResetView(this);
		}
		// 初始化上拉布局
		if(mLoadMoreViewChangedListener != null) {
			mLoadMoreViewChangedListener.onLoadMoreResetView(this);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		Log.d("Test", "Test");
		if (!isLayout)
		{
			// 这里是第一次进来的时候做一些初始化
			int idx = 0, count = getChildCount();
			View v = getChildAt(0);
			if(!(v instanceof Pullable)) {
				// 有下拉头
				refreshView = v;
				refreshDist = ((ViewGroup) refreshView).getChildAt(0)
						.getMeasuredHeight();
				canUserPullDown = true;
				onRefreshViewGetted(refreshView);
			} else {
				canUserPullDown = false;
				pullableView = v;
			}

			if(count > 1) {
				v = getChildAt(1);
				if(!(v instanceof Pullable)) {
					// 没有下拉头，但是有上拉头
					loadmoreView = v;
					loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
							.getMeasuredHeight();
					canUserPullUp = true;
					onLoadMoreViewGetted(loadmoreView);
				} else {
					canUserPullUp = false;
					pullableView = v;

					if(count == 3 && pullableView != null) {
						// 有下拉头，也有上拉头
						loadmoreView = getChildAt(2);
						loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
								.getMeasuredHeight();
						canUserPullUp = true;
						onLoadMoreViewGetted(loadmoreView);
					} else {
						canUserPullUp = false;
					}
				}
			}

			isLayout = true;
			initView();
		}
		Log.e(TAG, "canUserPullDown=" + canUserPullDown + " refreshView not null=" + (refreshView != null)
			+ " canUserPullUp=" + canUserPullUp + " loadMoreView not null=" + (loadmoreView != null));
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		if (canUserPullDown) {
			refreshView.layout(0,
					(int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
					refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		}
		pullableView.layout(0, (int) (pullDownY + pullUpY),
				pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ pullableView.getMeasuredHeight());
		if (canUserPullUp) {
			loadmoreView.layout(0,
					(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
					loadmoreView.getMeasuredWidth(),
					(int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
							+ loadmoreView.getMeasuredHeight());
		}
	}

	protected void onRefreshViewGetted(View refreshView) {}

	protected void onLoadMoreViewGetted(View loadMoreView) {}

	class MyTimer
	{
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler)
		{
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period)
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel()
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask
		{
			private Handler handler;

			public MyTask(Handler handler)
			{
				this.handler = handler;
			}

			@Override
			public void run()
			{
				handler.obtainMessage().sendToTarget();
			}

		}
	}

	/**
	 * 刷新加载回调接口
	 * 
	 * @author chenjing
	 * 
	 */
	public interface OnRefreshListener
	{
		/**
		 * 刷新操作
		 */
		void onRefresh(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 加载操作
		 */
		void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
	}


	/**
	 * 下拉刷新时view的更改接口
	 */
	public interface OnRefreshViewStatusChangedListener {
		/**
		 * 初始化布局（加载资源）
		 */
		void initView(View refreshView);
		/**
		 * 重置下拉状态
		 */
		void onRefreshResetView(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 释放下拉状态
		 */
		void onRefreshReleased(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 下拉正在刷新
		 */
		void onRefreshing(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 下拉刷新成功
		 */
		void onRefreshSuccess(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 下拉刷新失败
		 */
		void onRefreshFailed(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 没有更多数据了
		 */
		void onNoMoreData(PullToRefreshLayout pullToRefreshLayout);
	}

	/**
	 * 上拉刷新时view的更改接口
	 */
	public interface OnLoadMoreViewStatusChangedListener {
		/**
		 * 初始化布局（加载资源）
		 */
		void initView(View loadMoreView);
		/**
		 * 重置上拉状态
		 */
		void onLoadMoreResetView(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 释放上拉状态
		 */
		void onLoadMoreReleased(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 上拉正在刷新
		 */
		void onLoadingMore(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 上拉刷新成功
		 */
		void onLoadMoreSuccess(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 上拉刷新失败
		 */
		void onLoadMoreFailed(PullToRefreshLayout pullToRefreshLayout);

		/**
		 * 没有更多数据了
		 */
		void onNoMoreData(PullToRefreshLayout pullToRefreshLayout);
	}
}
