package com.qkl.online.mining.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.data.commons.Constants;
import com.qkl.online.mining.app.data.commons.UrlConfig;
import com.qkl.online.mining.app.data.entity.Deposit;
import com.qkl.online.mining.app.data.event.EventBase;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.mvp.presenter.LoginPresenter;
import com.qkl.online.mining.app.mvp.view.ILoginView;
import com.qkl.online.mining.app.ui.BaseActivity;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.IntentUtil;
import com.qkl.online.mining.app.utils.glide.GlideImageLoader;
import com.qkl.online.mining.app.utils.languagelib.MultiLanguageUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author：oyb on 2018/8/29 12:31
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    @BindView(R.id.activity_login_headerview)
    View mBarView;
    @BindView(R.id.login_loginname_et)
    EditText mLoginNameET;
    @BindView(R.id.login_password_et)
    EditText mPasswordET;
    @BindView(R.id.login_see_password_view)
    ImageView mSeePasswordET;
    @BindView(R.id.login_yanzhengma_layout)
    LinearLayout mYanZhengMaLayout;
    @BindView(R.id.login_yanzhengma_et)
    EditText mYanZhengMaET;
    @BindView(R.id.login_yanzhengma_view)
    ImageView mYanZhengMaImageView;

    @BindView(R.id.login_set_language_txt)
    TextView mSetLanguageTxt;

    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new LoginPresenter(this, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        setTitleBar(mBarView);
        setStatusTransparent();

        if (Hawk.contains(Constants.LOGIN_NAME_KEY)) {
            String loginName = Hawk.get(Constants.LOGIN_NAME_KEY).toString();
            mLoginNameET.setText(loginName);
            mLoginNameET.setSelection(loginName.length());
            mYanZhengMaLayout.setVisibility(View.VISIBLE);
            getCaptcha();
        }

        mSetLanguageTxt.setText(getLanguageValue());
    }

    @Override
    protected void initData() {
//        mLoginNameET.setText("huodbdev@qq.com");
//        mPasswordET.setText("123123");
//        mLoginNameET.setText("huodbdev@qq.com");
//        mPasswordET.setText("123123");
//        mLoginNameET.setText("160787616@qq.com");
//        mPasswordET.setText("QIN123123");

//        mLoginNameET.setText("411831087@qq.com");
//        mPasswordET.setText("qweQWE123");

        AccountManager.getInstance().checkAppUpdate(this);
    }

    @Override
    protected void onEventCallback(EventBase eventBase) {
        super.onEventCallback(eventBase);
        String action = eventBase.getAction();
        if (Constants.UPDATE_KEY.equals(action)) {
            AccountManager.getInstance().checkAppUpdate(this);
        }
    }

    @OnClick(R.id.login_go_login)
    void goLogin() {
        String loginName = mLoginNameET.getText().toString().trim();
        String password = mPasswordET.getText().toString().trim();
        String captcha = mYanZhengMaET.getText().toString().trim();

        if (TextUtils.isEmpty(loginName)) {
            showToast(R.string.login_input_loginname);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast(R.string.login_input_password);
            return;
        }
        if (TextUtils.isEmpty(captcha)) {
            if (mYanZhengMaLayout.getVisibility() == View.GONE) {
                mYanZhengMaLayout.setVisibility(View.VISIBLE);
                getCaptcha();
                return;
            }
            showToast(R.string.register_input_code);
            return;
        }

        mPresenter.login(loginName, password, captcha);
    }

    @OnClick(R.id.login_yanzhengma_view)
    void getCaptcha() {
        String loginName = mLoginNameET.getText().toString().trim();
        if (!TextUtils.isEmpty(loginName)) {
            String imageUrl = UrlConfig.getLoginCaptchaUrl(loginName);
            GlideImageLoader.loadYanZhengMaImage(this, imageUrl, mYanZhengMaImageView);
        }
    }

    private boolean isVisible;

    @OnClick(R.id.login_see_password_view)
    void seePassword() {
        if (isVisible) {
            mPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mPasswordET.setSelection(mPasswordET.getText().toString().length());
        isVisible = !isVisible;
        checkYan();
    }

    @OnClick(R.id.login_to_forgetpassword_view)
    void forgetPassword() {
        // 忘记密码
        IntentUtil.ToForgetPasswordActivity(this);
    }

    @OnClick(R.id.login_to_register_view)
    void register() {
        // 注册
        IntentUtil.ToRegisterUserActivity(this);
    }

    @OnClick(R.id.login_set_language_txt)
    void setLanguage() {
        IntentUtil.ToSetLanguageActivity(this);
    }

    private void checkYan() {
        if (isVisible) {
            mSeePasswordET.setImageResource(R.drawable.login_icon_zhengyan);
        } else {
            mSeePasswordET.setImageResource(R.drawable.login_icon_biyan);
        }
    }

    @Override
    public void showLoading() {
        showLoading(true, getXmlString(R.string.login_loading_in));
    }

    @Override
    public void hideLoading() {
        showLoading(false);
    }

    @Override
    public void loginSuccess(Deposit deposit) {
        // 保存用户名
        Hawk.put(Constants.LOGIN_NAME_KEY, mLoginNameET.getText().toString().trim());
        Hawk.put(Constants.LOGIN_PASSWORD_KEY, mPasswordET.getText().toString().trim());

        hideLoading();

        if (deposit != null) {
            String rightTxt;
            if (deposit.getIsMust() != 1) {
                IntentUtil.ToMainActivity(this);
                rightTxt = CommonsUtils.getXmlString(this, R.string.deposit_back_home);
            } else {
                rightTxt = CommonsUtils.getXmlString(this, R.string.deposit_back_login);
            }
            IntentUtil.ToWebViewActivity(this, "", deposit.getGoUrl(), true, deposit.getIsMust(), rightTxt);
        } else {
            IntentUtil.ToMainActivity(this);
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentUtil.ToSetLanguageActivityCode && resultCode == Activity.RESULT_OK) {
            recreate();
//            restartAct();
        }
    }

    private String getLanguageValue() {
        String str = "";
        int languageType = MultiLanguageUtil.getInstance().getLanguageType();
        switch (languageType) {
            case 1:
                str = getXmlString(R.string.language_type_simple_cn);
                break;
            case 2:
                str = getXmlString(R.string.language_type_traditional_cn);
                break;
            case 3:
                str = getXmlString(R.string.language_type_english);
                break;
        }
        return str;
    }

    /**
     * 重启当前Activity
     */
    private void restartAct() {
        Intent _Intent = new Intent(this, LoginActivity.class);
        startActivity(_Intent);        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);
        finish();
    }

}
