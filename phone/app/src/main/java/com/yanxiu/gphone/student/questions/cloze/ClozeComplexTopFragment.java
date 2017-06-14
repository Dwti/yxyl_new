package com.yanxiu.gphone.student.questions.cloze;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.TopBaseFragment;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * Created by 戴延枫 on 2017/6/14.
 */

public class ClozeComplexTopFragment extends TopBaseFragment {
    private ClozeComplexQuestion mData;
    private TextView mText;
    private boolean is = false;//Todo  demo展示，孙鹏需要删除这些示例代码

    @Override
    public void setData(BaseQuestion data) {
        mData = (ClozeComplexQuestion) data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mData = (ClozeComplexQuestion) savedInstanceState.getSerializable(ExerciseBaseFragment.KEY_NODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_cloze_top, container, false);
        initView(mRootView);
        return mRootView;
    }

    private void initView(View root) {
        mText = (TextView) root.findViewById(R.id.tv_stem);
        String stem = mData.getStem();
        Spanned spanned = Html.fromHtml(stem, new HtmlImageGetter(mText), null);
        mText.setText(spanned);

        //Todo  demo展示，孙鹏需要删除这些示例代码
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is) {
                    is = false;
                } else {
                    is = true;
                }
                showChildQuestion(is);
            }
        });
    }

    /**
     * 是否显示子题
     * @param isShow
     */
    private void showChildQuestion(boolean isShow) {
        Fragment fragment = getParentFragment();
        if (null != fragment && fragment instanceof ClozeComplexFragment) {
            ClozeComplexFragment parentFragment = (ClozeComplexFragment) fragment;
            if (isShow) {
                parentFragment.expand();
            } else {
                parentFragment.collapse();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mData);
    }

}
