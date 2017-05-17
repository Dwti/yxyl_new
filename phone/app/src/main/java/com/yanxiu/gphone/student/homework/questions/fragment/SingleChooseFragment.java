package com.yanxiu.gphone.student.homework.questions.fragment;


import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;

/**
 * Created by 戴延枫 on 2017/5/5.
 */

public class SingleChooseFragment extends SimpleExerciseFragmentBase {
//    SingleChooseQuestion model;

    @Override
    public void setNode(BaseQuestion node) {
//        model = (SingleChooseQuestion) node;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            model = (SingleChooseQuestion) savedInstanceState.getSerializable(KEY_NODE);
//        }
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable(KEY_NODE, model);
//    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_single_choose, container, false);
//        setQaNumber(view);
//        return view;
//    }


    /**
     * 当Fragment对用户的可见性发生了改变的时候就会回调此方法
     *
     * @param isVisibleToUser                      true：用户能看见当前Fragment；false：用户看不见当前Fragment
     * @param isHappenedInSetUserVisibleHintMethod true：本次回调发生在setUserVisibleHintMethod方法里；false：发生在onResume或onPause方法里
     */
    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser, boolean isHappenedInSetUserVisibleHintMethod) {

    }
}