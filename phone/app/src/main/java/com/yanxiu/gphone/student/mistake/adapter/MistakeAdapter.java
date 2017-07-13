package com.yanxiu.gphone.student.mistake.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.adapter.BaseRetractRecycleAdapter;
import com.yanxiu.gphone.student.common.viewholder.BaseRetractRecycleViewHolder;
import com.yanxiu.gphone.student.mistake.response.MistakeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/13 9:25.
 * Function :
 */
public class MistakeAdapter extends BaseRetractRecycleAdapter<MistakeResponse,MistakeAdapter.MyViewHolder> {

    public MistakeAdapter(Context context) {
        super(context);
    }

    public void setData(){
        List<MistakeResponse> list=new ArrayList<>();
        for (int i=0;i<5;i++){
            MistakeResponse response=new MistakeResponse();
            list.add(response);
        }
        setDefaultData(list);
    }

    @NonNull
    @Override
    protected View onRetractCreateView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.adapter_mistake,parent,false);
    }

    @NonNull
    @Override
    protected MyViewHolder onRetractCreateViewHolder(View rootView) {
        return new MyViewHolder(rootView);
    }

    @NonNull
    @Override
    protected List<MistakeResponse> onRetractCreateChildData(MistakeResponse parentData, int position, int dataType) {
        List<MistakeResponse> list=new ArrayList<>();
        for (int i=0;i<5;i++){
            MistakeResponse response=new MistakeResponse();
            list.add(response);
        }
        return list;
    }

    @Override
    protected void onRetractBindView(MyViewHolder holder, MistakeResponse data, int position, int dataType) {
        holder.name.setText(data.test);
    }

    class MyViewHolder extends BaseRetractRecycleViewHolder{

        TextView name;
        TextView clear;
        public MyViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tv_name);
            clear= (TextView) itemView.findViewById(R.id.clear);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChildrenByPosition(getLayoutPosition());
                }
            });
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideChildrenByPosition(getLayoutPosition());
                }
            });
        }
    }
}
