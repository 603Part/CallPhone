package com.phone.call.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anderson.AndroidUtils;
import com.nispok.snackbar.Snackbar;
import com.phone.call.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by ${范泽宁} on 2018/5/16.
 */

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;
    ImageView nodata;
    LinearLayout linear;
    RelativeLayout relative;
    TextView money;
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        ((TextView) findViewById(R.id.title)).setText("查询余额");

        back = findViewById(R.id.back);
        nodata = findViewById(R.id.balance_nodata);
        linear = findViewById(R.id.balance_linear);
        relative = findViewById(R.id.balance_relative);
        money = findViewById(R.id.balance_money);

        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(this);

        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void getData() {
        RequestBody body = new FormBody.Builder().add("username", AndroidUtils.prefs.getString("PhoneNum", "")).add("password", AndroidUtils.prefs.getString("PassWord", "")).build();
        okhttp3.Request request = new okhttp3.Request.Builder().url("http://47.105.47.232/login.html").post(body).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                AndroidUtils.log.e("IOException: " + e.getMessage().toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String header = response.header("set-cookie");

                AndroidUtils.log.e("onResponse: " + header);

                List<String> cookies = response.headers("Set-Cookie");

                String cookie = "";

                for (int i = cookies.size() - 1; i >= 0; i--) {
                    cookie = cookies.get(i);
                }

                AndroidUtils.prefs.save("Cookie", cookie);

                okhttp3.Request redirectRequest = new okhttp3.Request.Builder().url("http://47.105.47.232/")
                        .addHeader("Cookie", cookie)
                        .build();

                okhttp3.Response response2 = client.newCall(redirectRequest).execute();
                String result = response2.body().string();

                AndroidUtils.log.e("onResponse: " + result);

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://47.105.47.232/account/queryIntegral")
                        .addHeader("Cookie", cookie)
                        .build();
                call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        String s = response.body().string();
                        if (s.equals("")) {
                            nodata.setVisibility(View.VISIBLE);
                            relative.setVisibility(View.GONE);
                            Snackbar.with(getApplicationContext())
                                    .text("没有余额.....")
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .animation(true)
                                    .show(BalanceActivity.this);
                        } else {
                            nodata.setVisibility(View.GONE);
                            relative.setVisibility(View.VISIBLE);
                            num = s;
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

                        }
                        AndroidUtils.log.e("response.body().string(): " + s);
                    }
                });
            }
        });
    }

    final OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            .cookieJar(new LocalCookieJar())
            .build();

    class LocalCookieJar implements CookieJar {
        List<Cookie> cookies;

        @Override
        public List<Cookie> loadForRequest(HttpUrl arg0) {
            if (cookies != null)
                return cookies;
            return new ArrayList<Cookie>();
        }

        @Override
        public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
            this.cookies = cookies;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    money.setText(num + "积分");
                    break;
            }
        }
    };
}
