package com.yanxiu.gphone.student.user.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PickerView;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/24 14:13.
 * Function :
 */
public class ChooseStageActivity extends YanxiuBaseActivity implements PickerView.onSelectListener, View.OnClickListener{

    private Context mContext;
    private PickerView mChooseStageView;

    private String[] stageIds=Constants.StageId;

    private String stageText;
    private String stageId;
    private TextView mTitleRightView;
    private ImageView mTitleLeftView;
    private View mTopView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,ChooseStageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=ChooseStageActivity.this;
        PublicLoadLayout rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_choosestage);
        rootView.finish();
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mTopView=findViewById(R.id.include_top);
        mTitleLeftView= (ImageView) findViewById(R.id.iv_left);
        mTitleRightView= (TextView) findViewById(R.id.tv_right);
        mChooseStageView= (PickerView) findViewById(R.id.pv_stage);
    }

    private void listener() {
        mTitleLeftView.setOnClickListener(ChooseStageActivity.this);
        mTitleRightView.setOnClickListener(ChooseStageActivity.this);
        mChooseStageView.setOnSelectListener(ChooseStageActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mTitleLeftView.setVisibility(View.VISIBLE);
        mTitleRightView.setVisibility(View.VISIBLE);
        mTitleRightView.setText(getText(R.string.ok));
        mTitleRightView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.selector_choose_ensure_bg));
        mChooseStageView.setData(getList());
    }

    private List<String> getList(){
        List<String> list=new ArrayList<>();
        int[] ids= Constants.StageTxtId;
        stageText=getText(ids[0]).toString();
        stageId=stageIds[0];
        for (int i=0;i<ids.length;i++){
            list.add(getText(ids[i]).toString());
        }
        return list;
    }

    @Override
    public void onSelect(View view, String text,int selectId) {
        switch (view.getId()){
            case R.id.pv_stage:
                stageText=text;
                stageId=stageIds[selectId];
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                CompleteInfoActivity.StageMessage message=new CompleteInfoActivity.StageMessage();
                message.stageId=stageId;
                message.stageText=stageText;
                EventBus.getDefault().post(message);
            case R.id.iv_left:
                ChooseStageActivity.this.finish();
                break;
        }
    }

}
