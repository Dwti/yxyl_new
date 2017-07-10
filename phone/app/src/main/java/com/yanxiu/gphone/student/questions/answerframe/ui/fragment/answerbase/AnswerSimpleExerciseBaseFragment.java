package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.ListenerSeekBarLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

/**
 * Created by 戴延枫 on 2017/5/5.
 * 单题型Frament基类
 */

public abstract class AnswerSimpleExerciseBaseFragment extends AnswerExerciseBaseFragment {

    private ListenerSeekBarLayout mListenView;//听力复合题只有一个子题时，题干的听力控件
    private BaseQuestion mData;
    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        this.mData=node;
    }

    /**
     * 如果是只有一个子题的复合题，显示大题的题干
     * (所有单题型都需要支持该方法)
     *
     * @param view
     */
    public void initComplexStem(View view, BaseQuestion data) {
        //通用题干
        LinearLayout complex_stem_layout = (LinearLayout) view.findViewById(R.id.complex_stem_layout);
        TextView complex_stem = (TextView) view.findViewById(R.id.complex_stem);
        String complexStem = data.getStem_complexToSimple();
        if (!TextUtils.isEmpty(complexStem)) {
            Spanned spanned = Html.fromHtml(complexStem, new HtmlImageGetter(complex_stem), null);
            complex_stem.setText(spanned);
            complex_stem_layout.setVisibility(View.VISIBLE);
        }
        //听力题干的听力控件
        String template = data.getTemplate_complexToSimple();
        String url = data.getUrl_listenComplexToSimple();//听力url
        if (!TextUtils.isEmpty(template) && template.equals(QuestionTemplate.LISTEN)) {
            mListenView = (ListenerSeekBarLayout) view.findViewById(R.id.complex_stem_listen);
            mListenView.setVisibility(View.VISIBLE);
            mListenView.setUrl(url);
            mListenView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        super.onVisibilityChangedToUser(isVisibleToUser,invokeInResumeOrPause);
        if (isVisibleToUser) {
            if (null != mListenView) {
                mListenView.setResume();
                if (mData.mIsShouldPlay&&!mData.mIsPause){
                    mListenView.setPlayToProgress(mData.mProgress,mData.mMax);
                    mData.mIsShouldPlay=false;
                }else if (mData.mIsShouldPlay&&mData.mIsPause){
                    mListenView.setPauseToProgress(mData.mProgress,mData.mMax);
                    mData.mIsShouldPlay=false;
                }
            }
        } else {
            if (null != mListenView)
                mListenView.setPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mListenView) {
            mData.mProgress=mListenView.getProgress();
            mData.mMax=mListenView.getMax();
            mData.mIsShouldPlay=mListenView.getIsPlaying();
            mData.mIsPause=mListenView.getIsPause();
            mListenView.setDestory();
        }
    }
}
