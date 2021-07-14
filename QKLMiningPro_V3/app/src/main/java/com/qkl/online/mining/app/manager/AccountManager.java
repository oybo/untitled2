package com.qkl.online.mining.app.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.Constants;
import com.qkl.online.mining.app.data.commons.UrlConfig;
import com.qkl.online.mining.app.data.entity.Account;
import com.qkl.online.mining.app.data.entity.AppCommonsConfig;
import com.qkl.online.mining.app.data.entity.DictConfig;
import com.qkl.online.mining.app.data.entity.MyYuntEntity;
import com.qkl.online.mining.app.data.entity.SSExchangerate;
import com.qkl.online.mining.app.data.entity.User;
import com.qkl.online.mining.app.data.event.EventBase;
import com.qkl.online.mining.app.data.json.Convert;
import com.qkl.online.mining.app.data.json.JsonCallback;
import com.qkl.online.mining.app.data.json.LzyResponse;
import com.qkl.online.mining.app.ui.activity.SetActivity;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.FileIOUtils;
import com.qkl.online.mining.app.utils.FilePathUtils;
import com.qkl.online.mining.app.utils.IntentUtil;
import com.qkl.online.mining.app.utils.TimeUtils;
import com.qkl.online.mining.app.utils.languagelib.MultiLanguageUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * author：oyb on 2018/9/7 15:35
 */
public class AccountManager {

    private static AccountManager mInstance;

    private String access_token;
    private String starResidueName;
    private String starResidueDay;

    private Context mContext;
    private Account mAccount;
    private User mUser;
    private MyYuntEntity mMyYuntEntity;

    public static AccountManager getInstance() {
        if (mInstance == null) {
            synchronized (AccountManager.class) {
                if (mInstance == null) {
                    mInstance = new AccountManager();
                }
            }
        }
        return mInstance;
    }

    public void checkH5Zip() {
        // 检测H5页面版本
        OkGo.<JSONObject>post(UrlConfig.getManageUrl())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        try {
                            JSONObject jsonObject = response.body();
                            if (jsonObject != null) {
                                String downloadUrl = jsonObject.optString("downloadUrl");
                                if (TextUtils.isEmpty(downloadUrl)) {
                                    return;
                                }
                                String html5 = FilePathUtils.getInstance().getDefaultFilePath() + "/h5wallet";
                                String spVersion = Hawk.get(Constants.H5_MANAGE_VERSION, "1.0.0");
                                String version = jsonObject.optString("version");
                                if (!spVersion.equals(version)) {
                                    // 下载
                                    downloadFile(downloadUrl, html5, version);
                                } else {
                                    File h5walletFile = new File(html5);
                                    if (h5walletFile == null || !h5walletFile.exists()) {
                                        downloadFile(downloadUrl, html5, version);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }
                });
    }

    private void downloadFile(final String downloadUrl, final String outPathString, final String version) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String saveFile = FilePathUtils.getInstance().getDefaultFilePath() + "/h5wallet.zip";
                    boolean success = FileIOUtils.downLoadFromUrl(downloadUrl, new File(saveFile));
                    if (success) {
                        // 解压
                        File outFile = new File(outPathString);
                        if (outFile.exists()) {
                            FileIOUtils.deleteFolder(outPathString);
                        }
                        if (!outFile.exists()) {
                            outFile.mkdirs();
                        }
                        FileIOUtils.unZipFolder(saveFile, outPathString);
                        Hawk.put(Constants.H5_MANAGE_VERSION, version);
                        Hawk.put(Constants.H5_MANAGE_PATH, "file:///" + outPathString + "/index.html");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getAccountToken() {
        return "Bearer " + (!TextUtils.isEmpty(access_token) ? access_token : "");
    }

    public String getMemberId() {
        if (mAccount != null) {
            return mAccount.getMemberId();
        }
        return "";
    }

    public String getEmail() {
        String email = "";
        if (mUser != null) {
            email = mUser.getEmail();
            return email;
        }
        if (TextUtils.isEmpty(email)) {
            email = Hawk.get(Constants.LOGIN_NAME_KEY).toString();
        }
        return email;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public synchronized User getUser() {
        if (mUser == null) {
            mUser = new User();
        }
        return mUser;
    }

    public MyYuntEntity getMyYuntEntity() {
        return mMyYuntEntity;
    }

    public String getStarResidueName() {
        if (TextUtils.isEmpty(starResidueName)) {
            return CommonsUtils.getXmlString(mContext, R.string.null_txt);
        }
        return starResidueName;
    }

    public void setStarResidueName(String starResidueName) {
        this.starResidueName = starResidueName;
    }

    public String getStarResidueDay() {
        if (TextUtils.isEmpty(starResidueDay)) {
            return CommonsUtils.getXmlString(mContext, R.string.null_txt);
        }
        return starResidueDay;
    }

    public void setStarResidueDay(String starResidueDay) {
        this.starResidueDay = starResidueDay;
    }

    /**
     * 昵称
     *
     * @return
     */
    public String getNickName() {
        String nickName = getCloudNickName();
        if (TextUtils.isEmpty(nickName)) {
            nickName = CommonsUtils.getXmlString(mContext, R.string.userinfo_defult_nickname);
        }
        return nickName;
    }

    public String getCloudNickName() {
        String nickName = null;
        if (mUser != null && !TextUtils.isEmpty(mUser.getNickName())) {
            nickName = mUser.getNickName();
        }
        if (TextUtils.isEmpty(nickName)) {
            if (mAccount != null && !TextUtils.isEmpty(mAccount.getNickName())) {
                nickName = mAccount.getNickName();
                if (!TextUtils.isEmpty(nickName)) {
                    nickName = unicodeToCn(nickName);
                }
            }
        }
        return nickName;
    }

    public int getUserLevel() {
        if (mUser != null) {
            return mUser.getLevelCode();
        }
        return 0;
    }

    /**
     * 手机号
     *
     * @return
     */
    public String getPhone() {
        if (mUser != null) {
            return mUser.getMobile();
        }
        return "";
    }

    /**
     * 头像地址
     *
     * @return
     */
    public String getPicture() {
        String profilePicture = null;
        if (mUser != null && !TextUtils.isEmpty(mUser.getProfilePicture())) {
            profilePicture = mUser.getProfilePicture();
        }
        if (TextUtils.isEmpty(profilePicture)) {
            if (mAccount != null && !TextUtils.isEmpty(mAccount.getProfilePicture())) {
                profilePicture = mAccount.getProfilePicture();
            }
        }
        return profilePicture;
    }

    public void saveUser(String access_token) {
        if (!TextUtils.isEmpty(access_token)) {
            // to
            try {
                String strtoken = new String(Base64.decode(access_token.split("\\.")[1], Base64.DEFAULT));
                Account account = formUser(strtoken);
                if (account != null) {
                    this.mAccount = account;
                    this.access_token = access_token;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Account formUser(String jsonObjectStr) {
        Account account = null;
        try {
            Gson gson = new Gson();
            account = gson.fromJson(jsonObjectStr, Account.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        OkGo.<LzyResponse<User>>post(UrlConfig.getUserInfoUrl(AccountManager.getInstance().getMemberId()))
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<LzyResponse<User>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<User>> response) {
                        if (response != null) {
                            LzyResponse lzyResponse = response.body();
                            if (lzyResponse != null) {
                                mUser = (User) lzyResponse.data;
                                // 发送广播
                                sendRefreshEvent();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<User>> response) {
                        super.onError(response);
                    }
                });
    }

    /**
     * 获取用户YUNT账户信息
     */
    public void getUserYUNTData() {
        OkGo.<JSONObject>post(UrlConfig.getUserYuntDataUrl(AccountManager.getInstance().getMemberId()))
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        if (response != null) {
                            JSONObject object = response.body();
                            if (object != null) {
                                object = object.optJSONObject("data");
                                mMyYuntEntity = Convert.forMyYuntEntity(object);
                                if (mMyYuntEntity != null) {
                                    // 发送广播
                                    sendRefreshEvent();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }
                });
    }

    private SSExchangerate mSSExchangerate;

    public SSExchangerate getSSExchangerate() {
        return mSSExchangerate;
    }

    /**
     * 获取实时汇率
     */
    public void getYunEchangeRate() {
        OkGo.<JSONObject>post(UrlConfig.getYunExchangerateUrl())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        // {"res":{"code":0,"msg":""},"exchangeRate":0.50295305,"yunToYuntRate":50.295305}
                        JSONObject object = response.body();
                        if (object != null) {
                            double exchangeRate = object.optDouble("exchangeRate", 0);
                            double yunToYuntRate = object.optDouble("yunToYuntRate", 0);
                            double yunToYuntPrice = object.optDouble("yunToYuntPrice", 0);
                            mSSExchangerate = new SSExchangerate();
                            mSSExchangerate.setExchangeRate(exchangeRate);
                            mSSExchangerate.setYunToYuntRate(yunToYuntRate);
                            mSSExchangerate.setYunToYuntPrice(yunToYuntPrice);

//                            int code = object.optInt("code", -1);
//                            if (code == 0) {
//                                object = object.optJSONObject("data");
//                                if (object != null) {
//                                    mSSExchangerate = Convert.forSSExchangerate(object);
//                                    sendRefreshEvent();
//                                }
//                            }
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }
                });
    }

    private DictConfig mDictConfig;

    public DictConfig getDictConfig() {
        return mDictConfig;
    }

    public String getServiceEmail() {
        return mDictConfig != null ? mDictConfig.getServiceMail() : "";
    }

    /**
     * 获取系统配置字典接口
     */
    public void getCommonsConfig() {
        OkGo.<LzyResponse<DictConfig>>post(UrlConfig.getCommonsConfigUrl())
                .tag(this)
                .execute(new JsonCallback<LzyResponse<DictConfig>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<DictConfig>> response) {
                        LzyResponse<DictConfig> lzyResponse = response.body();
                        if (lzyResponse != null) {
                            mDictConfig = lzyResponse.data;
                        }
                        if (mDictConfig != null) {
                            String starCover = mDictConfig.getStartCover();
                            if (!TextUtils.isEmpty(starCover)) {
                                Hawk.put(Constants.WELCOME_BG_IMAGEVIEW_PATH_KEY, starCover);
                            }

                            EventBase eventBase = new EventBase();
                            eventBase.setAction(Constants.UPDATE_KEY);
                            EventBus.getDefault().post(eventBase);
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<DictConfig>> response) {
                        super.onError(response);
                    }
                });
    }

    private AppCommonsConfig mAppCommonsConfig;

    public AppCommonsConfig getAppCommonsConfig() {
        return mAppCommonsConfig;
    }

    public void getYunPlanetConfig(final JsonCallback jsonCallback) {
        OkGo.<JSONObject>post(UrlConfig.getYunPlanetConfigUrl())
                .tag(this)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        JSONObject jsonObject = response.body();
                        if (jsonObject != null) {
                            mAppCommonsConfig = Convert.forAppCommonsConfig(jsonObject);

                            if (jsonCallback != null) {
                                jsonCallback.onSuccess(null);
                            }

                            if (mAppCommonsConfig != null) {
                                String startBanners = mAppCommonsConfig.getStartBanners();
                                if (!TextUtils.isEmpty(startBanners)) {
                                    Hawk.put(Constants.WELCOME_BG_BANNER_PATH_KEY, startBanners);
                                } else {
                                    Hawk.delete(Constants.WELCOME_BG_BANNER_PATH_KEY);
                                }

                                // 请求配置接口
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        try {
                                            initConfigMsg();
                                            AppCommonsConfig.MsgBean msgBean = mAppCommonsConfig.getMsg();
                                            if (msgBean != null) {
                                                String twConfig = msgBean.getZhTW();
                                                String enConfig = msgBean.getEnUS();
                                                if (!TextUtils.isEmpty(twConfig)) {
                                                    JSONObject object = CommonsUtils.getJSONObjectGet(twConfig);
                                                    if (object != null) {
                                                        Hawk.put(Constants.ZH_MSG_CONFIG_KEY, object.toString());
                                                    }
                                                }
                                                if (!TextUtils.isEmpty(enConfig)) {
                                                    JSONObject object = CommonsUtils.getJSONObjectGet(enConfig);
                                                    if (object != null) {
                                                        Hawk.put(Constants.EN_MSG_CONFIG_KEY, object.toString());
                                                    }
                                                }
                                            }
                                            initConfigMsg();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        if (jsonCallback != null) {
                            jsonCallback.onError(null);
                        }
                    }
                });
    }

    private Map<String, String> mZhMsgMaps = new HashMap<>();
    private Map<String, String> mEnMsgMaps = new HashMap<>();

    private void initConfigMsg() {
        // 繁体
        try {
            String zhMsgStr = Hawk.get(Constants.ZH_MSG_CONFIG_KEY);
            if (!TextUtils.isEmpty(zhMsgStr)) {
                JSONObject zhJSONObject = new JSONObject(zhMsgStr);
                if (zhJSONObject != null) {
                    Iterator<String> it = zhJSONObject.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        String value = zhJSONObject.getString(key);
                        mZhMsgMaps.put(key, value);
                    }
                }
            }
            // 英文
            String enMsgStr = Hawk.get(Constants.EN_MSG_CONFIG_KEY);
            if (!TextUtils.isEmpty(enMsgStr)) {
                JSONObject enJSONObject = new JSONObject(enMsgStr);
                if (enJSONObject != null) {
                    Iterator<String> it = enJSONObject.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        String value = enJSONObject.getString(key);
                        mEnMsgMaps.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConfigMsg(int key) {
        int languageType = MultiLanguageUtil.getInstance().getLanguageType();
        if (languageType == 3) {
            // 英文
            return mEnMsgMaps.get(String.valueOf(key));
        } else {
            // 繁体
            return mZhMsgMaps.get(String.valueOf(key));
        }
    }

    private boolean isForce;

    public boolean checkAppUpdate(final AppCompatActivity activity) {
        // 是否设置页面过来的
        boolean isSetSource = false;
        if (activity instanceof SetActivity) {
            isSetSource = true;
        }

        if (mDictConfig != null) {
            String serviceVersion = mDictConfig.getVersion();
            String appVersion = CommonsUtils.getSoftVersionName(activity);
            if (TextUtils.isEmpty(serviceVersion) || TextUtils.isEmpty(appVersion)) {
                return false;
            }

            try {
                isForce = false;
                String[] serviceStrs = serviceVersion.split("\\.");
                String[] appStrs = appVersion.split("\\.");
                boolean isUpdate = false;
                if (Integer.parseInt(serviceStrs[0]) > Integer.parseInt(appStrs[0])) {
                    isUpdate = true;
                } else {
                    if (Integer.parseInt(appStrs[0]) > Integer.parseInt(serviceStrs[0])) {
                        return false;
                    }
                }
                if (!isUpdate) {
                    if (Integer.parseInt(serviceStrs[1]) > Integer.parseInt(appStrs[1])) {
                        isUpdate = true;
                    }
                }
                if (!isUpdate) {
                    if ((Integer.parseInt(serviceStrs[1]) == Integer.parseInt(appStrs[1])) &&
                            (Integer.parseInt(serviceStrs[2]) > Integer.parseInt(appStrs[2]))) {
                        isUpdate = true;
                    }
                }
                if (!isUpdate) {
                    return false;
                }

                if (Integer.parseInt(serviceStrs[0]) > Integer.parseInt(appStrs[0]) ||
                        Integer.parseInt(serviceStrs[1]) > Integer.parseInt(appStrs[1])) {
                    // 强制更新
                    isForce = true;
                }
                if (!isForce) {
                    if (Integer.parseInt(appStrs[2]) > Integer.parseInt(serviceStrs[2])) {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!activity.isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(CommonsUtils.getXmlString(activity, R.string.update_title));
                builder.setMessage(CommonsUtils.getXmlString(activity, R.string.update_message));
                builder.setPositiveButton(CommonsUtils.getXmlString(activity, R.string.update_ok), null);
                if (!isForce) {
                    builder.setNegativeButton(CommonsUtils.getXmlString(activity, R.string.update_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Hawk.put(Constants.UPDATE_CACEL_TODAY_KEY, TimeUtils.getToday());
                        }
                    });
                }
                if (!isSetSource) {
                    boolean isToday = TimeUtils.getToday().equals(Hawk.get(Constants.UPDATE_CACEL_TODAY_KEY));
                    if (!isForce && isToday) {
                        return false;
                    }
                }
                final AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String updateUrl = mDictConfig.getUpdateUrl();
                            IntentUtil.goBrowser(activity, updateUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!isForce) {
                            dialog.dismiss();
                        }
                    }
                });
                return true;
            }
        }
        return false;
    }

    public void clearCache() {
        mUser = null;
        mAccount = null;
        mMyYuntEntity = null;
    }

    /**
     * 退出游戏需要重新登录
     */
    public void resetLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            String userName = Hawk.get(Constants.LOGIN_NAME_KEY);
            String password = Hawk.get(Constants.LOGIN_PASSWORD_KEY);

            jsonObject.put("email", userName);
            jsonObject.put("password", password);
            jsonObject.put("language", CommonsUtils.getLanguage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<JSONObject>post(UrlConfig.getLoginUrl())
                .upJson(jsonObject)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        JSONObject object = response.body();
                        if (object != null) {
                            int code = object.optInt("code", -1);
                            if (code == 0) {
                                // 成功
                                // 发送刷新游戏列表
                                EventBase eventBase = new EventBase();
                                eventBase.setAction(Constants.REFRESH_GAME_LIST_KEY);
                                EventBus.getDefault().post(eventBase);
                                JSONObject dataObject = object.optJSONObject("data");
                                String access_token = dataObject.optString("access_token");
                                AccountManager.getInstance().saveUser(access_token);
                                // 登录之后获取用户信息
                                AccountManager.getInstance().getUserInfo();
                                // 用户账户信息余额
                                AccountManager.getInstance().getUserYUNTData();
                                // 获取实时汇率接口
                                AccountManager.getInstance().getYunEchangeRate();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }
                });
    }

    private void sendRefreshEvent() {
        // 发送广播
        EventBase eventBase = new EventBase();
        eventBase.setAction(Constants.REFRESH_USER_INFO_KEY);
        EventBus.getDefault().post(eventBase);
    }

    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    public String getExchangeStatus(String txid, int status) {
        // 1001=等待审核
        // 2001=等待打币
        // 3001=打币中，
        // 3002=打币失败，
        // 4001=打币完成
        if (TextUtils.isEmpty(txid)) {
            // 3001=打币中，
            return CommonsUtils.getXmlString(mContext, R.string.yun_exchange_in);
        }
        String statusStr = "";
        if (status == 1001) {
            statusStr = CommonsUtils.getXmlString(mContext, R.string.yun_exchange_audit);
        } else if (status == 2001) {
            statusStr = CommonsUtils.getXmlString(mContext, R.string.yun_exchange_begin);
        } else if (status == 3001) {
            statusStr = CommonsUtils.getXmlString(mContext, R.string.yun_exchange_in);
        } else if (status == 3002) {
            statusStr = CommonsUtils.getXmlString(mContext, R.string.yun_exchange_failed);
        } else if (status == 4001) {
            statusStr = CommonsUtils.getXmlString(mContext, R.string.yun_exchange_finish);
        }

        return statusStr;
    }

}
