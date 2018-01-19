package com.yanxiu.gphone.student.questions.operation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.util.FileUtil;

import java.util.List;

/**
 * Created by sunpeng on 2018/1/8.
 */

public class OperationAdapter extends BaseAdapter {
    private List<OperationBean> mData;

    public OperationAdapter(List<OperationBean> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operation,parent,false);
        }
        final OperationBean operationBean = mData.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_content);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tips);
        Button button = (Button) convertView.findViewById(R.id.btn_operate);
        if(TextUtils.isEmpty(operationBean.getImageUrl())){
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            button.setText("开始作答");
            button.setBackgroundResource(R.drawable.selector_operation_button_green_bg);
        }else {
            textView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            String picPath = operationBean.getStoredFilePath() + PaletteActivity.SUFFIX;
            if(OperationUtils.hasStoredBitmap(picPath)){
                button.setText("修改");
                button.setBackgroundResource(R.drawable.selector_operation_button_gray_bg);
                Glide.with(parent.getContext()).load(picPath).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.image_load_failed).into(imageView);
            }else {
                button.setText("开始作答");
                button.setBackgroundResource(R.drawable.selector_operation_button_green_bg);
                Glide.with(parent.getContext()).load(operationBean.getImageUrl()).asBitmap().error(R.drawable.image_load_failed).into(imageView);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaletteActivity.invoke(parent.getContext(),operationBean.getStoredFilePath(),operationBean.getImageUrl());
            }
        });
        return convertView;
    }
}
