package com.phone.call.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.phone.call.R;
import com.phone.call.bean.ProfitBean;
import com.phone.call.bean.TaskBean;

import java.util.List;

/**
 * Created by ${范泽宁} on 2018/5/15.
 */

public class TaskAdapter extends BaseQuickAdapter<TaskBean, BaseViewHolder> {

    public TaskAdapter(@Nullable List<TaskBean> data) {
        super(R.layout.item_task, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, TaskBean item) {
        helper.setText(R.id.item_task_name,item.getName());
        helper.setText(R.id.item_task_num,item.getNum());
        helper.setText(R.id.item_task_address,item.getAddress());
        }
}