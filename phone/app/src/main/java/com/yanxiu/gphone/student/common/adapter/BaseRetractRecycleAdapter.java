package com.yanxiu.gphone.student.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.viewholder.BaseRetractRecycleViewHolder;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/12 14:23.
 * Function :
 */
public abstract class BaseRetractRecycleAdapter<M extends Object,T extends BaseRetractRecycleViewHolder> extends RecyclerView.Adapter<T> {

    protected Context mContext;

    public BaseRetractRecycleAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        View rootView=mInflater.inflate(R.layout.adapter_baseretractrecycle,parent,false);
        ((FrameLayout)rootView.findViewById(R.id.root_view)).addView(onRetractCreateView(mInflater,parent,viewType));
        return onRetractCreateViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    protected abstract View onRetractCreateView(LayoutInflater mInflater,ViewGroup parent, int viewType);
    protected abstract T onRetractCreateViewHolder(View rootView);

    protected abstract M onRetractCreateData();

    private class BaseRetractData{
        int mDataType;
        M mData;
    }
}
