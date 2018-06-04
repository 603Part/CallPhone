package com.phone.call.ui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.phone.call.R;

public class ExtractActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;

    AppCompatButton save;

    TextView code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        ((TextView) findViewById(R.id.title)).setText("积分提取");

        back = findViewById(R.id.back);
        save = findViewById(R.id.edit_save);

        back.setVisibility(View.VISIBLE);

        code = findViewById(R.id.extract_code);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        code.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.extract_code:
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
            case R.id.edit_save:
                Snackbar.with(getApplicationContext())
                        .text("正在提取.....")
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .animation(true)
                        .show(ExtractActivity.this);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
                break;
        }
    }
}
