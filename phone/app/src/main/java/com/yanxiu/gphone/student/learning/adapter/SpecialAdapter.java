package com.yanxiu.gphone.student.learning.adapter;

import android.support.v7.widget.RecyclerView;

import com.yanxiu.gphone.student.exercise.adapter.ExerciseExpandableAdapter;
import com.yanxiu.gphone.student.exercise.bean.KnowledgePointBean;

import java.util.List;

/**
 * Created by lufengqing on 2018/1/16.
 */

public class SpecialAdapter extends ExerciseExpandableAdapter<KnowledgePointBean> {
    public SpecialAdapter(List<KnowledgePointBean> data) {
        super(data);
    }

    @Override
    public void replaceData(List<KnowledgePointBean> data) {
        checkData(data);
        super.replaceData(data);
    }

    private void checkData(List<KnowledgePointBean> data){
        if (data!=null) {
            for (KnowledgePointBean bean_1 : data) {
                List<KnowledgePointBean> child_2=bean_1.getChildren();
                if (child_2!=null&&!child_2.isEmpty()){

                    for (KnowledgePointBean bean_2 : child_2) {
                        List<KnowledgePointBean> child_3=bean_2.getChildren();
                        if (child_3!=null&&!child_3.isEmpty()){

                            for (KnowledgePointBean bean_3 : child_3) {
                                List<KnowledgePointBean> child_4=bean_3.getChildren();
                                if (child_4!=null&&!child_4.isEmpty()){

                                    for (KnowledgePointBean bean_4 : child_4) {
                                        bean_4.setChildren(null);
                                    }

                                }
                            }

                        }
                    }

                }
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((ExpandableViewHolder)holder).text.setText(mData.get(position).getName());
    }
}
