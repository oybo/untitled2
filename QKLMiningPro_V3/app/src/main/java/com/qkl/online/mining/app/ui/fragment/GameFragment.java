package com.qkl.online.mining.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.Constants;
import com.qkl.online.mining.app.data.commons.UrlConfig;
import com.qkl.online.mining.app.data.entity.BannerBean;
import com.qkl.online.mining.app.data.entity.GameBean;
import com.qkl.online.mining.app.data.event.EventBase;
import com.qkl.online.mining.app.listener.OnDialogOkListenner;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.presenter.GamePresenter;
import com.qkl.online.mining.app.mvp.view.IGameView;
import com.qkl.online.mining.app.ui.BaseFragment;
import com.qkl.online.mining.app.ui.adapter.GameAdapter;
import com.qkl.online.mining.app.ui.view.DialogTips;
import com.qkl.online.mining.app.ui.view.VpSwipeRefreshLayout;
import com.qkl.online.mining.app.utils.IntentUtil;
import com.qkl.online.mining.app.utils.glide.BannerImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author：oyb on 2018/7/4 16:46
 */
public class GameFragment extends BaseFragment<GamePresenter> implements IGameView, BaseQuickAdapter.OnItemClickListener, OnBannerListener {

    private static Handler mHandler = new Handler();
    private static final long REFRESH_TIME = 5 * 60000;
    private boolean startOnly;

    @BindView(R.id.fragment_game_titleview)
    View mTitleView;
    @BindView(R.id.fragment_game_refreshlayout)
    VpSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_game_recyclerview)
    RecyclerView mRecyclerView;
    private View mHeaderView;
    private Banner mBanner;
    private List<BannerBean.ListEntity> mBannerList;
    private GameAdapter mAdapter;

    private int currentPage = 1;

    @Override
    protected void initPresenter() {
        mPresenter = new GamePresenter(getActivity(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitleBar(mTitleView);

        mHeaderView = View.inflate(getParentActivity(), R.layout.fragment_header_game, null);

        mBanner = (Banner) mHeaderView.findViewById(R.id.fragment_game_banner);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
//        //添加自定义分割线
//        CommonsUtils.setDividerItemDecoration(mRecyclerView);
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
                // 获取游戏列表
                currentPage = 1;
                mPresenter.getGameList(currentPage);
                mPresenter.getGameTask();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 停止转动
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData() {
        mAdapter = new GameAdapter(getActivity());
        mAdapter.addHeaderView(mHeaderView);

        mRecyclerView.setAdapter(mAdapter);
        // 加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getGameList(currentPage);
            }
        }, mRecyclerView);
        mAdapter.setOnItemClickListener(this);

        // banner
        mPresenter.getBanner();
        // 获取游戏列表
        mPresenter.getGameList(currentPage);
        mPresenter.getGameTask();
    }

    @Override
    protected void onEventCallback(EventBase eventBase) {
        super.onEventCallback(eventBase);
        String action = eventBase.getAction();
        if (Constants.REFRESH_GAME_LIST_KEY.equals(action)) {
            // 退出游戏重新登录，需要重新刷新游戏列表
            currentPage = 1;
            mPresenter.getGameList(currentPage);
        }
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

    /**
     * 游戏任务列表
     *
     * @param gameBean
     */
    @Override
    public void resultGame(GameBean gameBean) {
        if (gameBean != null) {
            List<GameBean.ListBean> listBeans = gameBean.getList();

            if (currentPage == 1) {
                if (mGameBeanListBean != null && listBeans != null && listBeans.size() > 0) {
                    GameBean.ListBean listBean = listBeans.get(0);
                    if (listBean != null && listBean.getGameId() != mGameBeanListBean.getGameId()) {
                        gameBean.getList().add(0, mGameBeanListBean);
                    }
                }

                mAdapter.setNewData(listBeans);
                mAdapter.loadMoreComplete();
                if (listBeans.size() < 10) {
                    mAdapter.loadMoreEnd(true);
                }
            } else {
                if (listBeans != null && listBeans.size() > 0) {
                    mAdapter.addData(listBeans);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.loadMoreEnd();
                }
            }

            if (currentPage >= gameBean.getTotalPage()) {
                mAdapter.loadMoreEnd(false);
            } else {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            mAdapter.loadMoreFail();
            currentPage--;
        }

        currentPage++;

        if (!startOnly) {
            startOnly = true;
            mHandler.postDelayed(refreshRunnable, REFRESH_TIME);
        }
    }

    private GameBean.ListBean mGameBeanListBean;

    @Override
    public void resultGameTask(GameBean.ListBean gameBean) {
        if (gameBean != null) {
            mGameBeanListBean = gameBean;
            try {
                if (mAdapter.getItemCount() > 0) {
                    GameBean.ListBean listBean = mAdapter.getItem(0);
                    if (listBean != null && listBean.getGameId() != gameBean.getGameId()) {
                        mAdapter.addData(0, gameBean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            refreshGameList();
            mHandler.postDelayed(refreshRunnable, REFRESH_TIME);
        }
    };

    public void refreshGameList() {
        // 获取游戏列表
        currentPage = 1;
        mPresenter.getGameList(currentPage);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        // 检测是否完善资料
        if (TextUtils.isEmpty(AccountManager.getInstance().getPicture()) ||
                TextUtils.isEmpty(AccountManager.getInstance().getCloudNickName())) {
            DialogTips dialogTips = new DialogTips(getActivity());
            dialogTips.setTitle(getXmlString(R.string.tips_tip_title_txt));
            dialogTips.setMessage(getXmlString(R.string.please_finish_userinfo));
            dialogTips.setOkListenner(getXmlString(R.string.please_finish_userinfo_go), new OnDialogOkListenner() {
                @Override
                public void onClick() {
                    IntentUtil.ToSetUserInfoActivity(getActivity());
                }
            });
            dialogTips.setCancelListenner(null);
            dialogTips.show();
            return;
        }

        GameBean.ListBean item = mAdapter.getItem(position);
        if (item != null && !TextUtils.isEmpty(item.getWebServerRedirectUrl())) {
            IntentUtil.ToGameWebViewActivity(getActivity(), item.getGameName(), UrlConfig.getGameDefultUrl(item));
        }
    }

}
