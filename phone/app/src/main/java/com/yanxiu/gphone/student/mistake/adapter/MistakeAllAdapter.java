package com.yanxiu.gphone.student.mistake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.mistake.request.MistakeDeleteQuestionRequest;
import com.yanxiu.gphone.student.questions.bean.PaperBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/17 10:22.
 * Function :
 */
public class MistakeAllAdapter extends RecyclerView.Adapter<MistakeAllAdapter.MistakeCompleteViewHolder> {

    public interface onItemClickListener {
        void onItemClick(View view, PaperBean paperBean, int position);
    }

    private PublicLoadLayout rootView;
    private LayoutInflater mInflater;
    private PaperBean mPaperBean;
    private List<PaperTestBean> mData = new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public MistakeAllAdapter(Context context,PublicLoadLayout rootView) {
        this.rootView=rootView;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(PaperBean data) {
        if (data == null||data.getPaperTest()==null) {
            return;
        }
        this.mPaperBean=data;
        mData.clear();
        mData.addAll(data.getPaperTest());
        this.notifyDataSetChanged();
    }

    public void addData(PaperBean data){
        if (data == null||data.getPaperTest()==null) {
            return;
        }
        mData.addAll(data.getPaperTest());
        this.notifyDataSetChanged();
    }

    public String getLastItemWqid(){
        if (mData.size()>0){
            return String.valueOf(mData.get(mData.size()-1).getWqid());
        }
        return "0";
    }

    public void deleteItem(int position,String id){
        if (position<mData.size()){
            if (mData.get(position).getId().equals(id)){
                deleteItem(position);
            }
        }
    }

    private void deleteItem(int position){
        this.mData.remove(position);
        this.notifyDataSetChanged();
    }

    public void addItemClickListener(onItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public MistakeCompleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_mistakeall, parent, false);
        return new MistakeCompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MistakeCompleteViewHolder holder, int position) {
        holder.mNameView.setText(mData.get(position).getQuestions().getStem());
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class MistakeCompleteViewHolder extends RecyclerView.ViewHolder {

        TextView mNameView;

        MistakeCompleteViewHolder(final View itemView) {
            super(itemView);
            mNameView= (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null&&mPaperBean!=null) {
                        mPaperBean.setPaperTest(mData);
                        mItemClickListener.onItemClick(itemView, mPaperBean, getLayoutPosition());
                    }
                }
            });
        }
    }

    private void setDeleteItem(final int position, String qid){
        rootView.showLoadingView();
        MistakeDeleteQuestionRequest deleteQuestionRequest=new MistakeDeleteQuestionRequest();
        deleteQuestionRequest.questionId=qid;
        deleteQuestionRequest.startRequest(EXueELianBaseResponse.class, new EXueELianBaseCallback<EXueELianBaseResponse>() {
            @Override
            protected void onResponse(RequestBase request, EXueELianBaseResponse response) {
                rootView.hiddenLoadingView();
                deleteItem(position);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }
}
