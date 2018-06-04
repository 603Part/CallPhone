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

public class CommonDialog extends Dialog implements View.OnClickListener {

    private String content;
    private String text;
    private String positiveName;

    private Button submit;
    private Button cancel;
    private TextView message;
    private TextView title;

    private Context mContext;

    private OnCloseListener listener;

    public CommonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public CommonDialog setTitle(String title) {
        this.text = title;
        return this;
    }

    public CommonDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public CommonDialog(Context context, String content) {
        super(context, R.style.Dialog_Tran);
        this.mContext = context;
        this.content = content;
    }

    public CommonDialog(Context context, String content, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
        this.content = content;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CommonDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        submit = (Button) findViewById(R.id.button2);
        cancel = (Button) findViewById(R.id.button1);
        message = (TextView) findViewById(R.id.message);
        title = (TextView) findViewById(R.id.title);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        message.setText(content);
        title.setText(text);
        if (!TextUtils.isEmpty(positiveName)) {
            submit.setText(positiveName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                if (listener != null) {
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.button2:
                if (listener != null) {
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
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
