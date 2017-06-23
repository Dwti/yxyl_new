package com.yanxiu.gphone.student.questions.subjective;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.SimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.io.Serializable;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class SubjectiveFragment extends SimpleExerciseBaseFragment implements AlbumGridView.onClickListener, AlbumGridView.onItemChangedListener {

    public static final String RESULTCODE="code";

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
    public void onClick(int Type) {
        switch (Type){
            case AlbumGridView.TYPE_CAMERA:
                SubJectiveMessage message=new SubJectiveMessage();
                message.hashCode=SubjectiveFragment.this.hashCode();
                Intent intent=new Intent(getActivity(),TestActivity.class);
                intent.putExtra(RESULTCODE,message);
                startActivity(intent);
                break;
            case AlbumGridView.TYPE_IMAGE:
                break;
        }
    }

    @Override
    public void onChanged(List<String> paths) {
        mData.answerList.clear();
        if (paths!=null){
            mData.answerList.addAll(paths);
        }
    }

    public static class SubJectiveMessage implements Serializable {
        int hashCode;
        List<String> paths;
    }

    public void onEventMainThread(SubJectiveMessage message){
        if (message!=null&&message.hashCode==SubjectiveFragment.this.hashCode()){
            if (message.paths!=null){
                for (String path:message.paths){
                    mAnswerView.addData(path);
                }
            }
        }
    }

}
