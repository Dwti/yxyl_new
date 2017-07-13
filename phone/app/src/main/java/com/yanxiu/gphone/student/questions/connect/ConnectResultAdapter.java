package com.yanxiu.gphone.student.questions.connect;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.List;

/**
 * Created by sunpeng on 2017/7/13.
 */

public class ConnectResultAdapter extends RecyclerView.Adapter<ConnectResultAdapter.ConnectedItemViewHolder> {

    private List<ConnectedBean> mData;

    private OnItemDeletedListener mOnItemDeletedListener;

    public void setOnItemDeteleListener(OnItemDeletedListener onItemDeletedListener) {
        this.mOnItemDeletedListener = onItemDeletedListener;
    }

    public ConnectResultAdapter(List<ConnectedBean> data) {
        this.mData = data;
    }

    @Override
    public ConnectedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConnectedItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connected,parent,false));
    }

    @Override
    public void onBindViewHolder(ConnectedItemViewHolder holder, int position) {
        holder.mLeftText.setText(Html.fromHtml(mData.get(position).getLeftItem().getText(),new HtmlImageGetter(holder.mLeftText),null));
        holder.mRightText.setText(Html.fromHtml(mData.get(position).getRightItem().getText(),new HtmlImageGetter(holder.mRightText),null));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void remove(int index){
        mData.remove(index);
        notifyItemRemoved(index);
    }

    class ConnectedItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mLeftText,mRightText;
        private View mDetele;
        public ConnectedItemViewHolder(View itemView) {
            super(itemView);
            mLeftText = (TextView) itemView.findViewById(R.id.textLeft);
            mRightText = (TextView) itemView.findViewById(R.id.textRight);
            mDetele = itemView.findViewById(R.id.delete);
            mDetele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectedBean bean = mData.get(getLayoutPosition());
                    remove(getLayoutPosition());
                    if(mOnItemDeletedListener !=null){
                        mOnItemDeletedListener.onDeleted(bean);
                    }
                }
            });
        }
    }

    public interface OnItemDeletedListener {
        void onDeleted(ConnectedBean bean);
    }
}
