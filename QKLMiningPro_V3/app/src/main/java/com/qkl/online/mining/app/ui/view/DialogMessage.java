package com.qkl.online.mining.app.ui.view;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.qkl.online.mining.app.R;
import com.qkl.online.mining.app.listener.NoDoubleClickListener;

/**
 * 自定义提示框
 */
public class DialogMessage extends AppCompatDialog {

    private Context mContext;

    private TextView mMessageTxt;

    public DialogMessage(Context context) {
        this(context, "");
    }

    public DialogMessage(Context context, String message) {
        super(context, R.style.signin_dialog_style);


        mContext = context;
        init();

        setMessage(message);

    }

    private void init() {
        setContentView(R.layout.dialog_message);
        Window window = getWindow();
        window.getAttributes().width = LayoutParams.MATCH_PARENT;
        window.getAttributes().height = LayoutParams.WRAP_CONTENT;
        window.getAttributes().gravity = Gravity.CENTER;

        mMessageTxt = findViewById(R.id.dialog_message);
        findViewById(R.id.dialog_ok).setOnClickListener(listener);
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_ok:
                    dismiss();
                    break;
            }
        }
    };

    public void setMessage(String message) {
        mMessageTxt.setText(message);
    }

}