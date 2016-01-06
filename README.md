PullToRefreshAndLoad
====================

Android下拉刷新上拉加载控件，对所有View通用！ 
这是一个演示如何使用通用的下拉刷新上拉加载控件demo，demo中已经实现了常见的需要上下拉功能的控件，其他控件如果需要加入这两个功能可自行扩展，实现Pullable接口即可，
具体的实现原理分析可以参见我的博客[http://blog.csdn.net/zhongkejingwang/article/details/38868463](http://blog.csdn.net/zhongkejingwang/article/details/38868463)


Nstd 补充说明：

    1. 在陈靖的基础上剥离了header view以及footer view的状态改变逻辑，便于扩展
    2. 将aar包上传到jcenter中，方便引用

##demo截图
###demo首页也是一个可以上拉下拉的ListView
![demo首页](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/main.gif)
###ListView:
![ListView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/ListView.gif)
###GridView:
![GridView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/GridView.gif)
###ExpandableListView:
![ExpandableListView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/ExpandableListView.gif)
###ScrollView:
![ScrollView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/ScrollView.gif)
###WebView:
![WebView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/WebView.gif)
###ImageView:
![ImageView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/ImageView.gif)
###TextView:
![TextView](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/TextView.gif)
###ListView With Container
![ListView With Container](https://github.com/Nstd/PullToRefreshAndLoad/blob/master/screenshots/ListViewWithContainer.gif)

##引用
###Gradle
``` gradle
compile 'com.jingchen:pulltorefreshandload:3.1@aar'
```

##说明
- 布局结构
``` structure
┌------------------------------┐
|       PullToRefreshLayout    |
|┌----------------------------┐|
||    Header view (Optional)  ||
|└----------------------------┘|
|┌----------------------------┐|
||                            ||
||  View implements Pullable  ||
||                            ||
|└----------------------------┘|
|┌----------------------------┐|
||    Footer view (Optional)  ||
|└----------------------------┘|
└------------------------------┘
```

- 使用注意
 1. 如果直接使用 PullToRefreshLayout，需要在 activity 的 onCreate() 方法中进行 helper 的初始化

    ``` java
    /*
    假设 MyRefreshViewHelper 继承自 SimpleRefreshViewHelper
    (或者实现了 PullToRefreshLayout.OnRefreshViewStatusChangedListener 接口)
    并重写了相应的方法
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        MyRefreshViewHelper helper = new MyRefreshViewHelper(this);
        helper.initView(this.findViewById(R.id.your_header_view_id));
        pullToRefreshLayout.setOnRefreshViewStatusChangedListener(helper);
        ...
    }
    ```

 2. 或者可以像 DefaultPullToRefreshLayout 那样继承 PullToRefreshLayout，重写以下两个方法，进行 header 和 footer 的初始化，减少重复调用

    ``` java
    // in DefaultPullToRefreshLayout
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

    //in activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        pullToRefreshLayout.setOnRefreshListener(new MyListener());
        ...
    }
    ```

 3. 以下是已经实现Pullable的常用控件，如果需求不够可以自己实现Pullable接口

    ``` java
    PullableExpandableListView
    PullableGridView
    PullableImageView
    PullableListView
    PullableScrollView
    PullableTextView
    PullableWebView
    ```

 4. header view 状态改变接口说明(footer view类似)

    ``` java
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
         * 没有更多数据
         */
        void onNoMoreData(PullToRefreshLayout pullToRefreshLayout);
    }
    ```

    注：

        1. SimpleRefreshViewHelper已经实现以上接口，如果不想重写所有方法，只要继承该类即可
        2. DefaultRefreshViewHelper实现了一个默认下拉样式
           对应的layout是：pull_to_refresh_header
        3. DefaultLoadMoreViewHelper实现了一个默认的上拉样式
           对应的layout是：pull_to_refresh_footer

- 更新说明
### v3.1.4
    1. xml中增加autoDetect(boolean, default:true)、hasHeader(boolean, deafult:false)、hasFooter(boolean, default:false)属性
        autoDetect默认为true，可以自动侦测[Header]-PullableView-[Footer]结构的布局
        如果autoDetect=false，需要设置hasHeader、hasFooter属性，来指明是否有刷新头部以及尾部，同时还能添加形如以下的结构：
        [Header]-{Container(LinearLayout、Relativelayout...){PullableView(必须是第一个孩子节点)-[OtherView]}}-[Footer]
    2. 所有默认的PullableView中添加pullableConfig属性，可以手动设置是否可以上下拉
    
##用法
### XML(auto detect header and footer)
``` xml
<com.jingchen.pulltorefreshandload.DefaultPullToRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/refresh_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <include layout="@layout/refresh_head" />

    <!-- 支持所有实现Pullable接口的View -->
    <com.jingchen.pulltorefreshandload.pullableview.PullableListView
        android:id="@+id/content_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/white"
        android:divider="@color/gray"
        android:dividerHeight="1dp" />

    <include layout="@layout/load_more" />

</com.jingchen.pulltorefreshandload.DefaultPullToRefreshLayout>
```

### XML(pullable container)
``` xml
<com.jingchen.pulltorefreshandload.DefaultPullToRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/refresh_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:autoDetect="false"
    app:hasHeader="true"
    app:hasFooter="true"
    >

    <include layout="@layout/refresh_head" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        <!-- 支持所有实现Pullable接口的View -->
        <com.jingchen.pulltorefreshandload.pullableview.PullableListView
            android:id="@+id/content_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/white"
            android:divider="@color/gray"
            android:dividerHeight="1dp" />
        <TextView
            android:id="@+id/tv_no_data_label"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="No Data"
            android:textSize="20dp"
            android:visibility="gone"
            />
    </LinearLayout>

    <include layout="@layout/load_more" />

</com.jingchen.pulltorefreshandload.DefaultPullToRefreshLayout>
```

### JAVA
``` java
public class PullableListViewActivity extends Activity
{
    private ListView listView;
    private PullToRefreshLayout ptrl;
    private boolean isFirstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        /*
        MyListener() 实现了 PullToRefreshLayout.OnRefreshListener 接口
        当完成数据请求操作以后，需要在onRefresh() (或 onLoadMore()) 中调用:
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            (或 pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);)
        或者
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            (或 pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);)
        告诉PullToRefreshLayout刷新结果
        */
        ptrl.setOnRefreshListener(new MyListener());
        listView = (ListView) findViewById(R.id.content_view);
        initListView();
    }

    ...
}
```