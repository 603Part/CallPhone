package com.phone.call.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.phone.call.R;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;

    AppCompatButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ((TextView) findViewById(R.id.title)).setText("用户编辑");

        back = findViewById(R.id.back);
        save = findViewById(R.id.edit_save);

        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_save:
                Snackbar.with(getApplicationContext())
                        .text("正在修改.....")
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .animation(true)
                        .show(EditActivity.this);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
                break;
        }
    }
}
