package com.phone.call.interfaces;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

/**
 * Created by ${范泽宁} on 2018/5/14.
 */

public interface BaseActivity {
    @LayoutRes
    int getLayoutResId();
    void initBeforeSetContentView();
    void initView(Bundle savedInstanceState);
}
