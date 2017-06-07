package com.yanxiu.gphone.student.questions.answerframe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.adapter.AnswerCardAdapter;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.listener.OnAnswerCardItemSelectListener;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/5/7.
 * 答题卡
 */

public class AnswerCardFragment extends Fragment {
    private ArrayList<BaseQuestion> mQuestions;
    private Button goButton;
    private RecyclerView recyclerView;
    private AnswerCardAdapter mAnswerCardAdapter;
    private OnAnswerCardItemSelectListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_card, container, false);
        goButton = (Button) view.findViewById(R.id.button_go);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(AnswerCardFragment.this).commit();
//                QALevelSingleton.bus.post("save answer event");
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mAnswerCardAdapter = new AnswerCardAdapter(getActivity(), mListener);
        mAnswerCardAdapter.setData(mQuestions);
        recyclerView.setAdapter(mAnswerCardAdapter);
        return view;
    }

    public void setData(ArrayList<BaseQuestion> questions){
        mQuestions = allNodesThatHasNumber(questions);

    }
    /**
     * 给答题卡设置题号
     * @return
     */
    private ArrayList<BaseQuestion> allNodesThatHasNumber(ArrayList<BaseQuestion> questions) {
        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
        for (BaseQuestion node : questions) {
            retNodes.addAll(node.allNodesThatHasNumber());
        }
        return retNodes;
    }

    public void setOnCardItemSelectListener(OnAnswerCardItemSelectListener listener) {
        mListener = listener;
    }
}
