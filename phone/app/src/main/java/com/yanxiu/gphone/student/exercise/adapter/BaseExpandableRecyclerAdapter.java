package com.yanxiu.gphone.student.exercise.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.yanxiu.gphone.student.exercise.bean.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-7-31.
 */

public abstract class BaseExpandableRecyclerAdapter<T extends Node,VH extends ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> mData;
    protected OnItemClickListener mOnItemClickListener;


    public BaseExpandableRecyclerAdapter(List<T> data) {
        initLevel(data,-1);
        this.mData = data;
    }

    public void replaceData(List<T> data) {
        initLevel(data, -1);
        mData = data;
        notifyDataSetChanged();
    }

    public void expand(int position, boolean animation) {
        mData.get(position).setExpanded(true);
        List<T> dataToInsert = mData.get(position).getChildren();
        mData.addAll(position + 1, dataToInsert);
        if (animation) {
            notifyItemRangeInserted(position + 1, dataToInsert.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void collapse(int position, boolean animation) {
        mData.get(position).setExpanded(false);
        List<T> dataToRemove;
        if (animation) {
            dataToRemove = getAllNodesWithChildrenExpanded(mData.get(position).getChildren());
        } else {
            dataToRemove = getAllNodes(mData.get(position).getChildren());
        }
        //收起的时候，需要连子节点一块收起，设置展开状态为false，否则下次刷新，子节点的indicator状态显示不正确
        setExpand(dataToRemove, false);
        mData.removeAll(dataToRemove);
        if (animation) {
            notifyItemRangeRemoved(position + 1, dataToRemove.size());
        } else {
            notifyDataSetChanged();
        }
    }

    private void setExpand(List<T> data, boolean expand) {
        if (data == null)
            return;
        for (T node : data) {
            node.setExpanded(expand);
        }
    }

    public List<T> getAllNodes(List<T> data) {
        List<T> result = new ArrayList<>();
        for (T node : data) {
            result.add(node);
            if (node.getChildren() != null && node.getChildren().size() > 0)
                result.addAll(getAllNodes(node.getChildren()));
        }
        return result;
    }

    public List<T> getAllNodesWithChildrenExpanded(List<T> data) {
        List<T> result = new ArrayList<>();
        for (T node : data) {
            result.add(node);
            if (node.getChildren() != null && node.getChildren().size() > 0 && node.isExpanded())
                result.addAll(getAllNodes(node.getChildren()));
        }
        return result;
    }

    private void initLevel(List<T> data, int parentLevel) {
        for (T node : data) {
            node.setLevel(parentLevel + 1);
            if (node.getChildren() != null && node.getChildren().size() > 0) {
                initLevel(node.getChildren(), node.getLevel());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    interface OnItemClickListener<K extends Node>{
        void onItemClick(View itemView, int position, K k);
    }
}
