package com.phone.call.ui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.anderson.AndroidUtils;
import com.android.internal.telephony.ITelephony;
import com.nispok.snackbar.Snackbar;
import com.phone.call.R;
import com.phone.call.bean.BackBean;
import com.phone.call.nohttp.JavaBeanRequest;
import com.phone.call.ui.activity.PhoneTestActivity;
import com.phone.call.view.MyCheckBox;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.anderson.categories.system.getPackageName;

/**
 * Created by 浅子影 on 2018/6/3.
 */

public class HomeFragment2 extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    AppCompatButton start;
    AppCompatButton stop;
    String data;

    TelephonyManager manager;

    RadioButton fast;
    RadioButton commonly;
    RadioButton slow;
    RadioGroup group;

    TextView interval;
    TextView num;

    String phoneNum = "";

    int time = 40000;

    private RequestQueue requestQueue;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home2;
    }

    @Override
    protected void initView() {
        ((TextView) rootView.findViewById(R.id.title)).setText("首页");
        start = rootView.findViewById(R.id.home_start);
        stop = rootView.findViewById(R.id.home_stop);

        fast = rootView.findViewById(R.id.home_fast);
        commonly = rootView.findViewById(R.id.home_commonly);
        slow = rootView.findViewById(R.id.home_slow);
        group = rootView.findViewById(R.id.home_radio_group);
        interval = rootView.findViewById(R.id.home_interval);
        num = rootView.findViewById(R.id.home_num);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        group.setOnCheckedChangeListener(this);
        interval.setText("40");
        slow.setChecked(true);

        manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_start:
                getTask();
                handler.postDelayed(runnable, time);
                break;
            case R.id.home_stop:
                handler.removeCallbacks(runnable);
                break;
        }
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getTask();
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Snackbar.with(getContext())
                            .text("当前未获取到任务.....")
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .animation(true)
                            .show(getActivity());
                    break;
                case 2:
                    Snackbar.with(getContext())
                            .text("获取到任务.....")
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .animation(true)
                            .show(getActivity());

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 200);
                        }
                    } else {
                        callPhone();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == fast.getId()) {
            time = 10000;
            interval.setText("10");
        } else if (checkedId == commonly.getId()) {
            interval.setText("20");
            time = 20000;
        } else if (checkedId == slow.getId()) {
            time = 40000;
            interval.setText("40");
        }
    }

    private void callPhone() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + jsonObject.getString("targetNumber")));
            phoneNum = jsonObject.getString("targetNumber");
            num.setText(jsonObject.getString("inCome"));
            startActivity(intent);

            manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getTask() {
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
                        .url("http://47.105.47.232/order/pullOrder")
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
                        data = s;
                        AndroidUtils.log.e("response.body().string(): " + s);
                        if (s.equals("")) {
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    break;

                case TelephonyManager.CALL_STATE_RINGING:

                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    submitBack();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                        }
                    }, time);

                    break;
                default:
                    super.onCallStateChanged(state, incomingNumber);
                    break;
            }
        }
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
            .cookieJar(new HomeFragment2.LocalCookieJar())
            .build();

    private void submitBack() {
        OkHttpUtils
                .post()
                .url("http://47.105.47.232/order/completeOrder")
                .addParams("targetNumber", phoneNum)
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

                    }
                });
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("targetNumber", phoneNum);
//        JavaBeanRequest<BackBean> request = new JavaBeanRequest<>("http://47.105.47.232//order/completeOrder", RequestMethod.POST,BackBean.class);
//        request.set(params)
//                .setConnectTimeout(10 * 1000)
//                .setReadTimeout(20 * 1000);
//        requestQueue.add(0, request, new OnResponseListener<BackBean>() {
//            @Override
//            public void onStart(int what) {
//                AndroidUtils.log.e(what + "");
//            }
//
//            @Override
//            public void onSucceed(int what, Response<BackBean> response) {
//
//            }
//
//            @Override
//            public void onFailed(int what, Response<BackBean> response) {
//
//            }
//
//            @Override
//            public void onFinish(int what) {
//
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
    }
}
