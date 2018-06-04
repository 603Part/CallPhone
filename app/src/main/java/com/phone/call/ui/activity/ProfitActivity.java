package com.phone.call.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nispok.snackbar.Snackbar;
import com.phone.call.R;
import com.phone.call.adapter.ProfitAdapter;
import com.phone.call.bean.ProfitBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${范泽宁} on 2018/5/16.
 */

public class ProfitActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;

    ImageView nodata;

    RecyclerView recyclerView;

    ProfitAdapter adapter;

    List<ProfitBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        ((TextView) findViewById(R.id.title)).setText("我的收益");

        back = findViewById(R.id.back);
        nodata = findViewById(R.id.balance_nodata);
        recyclerView = findViewById(R.id.profit_recycle);

        back.setVisibility(View.VISIBLE);

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProfitAdapter(new ArrayList<ProfitBean>());
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(this);

        for (int i = 0; i < 100; i++) {
            ProfitBean profitBean = new ProfitBean();
            profitBean.setData("2018" + " - " + i + " - " + i);
            profitBean.setMoney(i + "元");
            profitBean.setName("今日收益");
            list.add(profitBean);
        }

        adapter.setNewData(list);

        if (((int) (Math.random() * 10)) % 2 == 0) {

        } else {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
