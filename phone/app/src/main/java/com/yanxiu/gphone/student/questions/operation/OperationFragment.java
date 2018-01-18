package com.yanxiu.gphone.student.questions.operation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetterNew;
import com.yanxiu.gphone.student.util.StemUtil;
import com.yanxiu.gphone.student.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/12/25.
 */

public class OperationFragment extends AnswerSimpleExerciseBaseFragment {
    private OperationQuestion mQuestion;
    private View mRootView;
    private TextView mStem;
    private List<String> mImgUrls;
    private GridView mGridView;
    private OperationAdapter mAdapter;
    //TODO initDataWithAnswer里面没有加操作题的判断，会影响答题报告，逻辑跟主观题是一样的
    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (OperationQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((OperationQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView  = inflater.inflate(R.layout.fragment_operation,container,false);
        initView();
        initData();
        setQaNumber(mRootView);
        setQaName(mRootView);
        initComplexStem(mRootView,mQuestion);
        return mRootView;
    }

    private void initView() {
        mStem = (TextView) mRootView.findViewById(R.id.tv_stem);
        mGridView = (GridView) mRootView.findViewById(R.id.gridView);
    }

    private void initData() {
        mStem.setText(Html.fromHtml(mQuestion.getStem(),new HtmlImageGetterNew(mStem),null));
        mImgUrls = mQuestion.getOperateImgUrls();
        List<OperationBean> operationBeanList = new ArrayList<>();
        for(String url: mImgUrls){
            OperationBean bean = new OperationBean();
            bean.setImageUrl(url);
            String fileName;
            if(TextUtils.isEmpty(url)){
                fileName = SaveAnswerDBHelper.makeId(mQuestion);
            }else {
                fileName = SaveAnswerDBHelper.makeId(mQuestion) + StringUtil.getPictureName(url);
            }
            bean.setStoredFileName(fileName);
            operationBeanList.add(bean);
        }
        mAdapter = new OperationAdapter(operationBeanList);
        if(operationBeanList.size() > 1){
            mGridView.setNumColumns(2);
            mGridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
        }
        mGridView.setAdapter(mAdapter);
    }
}
