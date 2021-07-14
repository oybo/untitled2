package com.qkl.online.mining.app.mvp.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.UrlConfig;
import com.qkl.online.mining.app.data.entity.Deposit;
import com.qkl.online.mining.app.data.json.JsonCallback;
import com.qkl.online.mining.app.data.json.LzyResponse;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.view.ILoginView;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 登录
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    public LoginPresenter(final Activity context, ILoginView view) {
        super(context, view);
    }

    public void login(String email, String password, String captcha) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("captcha", captcha);
            jsonObject.put("language", CommonsUtils.getLanguage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mView.showLoading();
        OkGo.<JSONObject>post(UrlConfig.getLoginUrl())
                .upJson(jsonObject)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        final JSONObject object = response.body();
                        if (object != null) {
                            int code = object.optInt("code", -1);
                            if (code == 0) {
                                // 成功
                                // 判断是否有获取到getYunPlanetConfigUrl接口
                                if (AccountManager.getInstance().getAppCommonsConfig() == null) {
                                    AccountManager.getInstance().getYunPlanetConfig(new JsonCallback() {
                                        @Override
                                        public void onSuccess(Response response) {
                                            callLoginSuccess(object);
                                        }

                                        @Override
                                        public void onError(Response response) {
                                            if (response != null) {
                                                super.onError(response);
                                            }
                                            callLoginError(object);
                                            mView.hideLoading();
                                        }
                                    });
                                    return;
                                }

                                callLoginSuccess(object);
                                return;
                            } else {
                                callLoginError(object);
                            }
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        try {
                            ToastUtils.showShort(response.getException().getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mView.hideLoading();
                    }
                });
    }

    private void callLoginSuccess(final JSONObject object) {
        JSONObject dataObject = object.optJSONObject("data");
        String access_token = dataObject.optString("access_token");
        AccountManager.getInstance().saveUser(access_token);
        // 检测用户的存款任务
        checkUserDeposit();
        // 登录之后获取用户信息
        AccountManager.getInstance().getUserInfo();
        // 用户账户信息余额
        AccountManager.getInstance().getUserYUNTData();
        // 获取实时汇率接口
        AccountManager.getInstance().getYunEchangeRate();
        // 检测H5页面版本
        AccountManager.getInstance().checkH5Zip();
    }

    /**
     * 检测用户的存款任务
     */
    public void checkUserDeposit() {
        OkGo.<LzyResponse<List<Deposit>>>post(UrlConfig.getUserDepositTask())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<LzyResponse<List<Deposit>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<Deposit>>> response) {
                        Deposit deposit = null;
                        // 功能暂时不上 暂时注释叼掉
//                        if (response != null) {
//                            LzyResponse<List<Deposit>> listLzyResponse = response.body();
//                            List<Deposit> depositList = listLzyResponse.data;
//                            if (depositList != null && depositList.size() > 0) {
//                                deposit = depositList.get(0);
//                                for (Deposit d : depositList) {
//                                    if (d.getIsMust() == 1) {
//                                        deposit = d;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                        // 回调
                        mView.loginSuccess(deposit);
                    }

                    @Override
                    public void onError(Response<LzyResponse<List<Deposit>>> response) {
                        super.onError(response);
                        // 回调
                        mView.loginSuccess(null);
                    }
                });
    }

    private void callLoginError(JSONObject object) {
        String msg = object.optString("msg",
                CommonsUtils.getXmlString(mContext, R.string.login_failed));
        ToastUtils.showShort(msg);
    }

}
