package com.phone.call.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.phone.call.R;

public class PhoneTestDialog extends Dialog implements View.OnClickListener {

    private TextView contentTxt;
    private TextView titleTxt;
    private Button submitTxt;
    private Button cancelTxt;
    private Button retry;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public PhoneTestDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PhoneTestDialog(Context context, String content) {
        super(context, R.style.Dialog_Tran);
        this.mContext = context;
        this.content = content;
    }

    public PhoneTestDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public PhoneTestDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected PhoneTestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public PhoneTestDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public PhoneTestDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public PhoneTestDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_phone_test);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        contentTxt = (TextView) findViewById(R.id.message);
        titleTxt = (TextView) findViewById(R.id.alertTitle);
        submitTxt = (Button) findViewById(R.id.button2);
        cancelTxt = (Button) findViewById(R.id.button1);
        retry = (Button) findViewById(R.id.button3);

        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);
        retry.setOnClickListener(this);

        contentTxt.setText(content);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (listener != null) {
                    listener.onClick(this, 1);
                }
                this.dismiss();
                break;
            case R.id.button2:
                if (listener != null) {
                    listener.onClick(this, 2);
                }
                break;

            case R.id.button3:
                if (listener != null) {
                    listener.onClick(this, 3);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, int index);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
