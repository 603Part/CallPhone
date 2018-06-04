package com.phone.call.ui.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.phone.call.R;

public abstract class BaseFragment extends LazyFragment {

    protected View rootView;

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    public int pageIndex = 1;

    protected Context context;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        context = getContext();
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResId(), container, false);
            initView();
            isPrepared = true;
            canFirstLoad = true;
            if (useLazyLoad()) {
                lazyLoad();
            } else {
                getData();
            }
        }
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !canFirstLoad) {
            return;
        }
        if (needEveryTimeGetData()) {
            pageIndex = 1;
            getData();
        } else {
            onFirstLoad();
        }
    }

    protected void onFirstLoad() {
        getData();
        canFirstLoad = false;
    }

    protected boolean useLazyLoad() {
        return false;
    }

    protected boolean needEveryTimeGetData() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void getData() {

    }

    protected abstract int getLayoutResId();

    protected abstract void initView();
}
