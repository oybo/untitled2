package com.qkl.online.mining.app.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.data.entity.User;
import com.qkl.online.mining.app.mvp.presenter.PublicPresenter;
import com.qkl.online.mining.app.mvp.presenter.RegisterPresenter;
import com.qkl.online.mining.app.mvp.view.IPublicView;
import com.qkl.online.mining.app.mvp.view.IRegisterView;
import com.qkl.online.mining.app.ui.BaseActivity;
import com.qkl.online.mining.app.ui.view.CountDownTextView;
import com.qkl.online.mining.app.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：oyb on 2018/9/5 02:51
 * 设置YUN地址
 */
public class SetYunActivity extends BaseActivity<PublicPresenter> implements IPublicView, IRegisterView {

    @BindView(R.id.set_wallet_address_et)
    EditText mWalletAddressET;
    @BindView(R.id.set_input_code_et)
    EditText mPasswordCodeET;

    @BindView(R.id.register_email_txt)
    EditText mEmailET;
    @BindView(R.id.register_get_code_txt)
    CountDownTextView mCountDownTextView;
    @BindView(R.id.register_msg_code_txt)
    EditText mMsgCodeET;

    private RegisterPresenter mRegisterPresenter;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new PublicPresenter(this, this);
        mRegisterPresenter = new RegisterPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_set_yun;
    }

    @Override
    protected void initView() {
        initTopBarOnlyTitle(R.id.activity_set_yun_headerview, getXmlString(R.string.act_set_yun_title_txt));
    }

    @Override
    protected void initData() {
        User user = AccountManager.getInstance().getUser();
        if(user != null && !TextUtils.isEmpty(user.getEtcAddress())) {
            mWalletAddressET.setText(user.getEtcAddress());
            mWalletAddressET.setSelection(user.getEtcAddress().length());
        }
    }

    @OnClick(R.id.set_save_update)
    void update() {
        String address = mWalletAddressET.getText().toString().trim();
        String code = mPasswordCodeET.getText().toString().trim();
        String email = mEmailET.getText().toString().trim();
        String msgCode = mMsgCodeET.getText().toString().trim();
        if(TextUtils.isEmpty(address)) {
            ToastUtils.showShort(R.string.act_set_wallet_address_txt);
            return;
        }
        if(TextUtils.isEmpty(code)) {
            ToastUtils.showShort(R.string.act_set_input_money_code_txt);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showToast(R.string.register_email_unable_null);
            return;
        }
        if (TextUtils.isEmpty(msgCode)) {
            showToast(R.string.register_code_unable_null);
            return;
        }

        mPresenter.setYunAddress(address, code, msgCode);
    }

    @OnClick(R.id.register_get_code_txt)
    void getMsgCode() {
        // 获取验证码
        String email = mEmailET.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showToast(R.string.register_email_unable_null);
            return;
        }

        if (!mCountDownTextView.isCountDown()) {
            mRegisterPresenter.getCode(email, 4, mCountDownTextView);
        }
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
    public void result(boolean success) {

    }

}
