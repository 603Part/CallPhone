package com.phone.call.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.phone.call.R;
import com.phone.call.adapter.ProfitAdapter;
import com.phone.call.adapter.TaskAdapter;
import com.phone.call.bean.ProfitBean;
import com.phone.call.bean.TaskBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${范泽宁} on 2018/5/16.
 */

public class TaskActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;

    ImageView nodata;

    RecyclerView recyclerView;

    TaskAdapter adapter;

    List<TaskBean> list;

    ImageView more;

    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ((TextView) findViewById(R.id.title)).setText("自动呼叫");

        back = findViewById(R.id.back);
        more = findViewById(R.id.more);
        more.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(this);
        more.setOnClickListener(this);

        nodata = findViewById(R.id.task_nodata);
        recyclerView = findViewById(R.id.task_recycle);


        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TaskAdapter(new ArrayList<TaskBean>());
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < 100; i++) {
            TaskBean taskBean = new TaskBean();
            taskBean.setAddress("石家庄");
            taskBean.setName("隔壁老王" + i);
            taskBean.setNum("1513006300" + i);
            list.add(taskBean);
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

            case R.id.more:
                if (popupMenu == null) {
                    popupMenu = new PopupMenu(this, view);
                    popupMenu.inflate(R.menu.task_menu);
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
                case R.id.call_phone:

                    return true;
                default:
                    return false;
            }
        }
    };
}
