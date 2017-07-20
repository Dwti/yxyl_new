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
import java.util.TreeSet;

/**
 * Created by sunpeng on 2017/7/13.
 */

public class ConnectItemAdapter extends RecyclerView.Adapter<ConnectItemAdapter.ItemViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<ConnectItemBean> mData;
    private View mLastSelectedItem;
    private int mLastSelectedPos = -1;

    public ConnectItemAdapter(List<ConnectItemBean> data) {
        mData = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connect, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                holder.text.setText(Html.fromHtml(mData.get(position).getText(), new HtmlImageGetter(holder.text), null));
            }
        });
        holder.itemView.setTag(mData.get(position));
        if (position == mLastSelectedPos) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(int index) {
        mData.remove(index);
        mLastSelectedItem = null;
        mLastSelectedPos = -1;
        notifyItemRemoved(index);
    }

    public void add(ConnectItemBean itemBean) {
        int pos = computeInsertPosition(itemBean);
        mData.add(pos, itemBean);
        if (mLastSelectedPos > pos) {
            mLastSelectedPos += 1;
        }
        notifyItemInserted(pos);
    }

    public void addAll(List<ConnectItemBean> beanList) {
        mData.addAll(beanList);
        TreeSet<ConnectItemBean> treeSet = new TreeSet<>(mData);
        mData = new ArrayList<>(treeSet);
        if (mLastSelectedItem != null)
            mLastSelectedPos = mData.indexOf(mLastSelectedItem.getTag());
        notifyDataSetChanged();
    }

    public int getLastSelectedPosition() {
        return mLastSelectedPos;
    }

    private int computeInsertPosition(ConnectItemBean itemBean) {
        int result = mData.size();
        for (int i = 0; i < mData.size(); i++) {
            if (itemBean.getOriginPosition() < mData.get(i).getOriginPosition()) {
                result = i;
                break;
            }
        }
        return result;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLastSelectedPos = getLayoutPosition();
                    if (mLastSelectedItem != null)
                        mLastSelectedItem.setSelected(false);
                    itemView.setSelected(true);
                    mLastSelectedItem = itemView;
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(itemView, mData.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, ConnectItemBean itemBean, int position);
    }
}
