package com.phone.call.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.anderson.AndroidUtils;
import com.phone.call.R;
import com.phone.call.interfaces.BaseActivity;

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
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, BaseActivity {

    Button login;
//    TextView register;
//    TextView forget;

    private static final String TAG = "MainActivity";
    private EditText account;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeSetContentView();
        setContentView(getLayoutResId());
        initView(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (!account.getText().toString().equals("") && !password.getText().toString().equals(""))
                    getData();

                break;
//            case R.id.login_register:
//                startActivity(new Intent(this, RegisterActivity.class));
//                break;
//            case R.id.login_forget:
//                startActivity(new Intent(this, ForgetActivity.class));
//                break;
        }
    }

    private void getData() {
        RequestBody body = new FormBody.Builder().add("username", account.getText().toString()).add("password", password.getText().toString()).build();
        Request request = new Request.Builder().url("http://47.105.47.232/login.html").post(body).build();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "IOException: " + e.getMessage().toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String header = response.header("set-cookie");

                Log.d(TAG, "onResponse: " + header);

                List<String> cookies = response.headers("Set-Cookie");

                String cookie = "";

                for (int i = cookies.size() - 1; i >= 0; i--) {
                    cookie = cookies.get(i);
                }

                AndroidUtils.prefs.save("Cookie",cookie);

                Request redirectRequest = new Request.Builder().url("http://47.105.47.232/")
                        .addHeader("Cookie", cookie)
                        .build();

                okhttp3.Response response2 = client.newCall(redirectRequest).execute();
                String result = response2.body().string();
                Log.d(TAG, "onResponse: " + result);

                Request request = new Request.Builder()
                        .url("http://47.105.47.232/findAll")
                        .addHeader("Cookie", cookie)
                        .build();
                call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        Log.d(TAG, "onResponse: " + s);
                        AndroidUtils.prefs.save("PhoneNum",account.getText().toString());
                        AndroidUtils.prefs.save("PassWord",password.getText().toString());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    final OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            .cookieJar(new com.phone.call.ui.activity.LoginActivity.LocalCookieJar())
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

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
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
        login = findViewById(R.id.login);
        account = findViewById(R.id.login_account);
        password = findViewById(R.id.login_password);
//        register = findViewById(R.id.login_register);
//        forget = findViewById(R.id.login_forget);
        login.setOnClickListener(this);
//        register.setOnClickListener(this);
//        forget.setOnClickListener(this);
    }
}
