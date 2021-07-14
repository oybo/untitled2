package com.qkl.online.mining.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.manager.AccountManager;
import com.qkl.online.mining.app.ui.view.HeaderView;
import com.qkl.online.mining.app.ui.view.browse.ProgressBarWebView;
import com.qkl.online.mining.app.utils.CommonsUtils;
import com.qkl.online.mining.app.utils.IntentUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：oyb on 2018/9/5 19:08
 */
public class WebViewActivity extends AppCompatActivity implements ProgressBarWebView.OnWebViewListener {

    @BindView(R.id.activity_webview_headerview)
    HeaderView mHeaderView;
    @BindView(R.id.activity_webview_webview)
    ProgressBarWebView mWebView;

    private int isMust;
    private String rightTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //初始化ButterKnife
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");
        boolean header = intent.getBooleanExtra("header", false);
        isMust = intent.getIntExtra("isMust", -1);
        rightTxt = intent.getStringExtra("rightTxt");

        initView(title);

        Map<String, String> headers = null;
        if (header) {
            try {
                headers = new HashMap<>();
                headers.put("language", CommonsUtils.getLanguage());
                headers.put("from", "android");
                headers.put("Authorization", AccountManager.getInstance().getAccountToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (!TextUtils.isEmpty(url)) {
            if (headers != null) {
                mWebView.loadUrl(url, headers);
            } else {
                mWebView.loadUrl(url);
            }
        }

        if (isMust == 1) {
            // 强制任务, 重新登录
            mHeaderView.setBackViewGone();
            mHeaderView.setRightView(rightTxt, new HeaderView.OnRightListener() {
                @Override
                public void onClick() {
                    IntentUtil.ToLoginActivity(WebViewActivity.this);
                    finish();
                }
            });
        } else if (isMust == 0) {
            // 非强制任务， 返回主页
            mHeaderView.setRightView(rightTxt, new HeaderView.OnRightListener() {
                @Override
                public void onClick() {
                    onBackPressed();
                }
            });
        }

        mWebView.setWebViewTitleListener(this);
    }

    private void initView(String title) {
        mHeaderView.setTitile(title);
        mHeaderView.getToolbar().setTitle("");
        setSupportActionBar(mHeaderView.getToolbar());
    }

    @Override
    public void onBackPressed() {
        if (isMust == -1) {
            super.onBackPressed();
        }
    }

    @Override
    public void onReceivedTitle(String title) {
        mHeaderView.setTitile(title);
    }

    @Override
    public void onLoadFinish() {

    }
}
