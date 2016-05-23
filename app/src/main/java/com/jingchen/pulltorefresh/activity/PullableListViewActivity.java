package com.jingchen.pulltorefresh.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingchen.pulltorefresh.MyAdapter;
import com.jingchen.pulltorefresh.MyListener;
import com.jingchen.pulltorefresh.R;
import com.jingchen.pulltorefreshandload.DefaultPullToRefreshLayout;
import com.jingchen.pulltorefreshandload.PullToRefreshLayout;
import com.jingchen.pulltorefreshandload.helper.DefaultLoadMoreViewHelper;
import com.jingchen.pulltorefreshandload.helper.DefaultRefreshViewHelper;
import com.jingchen.pulltorefreshandload.helper.SimpleLoadMoreViewHelper;
import com.jingchen.pulltorefreshandload.helper.SimpleRefreshViewHelper;
import com.jingchen.pulltorefreshandload.pullableview.PullableListView;

public class PullableListViewActivity extends Activity
{
	private final String TAG = "PullableListView";
	private ListView listView;
	private PullToRefreshLayout ptrl;
	private boolean isFirstIn = true;
	private TextView tvTips;
	private Button btnChanger;

	boolean isShowList = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview);
		ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
		ptrl.setOnRefreshListener(new MyListener());
//		ptrl.setOnRefreshViewStatusChangedListener(new DefaultRefreshViewHelper(this, findViewById(R.id.list_view_refresh_head)));
//		ptrl.setOnLoadMoreViewStatusChangedListener(new DefaultLoadMoreViewHelper(this, findViewById(R.id.list_view_load_more)));
		listView = (ListView) findViewById(R.id.content_view);
		tvTips = (TextView) findViewById(R.id.tv_no_data_label);
		btnChanger = (Button) findViewById(R.id.btn_changer);
		btnChanger.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isShowList = !isShowList;
				if(isShowList) {
					listView.setVisibility(View.VISIBLE);
					tvTips.setVisibility(View.GONE);
					((PullableListView) listView).pullableConfig.enablePull();
				} else {
					listView.setVisibility(View.GONE);
					tvTips.setVisibility(View.VISIBLE);
					((PullableListView) listView).pullableConfig.disablePullUp();
				}
			}
		});
		initListView();

		ptrl.setOnMoveListener(new PullToRefreshLayout.OnMoveListener() {
			@Override
			public void begin() {
				Log.e(TAG, "begin pull");
			}

			@Override
			public void move(float y) {
				Log.e(TAG, "pull y=" + y);

			}

			@Override
			public void stop() {
				Log.e(TAG, "stop pull");
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		// 第一次进入自动刷新
		if (isFirstIn)
		{
			ptrl.autoRefresh();
			isFirstIn = false;
		}
	}

	/**
	 * ListView初始化方法
	 */
	private void initListView()
	{
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < 30; i++)
		{
			items.add("这里是item " + i);
		}
		MyAdapter adapter = new MyAdapter(this, items);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(
						PullableListViewActivity.this,
						"LongClick on "
								+ parent.getAdapter().getItemId(position),
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(PullableListViewActivity.this,
						" Click on " + parent.getAdapter().getItemId(position),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
