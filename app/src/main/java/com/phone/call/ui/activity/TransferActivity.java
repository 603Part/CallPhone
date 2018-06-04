package com.phone.call.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.anderson.AndroidUtils;
import com.nispok.snackbar.Snackbar;
import com.phone.call.R;
import com.phone.call.dialog.CommonDialog;
import com.zhy.http.okhttp.OkHttpUtils;

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

public class TransferActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;

    AppCompatButton save;

    int money;

    EditText cardNum;
    EditText cardMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        ((TextView) findViewById(R.id.title)).setText("积分转账");

        back = findViewById(R.id.back);
        save = findViewById(R.id.edit_save);
        cardNum = findViewById(R.id.transfer_card_num);
        cardMoney = findViewById(R.id.transfer_money);

        back.setVisibility(View.VISIBLE);


        back.setOnClickListener(this);
        save.setOnClickListener(this);
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_save:

                new CommonDialog(TransferActivity.this, "提示", R.style.Dialog_Tran, new CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm){
                            dialog.dismiss();
                        }else{
                            dialog.dismiss();
                            if (Integer.parseInt(cardMoney.getText().toString()) < money){
                                OkHttpUtils
                                        .post()
                                        .url("http://47.105.47.232/account/transfer")
                                        .addParams("pointsNum", cardMoney.getText().toString())
                                        .addParams("cardNum", cardNum.getText().toString())
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
                                                        .text("操作成功")
                                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                        .animation(true)
                                                        .show(TransferActivity.this);

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                    }
                                                }, 1500);
                                            }
                                        });
                            }else{
                                Snackbar.with(getApplicationContext())
                                        .text("请不要大于您所有的积分")
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .animation(true)
                                        .show(TransferActivity.this);
                            }
                        }
                    }
                }).setTitle("提示").setContent("系统将扣除1积分手续费,确定转账？").show();
                break;
        }
    }

    private void getData(){
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
                        AndroidUtils.log.e("response.body().string(): " + s);
                        if (s.equals("")) {
                            Snackbar.with(getApplicationContext())
                                    .text("当前未获取到数据.....")
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .animation(true)
                                    .show(TransferActivity.this);
                        } else {
                            money = Integer.parseInt(s);
                        }
                    }
                });
            }
        });
    }

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

    final OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            .cookieJar(new LocalCookieJar())
            .build();
}
