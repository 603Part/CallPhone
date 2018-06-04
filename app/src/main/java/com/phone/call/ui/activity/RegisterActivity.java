package com.phone.call.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.call.R;
import com.phone.call.interfaces.BaseActivity;

/**
 * Created by ${范泽宁} on 2018/5/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, BaseActivity {

    TextView title;
    TextView code;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeSetContentView();
        setContentView(getLayoutResId());
        initView(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.register_code:
                new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                code.setText(millisUntilFinished / 1000 + " 秒重新发送");
                                code.setEnabled(false);
                            }

                            public void onFinish() {
                                code.setText("重新发送");
                                code.setEnabled(true);
                            }
                        }.start();
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    public void initBeforeSetContentView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        title.setText("注册账号");
        code = findViewById(R.id.register_code);
        back.setOnClickListener(this);
        code.setOnClickListener(this);
    }
}
