package com.qkl.online.mining.app.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.UrlConfig;
import com.qkl.online.mining.app.data.entity.MyStar;
import com.qkl.online.mining.app.data.entity.StarProduct;
import com.qkl.online.mining.app.data.json.JsonCallback;
import com.qkl.online.mining.app.data.json.LzyResponse;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.view.IStarView;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 首页
 */

public class StarPresenter extends BasePresenter<IStarView> {

    public StarPresenter(Activity context, IStarView view) {
        super(context, view);
    }

    /**
     * 获取我的星球
     */
    public void getMyStar() {
        OkGo.<JSONObject>post(UrlConfig.getOrderInfo())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        // {"res":{"code":0,"msg":"Success"},"orderId":0,"orderAmount":0,"output":0,"status":0}
                        JSONObject jsonObject = response.body();
                        if (jsonObject != null) {
                            JSONObject resJSONObject = jsonObject.optJSONObject("res");
                            if (resJSONObject != null && resJSONObject.optInt("code") == 0) {
                                MyStar myStar = new MyStar();
                                myStar.setOrderId(jsonObject.optInt("orderId"));
                                myStar.setOrderAmount(jsonObject.optDouble("orderAmount"));
                                myStar.setOutput(jsonObject.optDouble("output"));
                                myStar.setStatus(jsonObject.optInt("status"));
                                mView.resultMyStar(myStar);
                                return;
                            }
                        }
                        mView.resultMyStar(null);
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        mView.resultMyStar(null);
                    }
                });
    }

    /**
     * 星球购买
     */
    public void orderBuy(int amount) {
        mView.showLoading();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<JSONObject>post(UrlConfig.getOrderBuy())
                .tag(this)
                .upJson(jsonObject)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        if (response != null) {
                            JSONObject object = response.body();
                            // {"res":{"code":3002,"msg":"Insufficient balance of deposit wallet"},"id":0}
                            if (object != null) {
                                object = object.optJSONObject("res");
                                if (object != null) {
                                    int code = object.optInt("code");
                                    if (code == 0) {
                                        String msg = AccountManager.getInstance().getConfigMsg(code);
                                        if (TextUtils.isEmpty(msg)) {
                                            msg = CommonsUtils.getXmlString(mContext, R.string.purchase_star_puchase_success_bt);
                                        }
                                        ToastUtils.showShort(msg);
                                        getMyStar();
                                        getOrderWalletStat();
                                    } else {
                                        String msg = AccountManager.getInstance().getConfigMsg(code);
                                        if (TextUtils.isEmpty(msg)) {
                                            msg = object.optString("msg");
                                        }
                                        ToastUtils.showShort(msg);
                                    }
                                }
                            }
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        mView.hideLoading();
                    }
                });
    }

    /**
     * 星球购买配置接口
     */
    public void getOrderConfig() {
        OkGo.<JSONObject>post(UrlConfig.getOrderConfig())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        if (response != null) {
                            // {"res":{"code":0,"msg":"Success"},"minAmount":100,"maxAmount":100000,"canBuy":false,"minAmountFormat":"0.1K","maxAmountFormat":"100K"}
                            JSONObject object = response.body();
                            if (object != null) {
                                JSONObject resJSONObject = object.optJSONObject("res");
                                if (resJSONObject != null && resJSONObject.optInt("code") == 0) {
                                    int minAmount = object.optInt("minAmount");
                                    int maxAmount = object.optInt("maxAmount");
                                    boolean canBuy = object.optBoolean("canBuy");
                                    String minAmountFormat = object.optString("minAmountFormat");
                                    String maxAmountFormat = object.optString("maxAmountFormat");

                                    mView.resultOrderConfig(minAmount, maxAmount, canBuy, minAmountFormat, maxAmountFormat);
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

    /**
     * 各个钱包统计
     */
    public void getOrderWalletStat() {
        OkGo.<JSONObject>post(UrlConfig.getOrderWalletStat())
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
                                    // reinvestAmount": 9000, // 复投钱包
                                    // "yunAmount": 0 // 提币钱包
                                    int reinvestAmount = object.optInt("reinvestAmount");
                                    int inAmount = object.optInt("inAmount");

                                    mView.resultOrderWalletStat(reinvestAmount, inAmount);
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

    /**
     * 获取矿机列表
     */
    public void getMineList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page", "1");
            jsonObject.put("limit", "10");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<LzyResponse<StarProduct>>post(UrlConfig.getMinerListUrl())
                .tag(this)
                .upJson(jsonObject)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<LzyResponse<StarProduct>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<StarProduct>> response) {
                        if (response != null) {
                            LzyResponse lzyResponse = response.body();
                            if (lzyResponse != null) {
                                StarProduct starProduct = (StarProduct) lzyResponse.data;
                                if (starProduct != null) {
                                    mView.resultMine(starProduct);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<LzyResponse<StarProduct>> response) {
                        super.onError(response);
                    }
                });
    }

    /**
     * 今日剩余星球数
     */
    public void todayStarRestNumber() {
        OkGo.<JSONObject>post(UrlConfig.getTodayStarRestNumberUrl())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        JSONObject object = response.body();
                        if (object != null) {
                            int code = object.optInt("code", -1);
                            if (code == 0) {
                                int data = object.optInt("data");
                                mView.resultStarRestNumber(data);
                                return;
                            }
                        }
                        mView.resultStarRestNumber(0);
                    }

                    @Override
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                        mView.resultStarRestNumber(0);
                    }
                });
    }

    /**
     * 购买星球
     */
    public void purchaseStar(String minerId) {
        mView.showLoading();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", String.valueOf(AccountManager.getInstance().getMemberId()));
            jsonObject.put("minerId", minerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<JSONObject>post(UrlConfig.getPurchaseStarUrl())
                .tag(this)
                .headers("Authorization", AccountManager.getInstance().getAccountToken())
                .upJson(jsonObject)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        mView.hideLoading();
                        JSONObject object = response.body();
                        if (object != null) {
                            object = object.optJSONObject("res");
                            int code = object.optInt("code", -1);
                            if (code == 0) {
                                // 购买成功
                                String msg = AccountManager.getInstance().getConfigMsg(code);
                                if (TextUtils.isEmpty(msg)) {
                                    msg = CommonsUtils.getXmlString(mContext, R.string.purchase_star_puchase_success_bt);
                                }
                                ToastUtils.showShort(msg);
                                mView.resultPurchaseStar();
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
                        ToastUtils.showShort(response.getException().getMessage());
                        mView.hideLoading();
                    }
                });
    }

}
