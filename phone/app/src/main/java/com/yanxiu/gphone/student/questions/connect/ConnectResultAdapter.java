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

public class ConnectResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ConnectedBean> mData;

    private OnItemDeletedListener mOnItemDeletedListener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_EMPTY = 1;

    private int mMaxPicWidth = 0;

    public void setOnItemDeteleListener(OnItemDeletedListener onItemDeletedListener) {
        this.mOnItemDeletedListener = onItemDeletedListener;
    }

    public ConnectResultAdapter(List<ConnectedBean> data) {
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_NORMAL){
            return new ConnectedItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connected,parent,false));
        }else {
            return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_basket_empty_view,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ConnectedItemViewHolder){
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    ((ConnectedItemViewHolder)holder).leftText.setText(Html.fromHtml(mData.get(position).getLeftItem().getText(),new HtmlImageGetter(((ConnectedItemViewHolder)holder).leftText),null));
                    ((ConnectedItemViewHolder)holder).rightText.setText(Html.fromHtml(mData.get(position).getRightItem().getText(),new HtmlImageGetter(((ConnectedItemViewHolder)holder).rightText),null));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size()==0?1:mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.size() == 0 ){
            return TYPE_EMPTY;
        }else {
            return TYPE_NORMAL;
        }
    }

    private void remove(int index){
        mData.remove(index);
        notifyItemRemoved(index);
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    class ConnectedItemViewHolder extends RecyclerView.ViewHolder{
        private TextView leftText, rightText;
        private View detele;
        public ConnectedItemViewHolder(View itemView) {
            super(itemView);
            leftText = (TextView) itemView.findViewById(R.id.textLeft);
            rightText = (TextView) itemView.findViewById(R.id.textRight);
            detele = itemView.findViewById(R.id.delete);

            detele.setOnClickListener(new View.OnClickListener() {
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

    class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemDeletedListener {
        void onDeleted(ConnectedBean bean);
    }
}
