package com.qkl.online.mining.app.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.Constants;
import com.qkl.online.mining.app.data.entity.AppCommonsConfig;
import com.qkl.online.mining.app.data.entity.DictConfig;
import com.qkl.online.mining.app.data.entity.MyStar;
import com.qkl.online.mining.app.data.entity.SSExchangerate;
import com.qkl.online.mining.app.data.entity.StarProduct;
import com.qkl.online.mining.app.data.event.EventBase;
import com.qkl.online.mining.app.listener.OnDialogOkListenner;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.presenter.StarPresenter;
import com.qkl.online.mining.app.mvp.view.IStarView;
import com.qkl.online.mining.app.ui.BaseFragment;
import com.qkl.online.mining.app.ui.adapter.MyStarAdapter;
import com.qkl.online.mining.app.ui.adapter.StarProductAdapter;
import com.qkl.online.mining.app.ui.view.CircleImageView;
import com.qkl.online.mining.app.ui.view.DialogTips;
import com.qkl.online.mining.app.ui.view.VpSwipeRefreshLayout;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.IntentUtil;
import com.qkl.online.mining.app.utils.glide.GlideImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：oyb on 2018/7/4 16:46
 */
public class StarFragment extends BaseFragment<StarPresenter> implements IStarView, BaseQuickAdapter.OnItemChildClickListener {

    private static final long REFRESH_STAR_REST_NUMBER_TIME = 5 * 60 * 1000;
    private static Handler mHandler = new Handler();

    @BindView(R.id.fragment_star_refreshlayout)
    VpSwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fragment_star_titleview)
    View mBarView;
    @BindView(R.id.fragment_star_canbuy_number_txt)
    TextView mCanBuyTxt;
    @BindView(R.id.fragment_star_yunToYuntRate_txt)
    TextView mStarYunToYuntRate;
    @BindView(R.id.fragment_star_yuntPrice_txt)
    TextView mStarYunToYuntPrice;
    @BindView(R.id.fragment_star_parities_txt)
    TextView mStarPriceTxt;
    @BindView(R.id.fragment_star_myyun_txt)
    TextView mMyYunTxt;
    @BindView(R.id.fragment_star_user_nickname)
    TextView mNickNameTxt;
    @BindView(R.id.fragment_star_user_icon)
    CircleImageView mUserIconView;
    @BindView(R.id.fragment_mystar_title_txt)
    TextView mMyStarTitleTxt;
    @BindView(R.id.fragment_star_order_jine)
    EditText mOrderFinanceET;
    @BindView(R.id.fragment_star_orderbuy)
    TextView mOrderBuyTView;
    @BindView(R.id.star_order_config_finance_txt)
    TextView mOrderFinanceConfigTView;
    @BindView(R.id.star_order_config_finance_layout)
    LinearLayout mOrderFinanceConfigLayout;

    private MyStarAdapter mMyStarAdapter;

    private StarProductAdapter mStarProductAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new StarPresenter(getActivity(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitleBar(mBarView);
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
                AccountManager.getInstance().getUserYUNTData();
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
        // 我的星球
        mPresenter.getMyStar();
        // 获取矿机列表
        mPresenter.getMineList();
        // 今日剩余星球总数
        mPresenter.todayStarRestNumber();

        setUserInfo();
    }

    @Override
    protected void onEventCallback(EventBase eventBase) {
        super.onEventCallback(eventBase);
        String action = eventBase.getAction();
        if (Constants.REFRESH_USER_INFO_KEY.equals(action)) {
            // 刷新数据
            setUserInfo();
        } else if (Constants.REFRESH_MY_STAR_KEY.equals(action)) {
            // 刷新我的星球
            mPresenter.getMyStar();
        }
    }

    private void setUserInfo() {
        // 昵称
        mNickNameTxt.setText(AccountManager.getInstance().getNickName());
        // 头像
        if (!TextUtils.isEmpty(AccountManager.getInstance().getPicture())) {
            GlideImageLoader.loadImage(getActivity(), AccountManager.getInstance().getPicture(), mUserIconView);
        }
//        // 实时汇率
        SSExchangerate ssExchangerate = AccountManager.getInstance().getSSExchangerate();
        if (ssExchangerate != null) {
            mStarYunToYuntRate.setText(getXmlString(R.string.yun_usdt_exchange_txt, ssExchangerate.getYunToYuntRate()));
            mStarYunToYuntPrice.setText(getXmlString(R.string.yun_usdt_yuntoprice_txt, ssExchangerate.getYunToYuntPrice()));
        } else {
            mStarYunToYuntRate.setText(getXmlString(R.string.yun_usdt_exchange_txt, "0"));
            mStarYunToYuntPrice.setText(getXmlString(R.string.yun_usdt_yuntoprice_txt, "0"));
        }
//        // 我的yunt余额
//        MyYuntEntity yuntEntity = AccountManager.getInstance().getMyYuntEntity();
//        if (yuntEntity != null) {
//            mMyYunTxt.setText(getXmlString(R.string.yunt_my_money_txt, yuntEntity.getBalance()));
//        } else {
//            mMyYunTxt.setText(getXmlString(R.string.yunt_my_money_txt, "0"));
//        }
        // 订单配置
        mPresenter.getOrderConfig();
        // 各个钱包统计
        mPresenter.getOrderWalletStat();
    }

    /**
     * 星球列表
     *
     * @param starProduct
     */
    @Override
    public void resultMine(StarProduct starProduct) {

    }

    private AnimationDrawable mAnimationDrawable;

    @Override
    public void resultMyStar(MyStar myStar) {
        // 图片
        ImageView imageView = getView().findViewById(R.id.item_my_xingqiu_image);
        if (myStar != null && !TextUtils.isEmpty(myStar.getMinerImg())) {
            GlideImageLoader.loadImage(getActivity(), myStar.getMinerImg(), imageView);
        } else {
            GlideImageLoader.loadWelComeImage(getActivity(), R.drawable.star_defult_image, imageView);
        }
        // 星球金额
        TextView nameTxt = getView().findViewById(R.id.item_my_xingqiu_name_txt);
        // 累计产出
        TextView suanliTxt = getView().findViewById(R.id.item_my_xingqiu_suanli_txt);
        // 状态
        TextView zhuangtaiTxt = getView().findViewById(R.id.item_my_xingqiu_zhuangtai_txt);

        if (myStar != null) {
            mMyStarTitleTxt.setText(getXmlString(R.string.main_star_mysytar_txt));
            AccountManager.getInstance().setStarResidueName(myStar.getMinerName());
            AccountManager.getInstance().setStarResidueDay(String.valueOf(myStar.getRemaining()));
            // 刷新个人中心页面
            EventBase eventBase = new EventBase();
            eventBase.setAction(Constants.REFRESH_USER_INFO_KEY);
            EventBus.getDefault().post(eventBase);
        } else {
            // 暂无星球
            mMyStarTitleTxt.setText(getXmlString(R.string.main_star_mysytar_not_available_txt));
            myStar = new MyStar();
            myStar.setMinerName(getXmlString(R.string.purchase_star_title_null_txt));
            myStar.setMinerImg("");
            myStar.setRemaining(0);
            myStar.setAccumulated(0);
            myStar.setIsRun(-1);
            AccountManager.getInstance().setStarResidueName(myStar.getMinerName());
            AccountManager.getInstance().setStarResidueDay(String.valueOf(myStar.getRemaining()));
        }

        nameTxt.setText(CommonsUtils.getXmlString(getActivity(), R.string.order_star_jine_txt, myStar.getOrderAmount()));
        suanliTxt.setText(CommonsUtils.getXmlString(getActivity(), R.string.order_ljcc_txt, myStar.getOutput()));
        String runStatus = "";
        boolean isRun = false;
        switch (myStar.getStatus()) {
            case 0:
                // 未购买
                runStatus = CommonsUtils.getXmlString(getActivity(), R.string.order_states_weigoumai);
                break;
            case 1001:
                // 待审核
                runStatus = CommonsUtils.getXmlString(getActivity(), R.string.order_states_audit);
                break;
            case 2001:
                // 运行中
                isRun = true;
                runStatus = CommonsUtils.getXmlString(getActivity(), R.string.order_states_run);
                break;
            case 3001:
                // 已停止
                runStatus = CommonsUtils.getXmlString(getActivity(), R.string.order_states_stop);
                break;
            case 4001:
                // 已暂停
                runStatus = CommonsUtils.getXmlString(getActivity(), R.string.order_states_pause);
                break;
        }
        zhuangtaiTxt.setText(CommonsUtils.getXmlString(getActivity(), R.string.purchase_star_zhuangtai_bt, runStatus));

        if (isRun) {
            ImageView waKuangImage = getView().findViewById(R.id.item_wakuang_imageview);
            if (waKuangImage != null) {
                waKuangImage.setVisibility(View.VISIBLE);
                mAnimationDrawable = (AnimationDrawable) waKuangImage.getDrawable();
                mAnimationDrawable.start();
            }
        }
    }

    /**
     * 今日剩余星球总数
     *
     * @param number
     */
    @Override
    public void resultStarRestNumber(int number) {
        mCanBuyTxt.setText(getXmlString(R.string.public_canbuy_number_txt, number));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 定时刷新今日剩余星球总数
                mPresenter.todayStarRestNumber();
                // 获取实时汇率接口
                AccountManager.getInstance().getYunEchangeRate();
            }
        }, REFRESH_STAR_REST_NUMBER_TIME);
    }

    private int minAmount;
    private int maxAmount;

    /**
     * 订单配置信息
     *
     * @param minAmount
     * @param maxAmount
     * @param canBuy
     */
    @Override
    public void resultOrderConfig(int minAmount, int maxAmount, boolean canBuy, String minAmountFormat, String maxAmountFormat) {
        if (canBuy) {
            mOrderBuyTView.setBackgroundResource(R.drawable.selector_buy_bt_bg);
        } else {
            mOrderBuyTView.setBackgroundResource(R.drawable.selector_ban_buy_bt_bg);
            mOrderBuyTView.setEnabled(false);
        }
        mOrderFinanceConfigTView.setText(CommonsUtils.getXmlString(getActivity(), R.string.star_buy_tip,
                maxAmountFormat, minAmountFormat));
        mOrderFinanceConfigLayout.setVisibility(View.VISIBLE);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    /**
     * 各个钱包统计
     *
     * @param reinvestAmount
     * @param yunAmount
     */
    @Override
    public void resultOrderWalletStat(int reinvestAmount, int yunAmount) {
        mStarPriceTxt.setText(CommonsUtils.getXmlString(getActivity(), R.string.order_walletStat_ftouqb, reinvestAmount));
        mMyYunTxt.setText(CommonsUtils.getXmlString(getActivity(), R.string.order_walletStat_duiruqb, yunAmount));
    }

    @OnClick(R.id.fragment_star_make)
    void starMake() {
        // 星球预约
        AppCommonsConfig appCommonsConfig = AccountManager.getInstance().getAppCommonsConfig();
        if (appCommonsConfig != null) {
            String reservationUrl = appCommonsConfig.getReservationUrl();
            if (TextUtils.isEmpty(reservationUrl)) {
                DictConfig dictConfig = AccountManager.getInstance().getDictConfig();
                if (dictConfig != null) {
                    reservationUrl = dictConfig.getReservationUrl();
                }
            }
            if (!TextUtils.isEmpty(reservationUrl)) {
                IntentUtil.ToWebViewActivity(getActivity(), getXmlString(R.string.public_star_make_txt), reservationUrl, true);
            }
        }
    }

    @OnClick(R.id.fragment_star_orderbuy)
    void starOrderBuy() {
        // 购买
        String orderFinance = mOrderFinanceET.getText().toString().trim();
        if (!TextUtils.isEmpty(orderFinance)) {
            final int finance = Integer.parseInt(orderFinance);
            if (finance > minAmount && finance < maxAmount) {
                DialogTips dialogTips = new DialogTips(getActivity());
                dialogTips.setMessage(CommonsUtils.getXmlString(getActivity(), R.string.buy_plance_tips, finance));
                dialogTips.setOkListenner(new OnDialogOkListenner() {
                    @Override
                    public void onClick() {
                        mPresenter.orderBuy(finance);
                    }
                });
                dialogTips.setCancelListenner(null);
                dialogTips.setCanceledOnTouchOutside(false);
                dialogTips.show();
            }
        }
    }

    @Override
    public void resultPurchaseStar() {
        // 无需处理
    }

    @Override
    public void showLoading() {
        showLoading(true);
    }

    @Override
    public void hideLoading() {
        showLoading(false);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        StarProduct.ListEntity listEntity = mStarProductAdapter.getItem(position);
        if (listEntity != null) {
            IntentUtil.ToPurchaseStarActivity(getActivity(), listEntity);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMyStarAdapter != null) {
            mMyStarAdapter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMyStarAdapter != null) {
            mMyStarAdapter.onPause();
        }
    }
}
