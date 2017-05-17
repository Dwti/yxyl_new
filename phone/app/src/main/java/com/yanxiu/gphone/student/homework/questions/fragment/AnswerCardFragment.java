package com.yanxiu.gphone.student.homework.questions.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;

import java.util.ArrayList;

/**
 * Created by 戴延枫 on 2017/5/7.
 * 答题卡
 */

public class AnswerCardFragment extends Fragment {
    public interface OnCardItemSelectListener {
        void onItemSelect(BaseQuestion item);
    }
    private OnCardItemSelectListener mListener;
    public void setOnCardItemSelectListener(OnCardItemSelectListener listener) {
        mListener = listener;
    }

    public ArrayList<BaseQuestion> nodes;
    private Button goButton;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        goButton = (Button) v.findViewById(R.id.button_go);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(AnswerCardFragment.this).commit();
//                QALevelSingleton.bus.post("save answer event");
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setAdapter(new CardAdapter(getActivity()));
        return v;
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        public CardViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
    }

    private class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {
        private Context mContext;
        public CardAdapter(Context context) {
            mContext = context;
        }

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new CardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
            final BaseQuestion node = nodes.get(position);
//            holder.mTitleTextView.setText(node.numberStringForShow());//TODO: 2017/5/15  等设置题号逻辑融合进孙鹏的数据里后，再添加

            holder.mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemSelect(node);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return nodes.size();
        }
    }
}
