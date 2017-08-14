package com.yanxiu.gphone.student.questions.classify;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.util.TextTypefaceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 戴延枫
 * 归类题 adapter
 */

public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.AnswerCardViewHolder> {

    private Context mContext;

    private ClassifyQuestion mQuestion;

    private ArrayList<String> mClassifyBasketList;

    private OnItemClickLitener mOnItemClickLitener;

    public ClassifyAdapter(Context context) {
        mContext = context;
    }

    public void setData(ClassifyQuestion question) {
        mQuestion = question;
        mClassifyBasketList = mQuestion.getClassifyBasket();
    }

    @Override
    public AnswerCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.classify_basket_item, parent, false);
        return new AnswerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerCardViewHolder holder, final int position) {
        String name = mClassifyBasketList.get(position);
        int count = 0;
        if(mQuestion.getAnswerList().size() > position && null != mQuestion.getAnswerList().get(position)){
            count= mQuestion.getAnswerList().get(position).size();
        }
        Log.e("dyf", name + "----------->" +mQuestion.getAnswerList().toString());
//        for (int i = 0; i < mQuestion.getAnswerList().size(); i++) {
//            List<String> list = mQuestion.getAnswerList().get(i);
//            Log.e("dyf", name + "----------->" +list.toString());
//        }
        generateQuestionNumber(name, count ,holder);
        holder.classify_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLitener != null) {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            }
        });
    }

    /**
     * 生成题号
     */
    private void generateQuestionNumber(String name, int count ,AnswerCardViewHolder holder) {
        holder.classify_basket.setText(name);
        holder.classify_item_count.setText(count + "");
    }

    @Override
    public int getItemCount() {
        return mClassifyBasketList.size();
    }


    class AnswerCardViewHolder extends RecyclerView.ViewHolder {
        //        private RelativeLayout mQuestion_number;
//        private WavesLayout mWavesLayout;
        private TextView classify_basket;
        private TextView classify_item_count;

        public AnswerCardViewHolder(View itemView) {
            super(itemView);
            classify_basket = (TextView) itemView.findViewById(R.id.classify_basket);
            classify_item_count = (TextView) itemView.findViewById(R.id.classify_item_count);
//            mWavesLayout.setOtherView(mQuestion_number);
            classify_basket.setTag(classify_item_count);
            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, classify_item_count);
        }
    }

    /**
     * 归类题，归类篮子ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}


