package com.phone.call.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.phone.call.R;

public class TestDialog extends Dialog implements View.OnClickListener{

    private  String content;
    private Button submitTxt;
    private Button cancelTxt;

    private TextView message;
    private Context mContext;
    private OnCloseListener listener;
    private String positiveName;
    private String title;

    public TestDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public TestDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public TestDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public TestDialog(Context context, String content) {
        super(context, R.style.Dialog_Tran);
        this.mContext = context;
        this.content = content;
    }

    public TestDialog(Context context, String content,int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
        this.content = content;
    }

    protected TestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public TestDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_test);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        submitTxt = (Button)findViewById(R.id.button2);
        cancelTxt = (Button)findViewById(R.id.button1);
        message = (TextView)findViewById(R.id.message);
        submitTxt.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);
        message.setText(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
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
