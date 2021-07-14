package com.qkl.online.mining.app.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.lazy.viewpager.fragment.LazyFragmentLazy;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.entity.AppCommonsConfig;
import com.qkl.online.mining.app.data.entity.BannerBean;
import com.qkl.online.mining.app.data.entity.DictConfig;
import com.qkl.online.mining.app.data.entity.HomeNews;
import com.qkl.online.mining.app.data.entity.IncomeBean;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.presenter.HomePresenter;
import com.qkl.online.mining.app.mvp.view.IHomeView;
import com.qkl.online.mining.app.ui.adapter.HomeNewsAdapter;
import com.qkl.online.mining.app.ui.view.VpSwipeRefreshLayout;
import com.qkl.online.mining.app.ui.view.marqueeview.MarqueeView;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.IntentUtil;
import com.qkl.online.mining.app.utils.glide.BannerImageLoader;
import com.qkl.online.mining.app.utils.suitlines.SuitLines;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * author：oyb on 2018/7/4 16:46
 * 首页
 */
public class HomeFragment extends LazyFragmentLazy<HomePresenter> implements IHomeView, BaseQuickAdapter.OnItemClickListener, OnBannerListener, MarqueeView.OnItemClickListener, View.OnClickListener {

    private static Handler mHandler = new Handler();

    private VpSwipeRefreshLayout mSwipeRefreshLayout;
    private Banner mBanner;
    private MarqueeView mMarqueeView;
    private SuitLines mSuitLines;
    private RecyclerView mRecyclerView;
    private TextView mDumpEnergyTxt;
    private TextView mSuanLiTxt;

    private LineChart mLineChart;

    private List<BannerBean.ListEntity> mBannerList;
    private List<HomeNews.ListEntity> mRollNewsList;
    private HomeNewsAdapter mHomeNewsAdapter;

    private int currentPage = 1;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(getActivity(), this);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_home);

        initView();
        initData();
    }

    protected void initView() {
        mSwipeRefreshLayout = (VpSwipeRefreshLayout) findViewById(R.id.fragment_home_refreshlayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.fragment_home_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //添加自定义分割线
        CommonsUtils.setDividerItemDecoration(mRecyclerView, R.drawable.custom_divider);

        // 设置转动颜色变化
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        // 刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 开始转动
                mSwipeRefreshLayout.setRefreshing(true);
                initData();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 停止转动
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        mHomeNewsAdapter = new HomeNewsAdapter();
        mRecyclerView.setAdapter(mHomeNewsAdapter);
        View headerView = View.inflate(getApplicationContext(), R.layout.header_home, null);
        mBanner = (Banner) headerView.findViewById(R.id.fragment_home_banner);
        mMarqueeView = (MarqueeView) headerView.findViewById(R.id.fragment_home_marqueeview);
        mDumpEnergyTxt = (TextView) headerView.findViewById(R.id.fragment_home_kc_dump_energy_txt);
        mSuanLiTxt = (TextView) headerView.findViewById(R.id.fragment_home_kc_suanli_txt);
        mSuitLines = (SuitLines) headerView.findViewById(R.id.fragment_home_ynm_chartline);
        mLineChart = (LineChart) headerView.findViewById(R.id.fragment_home_ynm_linechart);
        headerView.findViewById(R.id.fragment_home_kc_layout).setOnClickListener(this);
        headerView.findViewById(R.id.fragment_home_kc_suanli_layout).setOnClickListener(this);
        mHomeNewsAdapter.addHeaderView(headerView);
        // 加载更多
        mHomeNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getNews(currentPage);
            }
        }, mRecyclerView);
        mHomeNewsAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        // 获取banner
        mPresenter.getBanner();
        // 矿池实时数据接口
        mPresenter.getPoolConfig();
//        // ynm价格曲线
//        mPresenter.getYunExchangerate();
        // ynm价格折线图
        mPresenter.getYunPriceLine();
        // 获取新闻公告
        currentPage = 1;
        mPresenter.getNews(currentPage);
        // 滚动消息
        mPresenter.getRollNews();
    }

    // 首页Banner回调
    @Override
    public void resultBanner(BannerBean bannerBean) {
        if (bannerBean != null) {
            mBannerList = bannerBean.getList();
            if (mBannerList != null) {
                List<String> images = new ArrayList<>();
                for (BannerBean.ListEntity banner : mBannerList) {
                    images.add(banner.getImage());
                }
                mBanner.setImages(images)
                        .setIndicatorGravity(BannerConfig.RIGHT)
                        .setDelayTime(10000)
                        .setImageLoader(new BannerImageLoader())
                        .setOnBannerListener(this)
                        .start();
            }
        }
    }

    // 首页Banner点击
    @Override
    public void OnBannerClick(int position) {
        if (mBannerList != null && mBannerList.size() > 0) {
            BannerBean.ListEntity listEntity = mBannerList.get(position);
            String url = listEntity.getUrl();
            if (!TextUtils.isEmpty(url)) {
                String title = listEntity.getTitle();
                IntentUtil.ToWebViewActivity(getActivity(), title, url);
            }
        }
    }

    // 首页矿池能量以及总算力
    @Override
    public void resultPool(String yunAmount, String yuntHashrate) {
        mDumpEnergyTxt.setText(yunAmount);
        mSuanLiTxt.setText(yuntHashrate);
    }

    private void kCEnergy() {
        // 矿池储量
        String tokenHolderUrl = null;
        DictConfig dictConfig = AccountManager.getInstance().getDictConfig();
        if (dictConfig != null && !TextUtils.isEmpty(dictConfig.getTokenHolderUrl())) {
            tokenHolderUrl = dictConfig.getTokenHolderUrl();
        }
        if (TextUtils.isEmpty(tokenHolderUrl)) {
            AppCommonsConfig appCommonsConfig = AccountManager.getInstance().getAppCommonsConfig();
            if (appCommonsConfig != null) {
                tokenHolderUrl = appCommonsConfig.getTokenHolderUrl();
            }
        }
        if (!TextUtils.isEmpty(tokenHolderUrl)) {
            IntentUtil.ToWebViewActivity(getActivity(),
                    "YUN data", tokenHolderUrl);
        }
    }

    private void kCSuanLi() {
        // 矿池总算力
        String yuntListUrl = null;
        DictConfig dictConfig = AccountManager.getInstance().getDictConfig();
        if (dictConfig != null && !TextUtils.isEmpty(dictConfig.getYuntListUrl())) {
            yuntListUrl = dictConfig.getYuntListUrl();
        }
        if (TextUtils.isEmpty(yuntListUrl)) {
            AppCommonsConfig appCommonsConfig = AccountManager.getInstance().getAppCommonsConfig();
            if (appCommonsConfig != null) {
                yuntListUrl = appCommonsConfig.getYuntListUrl();
            }
        }
        if (!TextUtils.isEmpty(yuntListUrl)) {
            IntentUtil.ToWebViewActivity(getActivity(),
                    "YUNT data", yuntListUrl);
        }
    }

    // 首页新闻
    @Override
    public void resultNews(HomeNews homeNews) {
        if (homeNews == null) {
            mHomeNewsAdapter.loadMoreFail();
            currentPage--;
            if (currentPage <= 1) {
                currentPage = 1;
            }
        } else {
            List<HomeNews.ListEntity> homeNewsList = homeNews.getList();
            if (homeNewsList != null) {
                if (currentPage == 1) {
                    mHomeNewsAdapter.setNewData(homeNewsList);
                    mHomeNewsAdapter.loadMoreComplete();
                } else {
                    if (homeNewsList.size() > 0) {
                        mHomeNewsAdapter.addData(homeNewsList);
                        mHomeNewsAdapter.loadMoreComplete();
                    } else {
                        mHomeNewsAdapter.loadMoreEnd();
                    }
                }
                currentPage++;
            }
        }
    }

    // 首页新闻点击
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HomeNews.ListEntity listEntity = mHomeNewsAdapter.getItem(position);
        if (listEntity != null) {
            IntentUtil.ToWebViewActivity(getActivity(), listEntity.getTitle(), listEntity.getUrl());
        }
    }

    // 首页滚动通知
    @Override
    public void resultRollNews(HomeNews homeNews) {
        //设置集合
        List<String> items = new ArrayList<>();
        if (homeNews != null) {
            mRollNewsList = homeNews.getList();
            if (mRollNewsList != null && mRollNewsList.size() > 0) {
                for (HomeNews.ListEntity news : mRollNewsList) {
                    items.add(news.getTitle());
                }
            }
        }
//        mMarqueeView.setOnItemClickListener(this);
        mMarqueeView.startWithList(items);
    }

    // 首页滚动通知点击
    @Override
    public void onItemClick(int position, TextView textView) {
        HomeNews.ListEntity listEntity = mRollNewsList.get(position);
        if (listEntity != null) {
            IntentUtil.ToWebViewActivity(getActivity(), listEntity.getTitle(), listEntity.getUrl());
        }
    }

    // 首页价格曲线图-YNU汇率
    @Override
    public void resultYunExchangeRate(List lines) {
        mSuitLines.feed(lines);
    }

    private float[] mYScales;
    private List<IncomeBean> mIncomeBeanList;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例

    // 首页价格折线图-YNU汇率
    @Override
    public void resultYunPriceLine(List incomeBeanList, float[] yScales) {
        mIncomeBeanList = incomeBeanList;
        mYScales = yScales;

        if (mIncomeBeanList != null && mIncomeBeanList.size() > 0 &&
                mYScales != null && mYScales.length > 0) {
            initChart(mLineChart);
            showLineChart(incomeBeanList, Color.CYAN);
        }
        // 背景渐变
        setChartFillDrawable(getResources().getDrawable(R.drawable.fade_blue));
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            mLineChart.invalidate();
        }
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param color    曲线颜色
     */
    public void showLineChart(List<IncomeBean> dataList, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            IncomeBean data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, (float) data.getValue());
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, null);
        initLineDataSet(lineDataSet, color, null);
        LineData lineData = new LineData(lineDataSet);
        mLineChart.setData(lineData);
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setValueTextColor(CommonsUtils.getXmlColor(R.color.color_ffffff));
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    private int yIndex;

    private void initChart(LineChart lineChart) {
        yIndex = 0;
        lineChart.setDescription(null);
        //是否可以缩放、移动、触摸
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);

        //不能让缩放，不然有bug，所以接口也没实现
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(true);

        //设置图表距离上下左右的距离
        lineChart.setExtraOffsets(10, 10, 10, 0);

        //获取左侧侧坐标轴
        YAxis leftAxis = lineChart.getAxisLeft();

        //设置是否显示Y轴的值
        leftAxis.setDrawLabels(true);
        leftAxis.setTextColor(this.getResources().getColor(R.color.color_D3AA64));

        //设置所有垂直Y轴的的网格线是否显示
        leftAxis.setDrawGridLines(true);

        //设置虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        //设置Y极值，我这里没设置最大值，因为项目需要没有设置最大值
        leftAxis.setAxisMinimum(0f);

        //将右边那条线隐藏
        lineChart.getAxisRight().setEnabled(false);
        //获取X轴
        xAxis = lineChart.getXAxis();
        //设置X轴的位置，可上可下
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //将垂直于X轴的网格线隐藏，将X轴显示
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(mIncomeBeanList.size(), true);

        //设置X轴上lable颜色和大小
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.GRAY);

        //设置X轴高度
        xAxis.setAxisLineWidth(1);

        /***XY轴的设置***/
        xAxis = mLineChart.getXAxis();
        leftYAxis = mLineChart.getAxisLeft();
        rightYaxis = mLineChart.getAxisRight();
        rightYaxis.setEnabled(false);
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        // x自定义
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mIncomeBeanList.get((int) value).getTradeDate();
            }
        });
        // y自定义
        leftYAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (yIndex >= mYScales.length) {
                    yIndex = 0;
                }
                String str = String.valueOf(mYScales[yIndex]);
                yIndex++;
                return str;
            }
        });

        float yMin = 0;
        float yMax = 0;
        float granularity = 0;
        for (float d : mYScales) {
            if (granularity == 0 && yMin != 0) {
                granularity = Math.abs(d - yMin);
            }
            if (yMin == 0 || d < yMin) {
                yMin = d;
            }
            if (yMax == 0 || d > yMax) {
                yMax = d;
            }
        }

        leftYAxis.setLabelCount(mYScales.length, true);
        leftYAxis.setAxisMinimum(yMin - granularity);
        leftYAxis.setAxisMaximum(yMax);

        // xy显示虚线
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        /***折线图例 标签 设置***/
        legend = mLineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.EMPTY);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
    }

    @OnClick(R.id.home_kefu_view)
    public void kefu() {
        IntentUtil.ToKefuActivity(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_home_kc_layout:
                kCEnergy();
                break;
            case R.id.fragment_home_kc_suanli_layout:
                kCSuanLi();
                break;
        }
    }

}
