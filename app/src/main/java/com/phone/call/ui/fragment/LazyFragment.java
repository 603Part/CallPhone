package com.phone.call.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * http://msdx.github.io/AndroidSnippet/com/githang/android/snippet/app/LazyFragment.html
 */
public abstract class LazyFragment extends Fragment {
    protected final int RESULT_CANCELED   = Activity.RESULT_CANCELED;
    protected final int RESULT_OK         = Activity.RESULT_OK;
    protected final int RESULT_FIRST_USER = Activity.RESULT_FIRST_USER;

    protected boolean isVisible;
    protected boolean isPrepared;

    public boolean canFirstLoad = true;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }
}
