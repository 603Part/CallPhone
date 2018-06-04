package com.phone.call.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.phone.call.R;
import com.phone.call.bean.ProfitBean;

import java.util.List;

/**
 * Created by ${范泽宁} on 2018/5/15.
 */

public class ProfitAdapter extends BaseQuickAdapter<ProfitBean, BaseViewHolder> {

    public ProfitAdapter(@Nullable List<ProfitBean> data) {
        super(R.layout.item_profit, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, ProfitBean item) {
        helper.setText(R.id.item_profit_name,item.getName());
        helper.setText(R.id.item_profit_date,item.getData());
        helper.setText(R.id.item_profit_money,item.getMoney());
        }
}