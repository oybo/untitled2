package com.qkl.online.mining.app.ui.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * author：oyb on 2018/9/1 22:33
 * 自定义倒计时View
 */
public class GuideCountDownView extends AppCompatTextView {

    // 总共的时间-单位秒
    private int countTime = 10;

    // 定时器
    private CountDownTimer countDownTimer;

    public GuideCountDownView(Context context) {
        super(context);
    }

    public GuideCountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCountTime(int countTime) {
        this.countTime = countTime;
    }

    private void init() {
        countDownTimer = new CountDownTimer(countTime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                setText(countTime-- + " S");
            }

            @Override
            public void onFinish() {
                setText("0 S");

                if(mOnGuideCountDownListener != null) {
                    mOnGuideCountDownListener.finish();
                }
            }
        };
    }

    private OnGuideCountDownListener mOnGuideCountDownListener;

    public void setCountDownListener(OnGuideCountDownListener listener) {
        mOnGuideCountDownListener = listener;
    }

    public void startTimer() {
        setText(countTime + " S");
        init();
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public interface OnGuideCountDownListener {
        void finish();
    }

}
