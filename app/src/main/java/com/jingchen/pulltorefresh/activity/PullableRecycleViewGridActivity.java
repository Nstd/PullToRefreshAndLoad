package com.jingchen.pulltorefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jingchen.pulltorefresh.R;
import com.jingchen.pulltorefresh.other.RefreshRecycleAdapter;
import com.jingchen.pulltorefreshandload.PullToRefreshLayout;
import com.jingchen.pulltorefreshandload.pullableview.PullableRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nstd on 2016/4/29.
 */
public class PullableRecycleViewGridActivity extends Activity {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RefreshRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        try {
            ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                    .setOnRefreshListener(new MyListener());
            recyclerView = (PullableRecycleView) findViewById(R.id.content_view);
            gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter = new RefreshRecycleAdapter(this, RefreshRecycleAdapter.LAYOUT_TYPE_GRID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addData(int type) {
        List<String> datas = new ArrayList<String>();
        String[] typeName = {"refreshed", "loaded"};
        for (int i = 0; i < 5; i++) {
            datas.add(typeName[type] + " item name " + (i + 1));
        }
        if(type == 0) {
            adapter.addItem(datas);
        } else {
            adapter.addMoreItem(datas);
        }
    }

    public class MyListener implements PullToRefreshLayout.OnRefreshListener
    {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
        {
            // 下拉刷新操作
            new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    addData(0);
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
        {
            // 加载操作
            new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    addData(1);
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);
        }

    }
}
