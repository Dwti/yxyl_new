package com.yanxiu.gphone.student.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.viewholder.BaseRetractRecycleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 14:23.
 * Function :
 */
public abstract class BaseRetractRecycleAdapter<M, T extends BaseRetractRecycleViewHolder> extends RecyclerView.Adapter<T> {

    private static final int DEFAULT_DATA_TYPE = 0;
    private static final int DEFAULT_INTERVAL_WIDTH = 130;
    private static final int MAX_DATA_TYPE = -1;

    protected Context mContext;
    private List<View> mViews = new ArrayList<>();
    private List<BaseRetractData> mData = new ArrayList<>();

    public BaseRetractRecycleAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * init data
     */
    protected void setDefaultData(List<M> list) {
        mData.clear();
        for (M m : list) {
            BaseRetractData retractData = new BaseRetractData();
            retractData.dataType = DEFAULT_DATA_TYPE;
            retractData.data = m;
            mData.add(retractData);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View rootView = mInflater.inflate(R.layout.adapter_baseretractrecycle, parent, false);
        ((FrameLayout) rootView.findViewById(R.id.root_view)).addView(onRetractCreateView(mInflater, parent, viewType));
        return onRetractCreateViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        BaseRetractData retractData = mData.get(position);
        int dataType = retractData.dataType;
        removeInterval(holder.mRetractLayout);
        addInterval(holder.mRetractLayout, dataType);
        onRetractBindView(holder, retractData.data, position, retractData.dataType);
    }

    private void removeInterval(LinearLayout layout) {
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = layout.getChildAt(0);
            mViews.add(view);
            layout.removeView(view);
        }
    }

    private void addInterval(LinearLayout layout, int count) {
        for (int i = 0; i < count; i++) {
            View view = null;
            if (mViews.size() > 0) {
                view = mViews.get(0);
                mViews.remove(0);
            }

            if (view == null) {
                view = new View(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(setIntervalWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(params);
            }

            layout.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * open the list
     */
    protected void showChildrenByPosition(int position) {
        BaseRetractData baseRetractData = mData.get(position);
        int dataType = baseRetractData.dataType + 1;
        List<M> Mlist = onRetractCreateChildData(baseRetractData.data, position, dataType);
        List<BaseRetractData> Rlist = new ArrayList<>();
        for (M data : Mlist) {
            BaseRetractData retractData = new BaseRetractData();
            retractData.dataType = dataType;
            retractData.data = data;
            Rlist.add(retractData);
        }
        position += 1;
        if (mData.size() <= position) {
            mData.addAll(Rlist);
        } else {
            mData.addAll(position, Rlist);
        }
        this.notifyItemRangeInserted(position, Rlist.size());
    }

    /**
     * close the list
     */
    protected void hideChildrenByPosition(int position) {
        BaseRetractData baseRetractData = mData.get(position);
        int dataType = baseRetractData.dataType + 1;
        List<BaseRetractData> Rlist = new ArrayList<>();
        boolean isNext = true;
        position += 1;
        while (isNext) {
            if (position < mData.size()) {
                BaseRetractData retractData = mData.get(position);
                if (retractData.dataType >= dataType) {
                    mData.remove(position);
                    Rlist.add(retractData);
                } else {
                    isNext = false;
                }
            } else {
                isNext = false;
            }
        }
        this.notifyItemRangeRemoved(position, Rlist.size());
    }

    /**
     * interval width
     */
    protected int setIntervalWidth() {
        return DEFAULT_INTERVAL_WIDTH;
    }

    /**
     * max data type
     */
    protected int setMaxDataType() {
        return MAX_DATA_TYPE;
    }

    protected abstract
    @NonNull
    View onRetractCreateView(LayoutInflater inflater, ViewGroup parent, int viewType);

    protected abstract
    @NonNull
    T onRetractCreateViewHolder(View rootView);

    protected abstract
    @NonNull
    List<M> onRetractCreateChildData(M parentData, int position, int dataType);

    protected abstract void onRetractBindView(T holder, M data, int position, int dataType);

    private class BaseRetractData {
        int dataType;
        M data;
    }
}
