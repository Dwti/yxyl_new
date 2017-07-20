package com.yanxiu.gphone.student.questions.answerframe.ui.fragment.wrongbase;

import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/18 15:14.
 * Function :
 */
public class WrongExercisbaseFragment extends ExerciseBaseFragment {

    public static final String TYPE_VIEWPAGER_LAST="last";
    public static final String TYPE_VIEWPAGER_NEXT="next";

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean invokeInResumeOrPause) {
        if (isVisibleToUser) {
            hiddenSwitchQuestionView();//这里有问题--acticity
        }
    }

    public boolean setViewPagerMove(String moveType){
        return false;
    }

    public boolean getIsFirst(){
        return true;
    }

    public boolean getIsEnd(){
        return true;
    }

}
