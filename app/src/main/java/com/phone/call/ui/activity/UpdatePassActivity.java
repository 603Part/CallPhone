package com.phone.call.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.phone.call.R;
import com.phone.call.interfaces.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * Created by ${范泽宁} on 2018/5/17.
 */

public class UpdatePassActivity extends AppCompatActivity implements View.OnClickListener, BaseActivity {

    TextView title;
    ImageView back;
    AppCompatButton btn;
    EditText pass,pass2;

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
            case R.id.update_pass_btn:
                    if (!pass.getText().toString().equals("") && !pass2.getText().toString().equals("")){
                        OkHttpUtils
                                .post()
                                .url("http://47.105.47.232/account/changePassword")
                                .addParams("oldPassword", pass.getText().toString())
                                .addParams("newPassword", pass2.getText().toString())
                                .build()
                                .execute(new com.zhy.http.okhttp.callback.Callback() {
                                    @Override
                                    public Object parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
                                        return null;
                                    }

                                    @Override
                                    public void onError(Call call, Exception e, int id) {

                                    }

                                    @Override
                                    public void onResponse(Object response, int id) {
                                        Snackbar.with(getApplicationContext())
                                                .text("修改成功")
                                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                .animation(true)
                                                .show(UpdatePassActivity.this);

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 1500);
                                    }
                                });
                    }
                break;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_update_pass;
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
        btn = findViewById(R.id.update_pass_btn);
        pass = findViewById(R.id.update_pass);
        pass2 = findViewById(R.id.update_pass2);
        title.setText("修改密码");
        back.setOnClickListener(this);
        btn.setOnClickListener(this);
    }
}
