package com.yanxiu.gphone.student.questions.operation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.operation.view.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2018/1/12.
 */

public class SelectColorAdapter extends BaseAdapter {
    private List<Integer> mColors = new ArrayList<>();
    private CircleView mSelectedColorView;

    public SelectColorAdapter(List<Integer> colors) {
        mColors = colors;
    }

    @Override
    public int getCount() {
        return mColors.size() ;
    }

    @Override
    public Object getItem(int position) {
        return mColors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color,parent,false);
        }
        CircleView circleView = (CircleView) convertView.findViewById(R.id.circleView);
        if(position == 0 && mSelectedColorView == null){
            mSelectedColorView = circleView;
            circleView.setCircleStyle(CircleView.CircleStyle.RING);
        }
        circleView.setCircleColor(mColors.get(position));
        return convertView;
    }

    public CircleView getSelectedColorView(){
        return mSelectedColorView;
    }

    public void setSelectedColorView(CircleView view){
        if(mSelectedColorView != view){
            mSelectedColorView = view;
        }
    }
}
