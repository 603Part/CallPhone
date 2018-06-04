package com.phone.call.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.ChasingDots;
import com.nispok.snackbar.Snackbar;
import com.phone.call.receiver.MyReceiver;
import com.phone.call.R;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    ChasingDots chasingDots;

    TextView textView;

    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getSupportActionBar().hide();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.text);
        textView2 = findViewById(R.id.skip);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        chasingDots = new ChasingDots();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text:
                if (isSimCard(getApplicationContext())) {
                    Snackbar.with(getApplicationContext())
                            .text("正在准备测试,请稍后.....")
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .animation(true)
                            .show(this);

                    textView.setText("请稍后..");
                    chasingDots.setBounds(0, 0, 100, 100);
                    chasingDots.setColor(Color.WHITE);
                    textView.setCompoundDrawables(null, null, chasingDots, null);
                    chasingDots.start();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chasingDots.stop();
                            startActivity(new Intent(TestActivity.this, PhoneTestActivity.class));
                        }
                    }, 2000);
                } else {
                    Snackbar.with(getApplicationContext())
                            .text("请检查SIM卡.....")
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .animation(true)
                            .show(this);
                }

                break;
            case R.id.skip:
                startActivity(new Intent(TestActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView.setText("是否开始测试 ?");
    }

    private MyReceiver receiver = new MyReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public boolean isSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }
}
