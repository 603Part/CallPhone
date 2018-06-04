package com.phone.call.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.phone.call.dialog.PhoneTestDialog;
import com.phone.call.receiver.MyReceiver;
import com.phone.call.R;
import com.phone.call.custom.NodeProgress;
import com.phone.call.dialog.TestDialog;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

public class PhoneTestActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    AppCompatButton confirm;
    NodeProgress nodeProgress;
    EditText edit;
    ImageView back;
    MyReceiver receiver;
    TelephonyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_test);

        ((TextView) findViewById(R.id.title)).setText("测试向导");
        back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        confirm = findViewById(R.id.confirm);
        nodeProgress = findViewById(R.id.node_progress);
        edit = findViewById(R.id.phone_test_edit);
        edit.addTextChangedListener(this);
        confirm.setOnClickListener(this);
        back.setOnClickListener(this);

        receiver = new MyReceiver();
        IntentFilter iFilter = new IntentFilter("finish");
        registerReceiver(receiver, iFilter);

        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (edit.length() < 11) {
                    Snackbar.with(getApplicationContext())
                            .text("请填写正确的电话号码.....")
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .animation(true)
                            .show(this);
                } else {
                    new TestDialog(this, edit.getText().toString() + "响铃后,请立即接听来电,本机将尝试自动挂断", R.style.Dialog_Tran, new TestDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                            } else {
                                if (ContextCompat.checkSelfPermission(PhoneTestActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(PhoneTestActivity.this, Manifest.permission.CALL_PHONE)) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    } else {
                                        ActivityCompat.requestPermissions(PhoneTestActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 200);
                                    }
                                } else {
                                    callPhone();
                                }
                            }
                        }
                    }).setTitle("提示").show();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 11) {
            nodeProgress.startForward();
        } else {
            nodeProgress.startBack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + edit.getText().toString()));
        startActivity(intent);

        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
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

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            endCall();
                        }
                    }, 15000);

                    break;
                default:
                    super.onCallStateChanged(state, incomingNumber);
                    break;
            }
        }
    }

    public void endCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();
        } catch (NoSuchMethodException e) {

        } catch (ClassNotFoundException e) {

        } catch (Exception e) {

        }

        new PhoneTestDialog(PhoneTestActivity.this, R.style.Dialog_Tran, "本机是否在另一部手机接听后立即自动挂断？(选错将导致严重后果，如果没看清除请重试)", new PhoneTestDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, int index) {
                switch (index) {
                    case 1:
                        Snackbar.with(getApplicationContext())
                                .text("成功通过测试.....")
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .animation(true)
                                .show(PhoneTestActivity.this);
                        startActivity(new Intent(PhoneTestActivity.this, LoginActivity.class));
                        sendBroadcast(new Intent("finish"));
                        finish();
                        break;
                    case 2:
                        Snackbar.with(getApplicationContext())
                                .text("测试已取消.....")
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .animation(true)
                                .show(PhoneTestActivity.this);
                        break;
                    case 3:
                        callPhone();
                        break;
                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }
}
