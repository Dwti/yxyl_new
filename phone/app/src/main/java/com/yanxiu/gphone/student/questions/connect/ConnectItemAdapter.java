package com.yanxiu.gphone.student.questions.connect;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/7/13.
 */

public class ConnectItemAdapter extends RecyclerView.Adapter<ConnectItemAdapter.ItemViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<ConnectItemBean> mData;
    private View mLastSelectedItem;
    private int mLastSelectedPos = -1;

    public ConnectItemAdapter(List<String> texts) {
        mData = new ArrayList<>();
        for(int i=0;i<texts.size();i++){
            mData.add(new ConnectItemBean(texts.get(i),i));
        }

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mText.setText(Html.fromHtml(mData.get(position).getText(),new HtmlImageGetter(holder.mText),null));
        if(position == mLastSelectedPos){
            holder.itemView.setSelected(true);
        }else {
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(int index){
        mData.remove(index);
        mLastSelectedItem=null;
        mLastSelectedPos= -1;
        notifyItemRemoved(index);
    }

    public void add(ConnectItemBean itemBean){
        int pos = computeInsertPosition(itemBean);
        mData.add(pos,itemBean);
        if(mLastSelectedPos > pos){
            mLastSelectedPos+=1;
        }
        notifyDataSetChanged();
    }

    public int getLastSelectedPosition(){
        return mLastSelectedPos;
    }

    private int computeInsertPosition(ConnectItemBean itemBean){
        int result = mData.size();
        for(int i=0;i<mData.size();i++){
            if(itemBean.getOriginPosition() < mData.get(i).getOriginPosition()){
                result = i;
                break;
            }
        }
        return result;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLastSelectedPos = getLayoutPosition();
                    if(mLastSelectedItem != null)
                        mLastSelectedItem.setSelected(false);
                    itemView.setSelected(true);
                    mLastSelectedItem = itemView;
                    if(mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(itemView,mData.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View itemView,ConnectItemBean itemBean,int position);
    }
}
