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

public class CustomDialog extends Dialog implements View.OnClickListener{

    private TextView contentTxt;
    private TextView titleTxt;
    private Button submitTxt;
    private Button cancelTxt;

    private Context mContext;
    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public CustomDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomDialog(Context context, String content) {
        super(context, R.style.Dialog_Tran);
        this.mContext = context;
        this.content = content;
    }

    public CustomDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CustomDialog(Context context, int themeResId, String content, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.listener = listener;
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CustomDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CustomDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CustomDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = (TextView)findViewById(R.id.message);
        titleTxt = (TextView) findViewById(R.id.alertTitle);
        submitTxt = (Button)findViewById(R.id.button2);
        cancelTxt = (Button)findViewById(R.id.button1);
        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);

        contentTxt.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.button2:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener{
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
