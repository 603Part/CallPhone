package com.phone.call.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.phone.call.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ScrollLayout extends FrameLayout {

    private ScrollHandler mHandler;
    private MyAdapter mAdapter;
    private RecyclerView recyclerView;
    private int index;

    public ScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.scroll, this);
        mHandler = new ScrollHandler(this);
        mAdapter = new MyAdapter();
        recyclerView = findViewById(R.id.rvNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;       //拦截事件
    }

    public void setData(List<String> data) {
        mAdapter.setList(data);
        if (data != null && data.size() > 0) {
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    }

    public void setSpeed(int i) {
        index = i;
        getSpeed();
    }



    private void getSpeed(){
        recyclerView.smoothScrollBy(0,index);
        mHandler.sendEmptyMessageDelayed(0, 100);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 弱引用防止内存泄露
     */
    private static class ScrollHandler extends Handler {
        private WeakReference<ScrollLayout> view;

        public ScrollHandler(ScrollLayout mView) {
            view = new WeakReference<>(mView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (view.get() != null) {
                view.get().getSpeed();
            }
        }
    }

    private static class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> list;

        public MyAdapter() {
            list = new ArrayList<>();
        }

        public void setList(List<String> list) {
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindData(list.get(position % list.size()));
        }

        @Override
        public int getItemCount() {
            return list.size() > 0 ? Integer.MAX_VALUE : 0;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.item_home_text);
        }

        public void bindData(String content) {
            contentView.setText(content);
        }
    }

}
