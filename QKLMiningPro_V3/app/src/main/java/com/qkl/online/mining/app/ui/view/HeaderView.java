package com.qkl.online.mining.app.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qkl.online.mining.app.R;

/**
 * 自定义ActionBar ouyangbo
 */
public class HeaderView extends FrameLayout implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView mBackView;
    private TextView mTitleTxt;
    private TextView mRightTxt;

    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_header_layout, this);

        if (headerView != null) {
            mToolbar = headerView.findViewById(R.id.activity_public_toolbar);
            mTitleTxt = headerView.findViewById(R.id.activity_public_title_txt);
            mRightTxt = headerView.findViewById(R.id.activity_public_right_txt);
            mRightTxt.setOnClickListener(this);

            mBackView = headerView.findViewById(R.id.activity_public_back_view);
            mBackView.setOnClickListener(this);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 设置标题
     *
     * @param txt
     */
    public void setTitile(String txt) {
        mTitleTxt.setText(txt);
    }

    public String getTitle() {
        return mTitleTxt.getText().toString().trim();
    }

    public void setBackViewGone() {
        mBackView.setVisibility(View.GONE);
    }

    private OnRightListener mOnRightListener;

    public void setRightView(String str, OnRightListener listener) {
        mRightTxt.setText(str);
        mRightTxt.setVisibility(View.VISIBLE);
        mOnRightListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_public_back_view:
                try {
                    ((Activity) getContext()).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.activity_public_right_txt:
                // 右侧
                if (mOnRightListener != null) {
                    mOnRightListener.onClick();
                }
                break;
        }
    }

    public interface OnRightListener {
        void onClick();
    }

}
