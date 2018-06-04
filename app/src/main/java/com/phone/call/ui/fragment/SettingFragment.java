package com.phone.call.ui.fragment;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anderson.AndroidUtils;
import com.phone.call.R;
import com.phone.call.view.MyCheckBox;

public class SettingFragment extends BaseFragment implements  View.OnClickListener {

    AppCompatButton edit;
    AppCompatButton test;
    EditText number;
    EditText address;
    AppCompatCheckBox caller;
    AppCompatCheckBox numCard;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {
        ((TextView) rootView.findViewById(R.id.title)).setText("设置");
        edit = rootView.findViewById(R.id.setting_edit);
        test = rootView.findViewById(R.id.setting_test);
        number = rootView.findViewById(R.id.setting_number);
        address = rootView.findViewById(R.id.setting_address);
        caller = rootView.findViewById(R.id.setting_check_caller);
        numCard = rootView.findViewById(R.id.setting_check_num_card);

        caller.setChecked(true);
        numCard.setChecked(false);

        caller.setSelected(false);
        numCard.setSelected(false);

        edit.setOnClickListener(this);
        test.setOnClickListener(this);

        number.setText(AndroidUtils.prefs.getString("LocalNumber",""));
        address.setText(AndroidUtils.prefs.getString("Local",""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_edit:
                address.setFocusableInTouchMode(true);
                address.setFocusable(true);
                address.requestFocus();

                number.setFocusableInTouchMode(true);
                number.setFocusable(true);
                number.requestFocus();
                break;
            case R.id.setting_test:
                address.setFocusableInTouchMode(false);
                address.setFocusable(false);

                number.setFocusableInTouchMode(false);
                number.setFocusable(false);

                AndroidUtils.prefs.save("LocalNumber",number.getText().toString());
                AndroidUtils.prefs.save("Local",address.getText().toString());
                break;
        }
    }
}
