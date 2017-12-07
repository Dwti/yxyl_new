package com.yanxiu.gphone.student.questions.readingcomplex;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.TopBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class ReadingRedoTopFragment extends TopBaseFragment {
    ReadingComplexQuestion mData;
    TextView mText;

    @Override
    public void setData(BaseQuestion data) {
        mData = (ReadingComplexQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mData = (ReadingComplexQuestion) savedInstanceState.getSerializable(ExerciseBaseFragment.KEY_NODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reading_top, container, false);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View root) {
        mText = (TextView) root.findViewById(R.id.tv_stem);
        String stem = mData.getStem();
        Spanned spanned = Html.fromHtml(stem,new HtmlImageGetter(mText),null);
        mText.setText(spanned);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mData);
    }

}
