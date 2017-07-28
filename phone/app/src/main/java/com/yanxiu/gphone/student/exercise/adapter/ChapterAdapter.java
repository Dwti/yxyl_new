package com.yanxiu.gphone.student.exercise.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.adapter.BaseRetractRecycleAdapter;
import com.yanxiu.gphone.student.common.viewholder.BaseRetractRecycleViewHolder;
import com.yanxiu.gphone.student.exercise.bean.ChapterBean;
import com.yanxiu.gphone.student.util.ScreenUtils;

import java.util.List;

/**
 * Created by sp on 17-7-28.
 */

public class ChapterAdapter extends BaseRetractRecycleAdapter<ChapterBean,ChapterAdapter.ChapterViewHolder> {

    private List<ChapterBean> mData;

    public ChapterAdapter(Context context) {
        super(context);
    }

    public void setData(List<ChapterBean> list){
        setDefaultData(list);
        this.mData = list;
    }

    @NonNull
    @Override
    protected View onRetractCreateView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.item_chapter,parent,false);
    }

    @NonNull
    @Override
    protected ChapterViewHolder onRetractCreateViewHolder(View rootView) {
        return new ChapterViewHolder(rootView);
    }

    @NonNull
    @Override
    protected List<ChapterBean> onRetractCreateChildData(ChapterBean parentData, int position, int dataType) {
        return parentData.getChildren();
    }

    @Override
    protected void onRetractBindView(ChapterViewHolder holder, ChapterBean data, int position, int dataType) {
        holder.text.setText(data.getName());
//        if(data.getChildren() != null && data.getChildren().size() > 0){
//            if(data.isExpanded()){
//                showChildrenByPosition(position);
//            }else {
//                hideChildrenByPosition(position);
//            }
//        }
    }

    @Override
    protected int getMaxDataType() {
        return super.getMaxDataType();
    }

    @Override
    protected int getIntervalWidth() {
        return ScreenUtils.dpToPxInt(mContext,50);
    }

    class ChapterViewHolder extends BaseRetractRecycleViewHolder{

        public ImageView indicator;
        public TextView text;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            text = (TextView) itemView.findViewById(R.id.text);
            indicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expand = mData.get(getLayoutPosition()).isExpanded();
                    if(expand){
                        hideChildrenByPosition(getLayoutPosition());
                        expand = false;
                    }else {
                        showChildrenByPosition(getLayoutPosition());
                        expand = true;
                    }
                    mData.get(getLayoutPosition()).setExpanded(expand);
                }
            });
        }
    }
}
