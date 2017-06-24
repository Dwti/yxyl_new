package com.yanxiu.gphone.student.questions.subjective;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.common.Bean.PhotoDeleteBean;
import com.yanxiu.gphone.student.common.activity.CameraActivity;
import com.yanxiu.gphone.student.common.activity.PhotoActivity;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.SimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class SubjectiveFragment extends SimpleExerciseBaseFragment implements AlbumGridView.onClickListener, AlbumGridView.onItemChangedListener {


    private SubjectiveQuestion mData;
    private TextView mQuestionView;
    private AlbumGridView mAnswerView;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData= (SubjectiveQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData ==null) {
            setData((SubjectiveQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(SubjectiveFragment.this);
        View view=inflater.inflate(R.layout.fragment_subjective,container,false);
        setQaNumber(view);
        setQaName(view);
        initView(view);
        initComplexStem(view,mData);
        initData();
        listener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(SubjectiveFragment.this);
    }

    private void initView(View view) {
        mQuestionView= (TextView) view.findViewById(R.id.tv_question);
        mAnswerView= (AlbumGridView) view.findViewById(R.id.ag_image);
    }

    private void initData() {
        Spanned string= Html.fromHtml(mData.getStem(),new HtmlImageGetter(mQuestionView),null);
        mQuestionView.setText(string);
        mAnswerView.setData(mData.answerList);
    }

    private void listener() {
        mAnswerView.addClickListener(this);
        mAnswerView.addItemChangedListener(this);
    }

    @Override
    public void onClick(int Type,int position) {
        switch (Type){
            case AlbumGridView.TYPE_CAMERA:
                CameraActivity.LaunchActivity(getContext(),SubjectiveFragment.this.hashCode());
                break;
            case AlbumGridView.TYPE_IMAGE:
                PhotoActivity.LaunchActivity(getContext(),mData.answerList,position,SubjectiveFragment.this.hashCode());
                break;
        }
    }

    @Override
    public void onChanged(ArrayList<String> paths) {
        mData.answerList.clear();
        if (paths!=null){
            mData.answerList.addAll(paths);
        }
        if (mData.answerList.size()>0){
            mData.setIsAnswer(true);
        }else {
            mData.setIsAnswer(false);
        }
        saveAnswer(mData);
        updateProgress();
    }

    public void onEventMainThread(CameraActivity.CameraCallbackMessage message){
        if (message!=null&&message.fromId==SubjectiveFragment.this.hashCode()){
            if (message.paths!=null){
                for (String path:message.paths){
                    mAnswerView.addData(path);
                }
            }
        }
    }

    public void onEventMainThread(PhotoDeleteBean deleteBean){
        if (deleteBean!=null&&deleteBean.formId==SubjectiveFragment.this.hashCode()){
            mAnswerView.remove(deleteBean.deleteId);
        }
    }

}
