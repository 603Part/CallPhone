package com.phone.call.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.phone.call.R;
import com.phone.call.ui.activity.BalanceActivity;
import com.phone.call.ui.activity.EditActivity;
import com.phone.call.ui.activity.ExtractActivity;
import com.phone.call.ui.activity.NewCardActivity;
import com.phone.call.ui.activity.TransferActivity;
import com.phone.call.ui.activity.UpdatePassActivity;

public class AccountFragment extends BaseFragment implements View.OnClickListener {

    AppCompatTextView edit;
    AppCompatTextView balance;
    AppCompatTextView more;
    AppCompatTextView update;
    AppCompatTextView extract;
    AppCompatTextView newCard;
    AppCompatTextView transfer;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {
        ((TextView) rootView.findViewById(R.id.title)).setText("账户管理");
        edit = rootView.findViewById(R.id.account_edit);
        balance = rootView.findViewById(R.id.account_balance);
        more = rootView.findViewById(R.id.account_more);
        update = rootView.findViewById(R.id.account_update);
        extract = rootView.findViewById(R.id.account_extract);
        newCard = rootView.findViewById(R.id.account_new_card);
        transfer = rootView.findViewById(R.id.account_transfer);
        edit.setOnClickListener(this);
        more.setOnClickListener(this);
        update.setOnClickListener(this);
        balance.setOnClickListener(this);
        extract.setOnClickListener(this);
        newCard.setOnClickListener(this);
        transfer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_edit:
//                startActivity(new Intent(context, EditActivity.class));
                Snackbar.with(context)
                        .text("敬请期待.....")
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .animation(true)
                        .show(getActivity());
                break;
            case R.id.account_transfer:
                startActivity(new Intent(context, TransferActivity.class));
                break;
            case R.id.account_new_card:
                startActivity(new Intent(context, NewCardActivity.class));
                break;
            case R.id.account_extract:
                Snackbar.with(context)
                        .text("敬请期待.....")
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .animation(true)
                        .show(getActivity());
                break;
            case R.id.account_update:
                startActivity(new Intent(context, UpdatePassActivity.class));
                break;
            case R.id.account_balance:
                startActivity(new Intent(context, BalanceActivity.class));
                break;
            case R.id.account_more:
                Snackbar.with(context)
                        .text("敬请期待.....")
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .animation(true)
                        .show(getActivity());
                break;
        }
    }
}
