package com.phone.call.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.phone.call.R;
import com.phone.call.interfaces.BaseActivity;
import com.phone.call.ui.fragment.AccountFragment;
import com.phone.call.ui.fragment.HomeFragment;
import com.phone.call.ui.fragment.HomeFragment2;
import com.phone.call.ui.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BaseActivity {

    SettingFragment settingFragment;
    HomeFragment2 homeFragment;
    AccountFragment accountFragment;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(getLayoutResId());
        initView(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment2();
                    transaction.add(R.id.frame_layout, homeFragment, "home");
                } else {
                    transaction.show(homeFragment);
                }
                break;

            case R.id.navigation_dashboard:
                if (accountFragment == null) {
                    accountFragment = new AccountFragment();
                    transaction.add(R.id.frame_layout, accountFragment, "account");
                } else {
                    transaction.show(accountFragment);
                }
                break;

            case R.id.navigation_notifications:
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.frame_layout, settingFragment, "setting");
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        return true;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initBeforeSetContentView() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        frameLayout = findViewById(R.id.frame_layout);

        homeFragment = (HomeFragment2) getSupportFragmentManager().findFragmentByTag("home");
        accountFragment = (AccountFragment) getSupportFragmentManager().findFragmentByTag("account");
        settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag("setting");

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (accountFragment != null) {
            transaction.hide(accountFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }
}
