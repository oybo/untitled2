package com.qkl.online.mining.app.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.UrlConfig;
import com.qkl.online.mining.app.data.json.JsonCallback;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.view.IUserInfoView;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.ToastUtils;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 个人信息中心
 */

public class UserInfoPresenter extends BasePresenter<IUserInfoView> {

    public UserInfoPresenter(Activity context, IUserInfoView view) {
        super(context, view);
    }

    /**
     * YUNT兑换YUN   --   YUNT提币接口
     * memberId	是	long	用户ID
     * inputAmount	是	decimal	输入的YUNT数量
     * exchangeFee	是	decimal	手续费
     * payPassword	是	string	支付密码
     */
    public void yunt2yun(String inputAmount, String exchangeFee, String payPassword) {
        mView.showLoading();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", AccountManager.getInstance().getMemberId());
            jsonObject.put("inputAmount", inputAmount);
            jsonObject.put("exchangeFee", exchangeFee);
            jsonObject.put("payPassword", payPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<JSONObject>post(UrlConfig.getYunt2YunUrl())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .upJson(jsonObject)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        mView.hideLoading();
                        JSONObject object = response.body();
                        if (object != null) {
                            int code = object.optInt("code", -1);
                            if (code == 0) {
                                String msg = AccountManager.getInstance().getConfigMsg(code);
                                if (TextUtils.isEmpty(msg)) {
                                    msg = CommonsUtils.getXmlString(mContext, R.string.parities_ynu_success_txt);
                                }
                                ToastUtils.showShort(msg);
                                // 兑换成功刷新我的钱包
                                AccountManager.getInstance().getUserYUNTData();
                            } else {
                                String msg = AccountManager.getInstance().getConfigMsg(code);
                                if (TextUtils.isEmpty(msg)) {
                                    msg = object.optString("msg");
                                }
                                ToastUtils.showShort(msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        mView.hideLoading();
                        ToastUtils.showShort(response.getException().getMessage());
                    }
                });
    }

    /**
     * 用户收益统计
     */
    public void getUserYuntYield() {
        OkGo.<JSONObject>post(UrlConfig.getUserYuntYield())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        if (response != null) {
                            // {"res":{"code":0,"msg":""},"miningYield":7397.1452,"invoteYield":0,"childrenYield":0,"solarYield":0,"galaxyYield":0,
                            // "groupYield":0,"clusterYield":0,"nodeYield":0,"released":7397.1452,"unreleased":1102174.6348,
                            // "h5Url":"http:\/\/h5wallet.yunplanet.net\/index.html","releasedPercent":"0.67","unreleasedPercent":"99.33"}
                            JSONObject object = response.body();
                            if (object != null) {
                                double miningYield = object.optDouble("miningYield", 0.00);
                                double invoteYield = object.optDouble("invoteYield", 0.00);
                                double childrenYield = object.optDouble("childrenYield", 0.00);
                                double solarYield = object.optDouble("solarYield", 0.00);
                                double galaxyYield = object.optDouble("galaxyYield", 0);
                                double groupYield = object.optDouble("groupYield", 0);
                                double clusterYield = object.optDouble("clusterYield", 0);
                                double nodeYield = object.optDouble("nodeYield", 0);
                                double released = object.optDouble("released", 0);
                                double unreleased = object.optDouble("unreleased", 0);
                                String releasedPercent = object.optString("releasedPercent");
                                String unreleasedPercent = object.optString("unreleasedPercent");
                                String h5Url = object.optString("h5Url", "0");

                                DecimalFormat df = new DecimalFormat("0.00");
                                String s_miningYield = df.format(miningYield);
                                String s_invoteYield = df.format(invoteYield);
                                String s_childrenYield = df.format(childrenYield);
                                String s_solarYield = df.format(solarYield);
                                String s_galaxyYield = df.format(galaxyYield);
                                String s_groupYield = df.format(groupYield);
                                String s_clusterYield = df.format(clusterYield);
                                String s_nodeYield = df.format(nodeYield);
                                String s_released = df.format(released);
                                String s_unreleased = df.format(unreleased);

                                if(!releasedPercent.contains("%")) {
                                    releasedPercent+= "%";
                                }
                                if(!unreleasedPercent.contains("%")) {
                                    unreleasedPercent+= "%";
                                }
                                mView.resultUserYuntYield(s_miningYield, s_invoteYield, s_childrenYield, s_solarYield,
                                        s_galaxyYield, s_groupYield, s_clusterYield, s_nodeYield,
                                        s_released, s_unreleased, releasedPercent, unreleasedPercent, h5Url);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }
                });
    }

    public void getInvoteConfig() {
        OkGo.<JSONObject>post(UrlConfig.getInvoteConfig())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        if (response != null) {
                            JSONObject object = response.body();
                            if (object != null) {
                                JSONObject resJSONObject = object.optJSONObject("res");
                                if (resJSONObject != null && resJSONObject.optInt("code") == 0) {
                                    String planetCount = object.optString("planetCount");
                                    mView.resultInvoteConfig(planetCount);
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

    public void logOut() {
        mView.showLoading();
        OkGo.<JSONObject>post(UrlConfig.getLogOutUrl())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        mView.hideLoading();
                        mView.logOut();
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        mView.hideLoading();
                        mView.logOut();
                    }
                });
    }

}
