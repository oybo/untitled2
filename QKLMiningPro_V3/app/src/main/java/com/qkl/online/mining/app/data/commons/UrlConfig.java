package com.qkl.online.mining.app.data.commons;

import android.text.TextUtils;

import com.qkl.online.mining.app.AppContext;
import com.qkl.online.mining.app.data.entity.GameBean;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.languagelib.MultiLanguageUtil;

import java.util.Random;

/**
 * author：oyb on 2018/7/5 22:54
 * 接口地址
 */
public class UrlConfig {

    /**
     * yunplanet.net
     * hjkk66.com
     */


    /**
     * 主要的接口请求域名
     */
//    private static final String BASE_URL = "https://api.hjkk66.com";
    private static final String BASE_URL = "http://api.hjkk66.com";
//    private static final String BASE_URL = "http://devapi.hjkk88.com";

    private static final String MODE = "ali";

    /**
     * 星球配置接口的
     */
    private static final String PLANET_CONFIG_URL = "http://config.hjkk66.com";
//    private static final String PLANET_CONFIG_URL = "http://config.yunplanet.net";

    /**
     * 用户协议接口域名
     */
    private static final String XIEYI_URL = "http://h5.hjkk66.com";
//    private static final String XIEYI_URL = "http://devh5.hjkk88.com";

    /**
     * 发送验证码    -   注册
     *
     * @return
     */
    public static final String getSendCodeUrl() {
        return BASE_URL + "/api/open/sys/user/regcode";
    }

    /**
     * 发送验证码    -   找回密码
     *
     * @return
     */
    public static final String getSendCodeByForgetUrl() {
        return BASE_URL + "/api/open/sys/user/forgetpasscode";
    }

    /**
     * 发送验证码    -   找回支付密码
     *
     * @return
     */
    public static final String getSendCodeByForgetPayUrl() {
        return BASE_URL + "/api/user/sys/user/forgetpaypasscode";
    }

    /**
     * 发送验证码    -   设置提币地址
     *
     * @return
     */
    public static final String getYUNAddressUrl() {
        return BASE_URL + "/api/user/sys/user/withdrawcode";
    }

    /**
     * 提交注册
     *
     * @return
     */
    public static final String getRegisterUserUrl() {
        return BASE_URL + "/api/open/sys/user/reg";
    }

    /**
     * 忘记密码
     *
     * @return
     */
    public static final String getForgetPasswordUrl() {
        return BASE_URL + "/api/open/sys/user/resetpassword";
    }

    /**
     * 修改密码
     *
     * @return
     */
    public static final String getChangePasswordUrl() {
        return BASE_URL + "/api/user/sys/user/changepassword";
    }

    /**
     * 忘记支付密码
     *
     * @return
     */
    public static final String getForgetPayPasswordUrl() {
        return BASE_URL + "/api/user/sys/user/resetpaypass";
    }

    /**
     * 修改支付密码
     *
     * @return
     */
    public static final String getChangePayPasswordUrl() {
        return BASE_URL + "/api/user/sys/user/changepaypass";
    }

    /**
     * 登录
     *
     * @return
     */
    public static final String getLoginUrl() {
        return BASE_URL + "/api/open/sys/user/token";
    }

    /**
     * 获取图形验证码
     *
     * @param email
     * @return
     */
    public static final String getLoginCaptchaUrl(String email) {
        return BASE_URL + "/api/open/sys/captcha/login/" + email + "?aa=" + new Random().nextInt(10000);
    }

    /**
     * 注销
     *
     * @return
     */
    public static final String getLogOutUrl() {
        return BASE_URL + "/api/auth/logout";
    }

    /**
     * 首页banner
     *
     * @return
     */
    public static final String getHomeBanner() {
        return BASE_URL + "/api/open/sys/banner/list";
    }

    /**
     * 首页新闻公告/滚动信息/常见问题
     *
     * @return
     */
    public static final String getHomeNews() {
        return BASE_URL + "/api/open/sys/article/list";
    }

    /**
     * 首页获取YUN汇率列表-价格曲线图
     *
     * @return
     */
    public static final String getHomeYunExchangerateUrl() {
        return BASE_URL + "/api/finance/sys/exchangecoinrate/pricetrends";
    }

    /**
     * 首页获取YUN汇率列表-价格折线图
     *
     * @return
     */
    public static final String getHomeYunPriceLineUrl() {
        return BASE_URL + "/api/finance/sys/memberyunbills/yunPriceLine";
    }

    /**
     * 用户存款任务
     *
     * @return
     */
    public static final String getUserDepositTask() {
        return BASE_URL + "/api/finance/sys/task/depositTask";
    }

    /**
     * 个人信息
     *
     * @return
     */
    public static final String getUserInfoUrl(String memberId) {
        return BASE_URL + "/api/user/sys/user/" + memberId;
    }

    /**
     * YUNT提币接口
     *
     * @return
     */
    public static final String getYunt2YunUrl() {
        return BASE_URL + "/api/finance/sys/exchange/yunt2yun";
    }

    /**
     * YUNT--YUN兑换账单接口
     *
     * @return
     */
    public static final String getExchangeListUrl() {
        return BASE_URL + "/api/finance/sys/exchange/list";
    }

    /**
     * YUNT--YUN兑换账单接口-详情接口
     *
     * @return
     */
    public static final String getExchangeDetailUrl() {
        return BASE_URL + "/api/finance/sys/exchange/txthash";
    }

    /**
     * 兑换详情- 检查Txid
     *
     * @return
     */
    public static final String getCheckTxidUrl(String txid) {
        return "https://etherscan.io/tx/" + txid;
    }

    /**
     * 获取用户YUNT账户信息
     *
     * @return
     */
    public static final String getUserYuntDataUrl(String memberId) {
        return BASE_URL + "/api/finance/sys/yunt/account/" + memberId;
    }

    /**
     * YUNT账单流水接口   -   收益详情
     *
     * @return
     */
    public static final String getJieDianEarningsUrl() {
        return BASE_URL + "/api/finance/sys/yunt/list";
    }

    /**
     * 上传头像
     *
     * @return
     */
    public static final String getUploadProfileUrl(String memberId) {
        return BASE_URL + "/api/user/sys/user/profileup/" + memberId;
    }

    /**
     * 上传头像
     *
     * @return
     */
    public static final String getSetUserInfoUrl() {
        return BASE_URL + "/api/user/sys/user/resetuserinfo";
    }

    /**
     * 获取矿机列表接口
     *
     * @return
     */
    public static final String getMinerListUrl() {
        return BASE_URL + "/api/miner/sys/minermachine/list";
    }

    /**
     * 我的星球接口
     *
     * @return
     */
    public static final String getMyStarListUrl(String memberId) {
        return BASE_URL + "/api/miner/sys/minerorder/user/" + memberId;
    }

    /**
     * 获取实时汇率接口
     *
     * @return
     */
    public static final String getYunExchangerateUrl() {
        return BASE_URL + "/v3.0/finance/yun/exchangeRate";
//        return BASE_URL + "/api/finance/sys/exchangecoinrate/yunrate";
    }

    /**
     * 星球订单信息接口
     * @return
     */
    public static final String getOrderInfo() {
        return BASE_URL + "/v3.0/finance/planet/orderInfo";
    }

    /**
     * 星球购买接口
     * @return
     */
    public static final String getOrderBuy() {
        return BASE_URL + "/v3.0/finance/planet/order";
    }

    /**
     * 星球购买配置接口
     * @return
     */
    public static final String getOrderConfig() {
        return BASE_URL + "/v3.0/finance/planet/orderConf";
    }

    /**
     * 星球购买配置接口
     * @return
     */
    public static final String getOrderWalletStat() {
        return BASE_URL + "/v3.0/finance/yun/walletStat";
    }

    /**
     * 用户收益统计
     * @return
     */
    public static final String getUserYuntYield() {
        return BASE_URL + "/v3.0/finance/yuntYield/stat";
    }

    /**
     * 用户邀请人数配置
     * @return
     */
    public static final String getInvoteConfig() {
        return BASE_URL + "/v3.0/user/user/invoteConfig";
    }

    public static final String getManageUrl() {
        return BASE_URL + "/v3.0/finance/yun/walletConfig";
    }

    /**
     * 今日剩余星球数
     *
     * @return
     */
    public static final String getTodayStarRestNumberUrl() {
        return BASE_URL + "/api/miner/sys/minermachine/totalbuylimit";
    }

    /**
     * 购买星球=矿机
     *
     * @return
     */
    public static final String getPurchaseStarUrl() {
        return BASE_URL + "/api/miner/sys/minerorder/submit";
    }

    /**
     * 获取游戏列表接口
     *
     * @return
     */
    public static final String getGameList() {
        return BASE_URL + "/api/client/sys/gametask/list";
    }

    public static final String getGameTaskUrl() {
        return BASE_URL + "/v3.0/task/deposit";
    }

    /**
     * 设置YUN钱包地址接口
     *
     * @return
     */
    public static final String getYunAddressUrl() {
        return BASE_URL + "/api/user/sys/user/resetyunaddress";
    }

    /**
     * 获得用户列表/我的团队接口
     *
     * @return
     */
    public static final String getTeamUrl() {
        return BASE_URL + "/api/user/sys/user/list";
    }

    /**
     * 获取系统配置字典接口
     *
     * @return
     */
    public static final String getCommonsConfigUrl() {
        return BASE_URL + "/api/open/sys/config/dict";
    }

    /**
     * 获取系统配置字典接口
     *
     * @return
     */
    public static final String getYunPlanetConfigUrl() {
        String version = CommonsUtils.getSoftVersionName(AppContext.getInstance());
        return PLANET_CONFIG_URL + "/config.aspx?v="
                + version + "&from=android&t="
                + System.currentTimeMillis() + "&mode=" + MODE;
    }

    /**
     * 矿池实时数据接口
     *
     * @return
     */
    public static final String getPoolConfigUrl() {
        return BASE_URL + "/api/open/sys/config/pool";
    }

    public static final String getGameDefultUrl(GameBean.ListBean gameEntity) {
        String language = "zh-CN";
        int languageType = MultiLanguageUtil.getInstance().getLanguageType();
        switch (languageType) {
            case 1:
                language = "zh-CN";
                break;
            case 2:
                language = "zh-TW";
                break;
            case 3:
                language = "en";
                break;
        }

        String url = BASE_URL + "/api/auth/oauth/authorize?client_id={{client_id}}&response_type=code&state={{state}}&scope={{webclient}}&redirect_uri={{redirect_uri}}";
        return url.replace("{{client_id}}", !TextUtils.isEmpty(gameEntity.getClientId()) ? gameEntity.getClientId() : "")
                .replace("{{state}}", language)
                .replace("{{webclient}}", "webclient")
                .replace("{{redirect_uri}}", gameEntity.getWebServerRedirectUrl());
    }

    public static String getRegisterXieYi() {
        return XIEYI_URL + "/protocol";
    }

}
