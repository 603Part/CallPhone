package com.phone.call.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anderson.AndroidUtils;
import com.phone.call.R;
import com.phone.call.ui.activity.ProfitActivity;
import com.phone.call.ui.activity.TaskActivity;
import com.phone.call.view.ScrollLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    AppCompatButton start;

    AppCompatButton stop;

    ScrollLayout layout;

    ImageView more;

    List<String> list;

    PopupMenu popupMenu;

    TextView profit;

    TextView task;

    int index = 100;

    String[] str = new String[]{"18330231657", "16330164565", "15330169565", "133365855",
            "133365855", "18330231657", "16330164565", "15330169565", "133365855", "133365855",
            "16330164565", "18330231657", "16330164565", "15330169565", "133365855", "133365855",
            "18330231657", "16330164565", "15330169565", "133365855", "133365855", "16330164565",
            "18330231657", "16330164565", "15330169565", "133365855", "133365855", "18330231657",
            "16330164565", "15330169565", "133365855", "133365855", "16330164565", "16330164565"};

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        ((TextView) rootView.findViewById(R.id.title)).setText("首页");
        more = rootView.findViewById(R.id.more);
        more.setVisibility(View.VISIBLE);
        start = rootView.findViewById(R.id.home_start);
        stop = rootView.findViewById(R.id.home_stop);
        layout = rootView.findViewById(R.id.scrollLayout);
        profit = rootView.findViewById(R.id.home_profit);
        task = rootView.findViewById(R.id.home_task);

        start.setOnClickListener(this);

        profit.setOnClickListener(this);

        task.setOnClickListener(this);

        stop.setOnClickListener(this);

        more.setOnClickListener(this);

        list = new ArrayList<>();

        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }

        layout.setData(list);

        getData1();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_start:
                layout.setSpeed(index);
                break;
            case R.id.home_profit:
                startActivity(new Intent(context, ProfitActivity.class));
                break;
            case R.id.home_task:
                startActivity(new Intent(context, TaskActivity.class));
                break;
            case R.id.home_stop:
                layout.setSpeed(0);
                break;
            case R.id.more:
                if (popupMenu == null) {
                    popupMenu = new PopupMenu(this.getActivity(), view);
                    popupMenu.inflate(R.menu.menu);
                    popupMenu.setOnMenuItemClickListener(listener);
                }
                popupMenu.show();
                break;
        }
    }

    private PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.refresh:
                    return true;

                case R.id.ten:
                    index = 100;
                    return true;

                case R.id.twenty:
                    index = 200;

                    return true;

                case R.id.forty:
                    index = 400;
                    return true;
                default:
                    return false;
            }

        }
    };

    public void getData1() {
        Request request = new Request.Builder()
                .url("http://47.105.47.232/order/pullOrder")
                .addHeader("SESSION", AndroidUtils.prefs.getString("Cookie", ""))
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d("sb", "onResponse:  " + string);
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
}
