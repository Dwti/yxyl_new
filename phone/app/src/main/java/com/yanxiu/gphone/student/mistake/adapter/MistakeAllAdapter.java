package com.yanxiu.gphone.student.mistake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.bean.PaperBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.QuestionBean;
import com.yanxiu.gphone.student.util.HtmlImageGetter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/17 10:22.
 * Function :
 */
public class MistakeAllAdapter extends RecyclerView.Adapter<MistakeAllAdapter.MistakeCompleteViewHolder> {

    public interface onItemClickListener {
        void onItemClick(View view, PaperBean paperBean, int position);
    }

    private LayoutInflater mInflater;
    private PaperBean mPaperBean;
    private List<PaperTestBean> mData = new ArrayList<>();
    private onItemClickListener mItemClickListener;

    public MistakeAllAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(PaperBean data) {
        if (data == null || data.getPaperTest() == null) {
            return;
        }
        this.mPaperBean = data;
        mData.clear();
        mData.addAll(data.getPaperTest());
        this.notifyDataSetChanged();
    }

    public void addData(PaperBean data) {
        if (data == null || data.getPaperTest() == null) {
            return;
        }
        mData.addAll(data.getPaperTest());
        this.notifyDataSetChanged();
    }

    public String getLastItemWqid() {
        if (mData.size() > 0) {
            return String.valueOf(mData.get(mData.size() - 1).getWqid());
        }
        return "0";
    }

    public void deleteItem(int position, String id) {
        if (position < mData.size()) {
            if (mData.get(position).getQid().equals(id)) {
                this.mData.remove(position);
                this.notifyDataSetChanged();
            }
        }
    }

    public void addItemClickListener(onItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public MistakeCompleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_mistakeall, parent, false);
        return new MistakeCompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MistakeCompleteViewHolder holder, int position) {
        PaperTestBean paperTestBean = mData.get(position);
        Spanned string = Html.fromHtml(paperTestBean.getQuestions().getStem(), new HtmlImageGetter(holder.mContentView), null);
        holder.mContentView.setText(string);
        holder.mSubjectNameView.setText(QuestionUtil.getQuestionTypeNameByParentTypeId(Integer.parseInt(paperTestBean.getTypeid())));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    class MistakeCompleteViewHolder extends RecyclerView.ViewHolder {

        TextView mSubjectNameView;
        ImageView mDeleteView;
        TextView mContentView;

        MistakeCompleteViewHolder(final View itemView) {
            super(itemView);
            mSubjectNameView = (TextView) itemView.findViewById(R.id.tv_subject_name);
            mDeleteView = (ImageView) itemView.findViewById(R.id.iv_delete);
            mContentView = (TextView) itemView.findViewById(R.id.tv_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null && mPaperBean != null) {
                        mPaperBean.setPaperTest(mData);
                        mItemClickListener.onItemClick(itemView, mPaperBean, getLayoutPosition());
                    }
                }
            });
            mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnItemDelete itemDelete = new OnItemDelete();
                    itemDelete.position = getLayoutPosition();
                    itemDelete.questionId = mData.get(itemDelete.position).getQid();
                    EventBus.getDefault().post(itemDelete);
                }
            });
        }
    }

    public class OnItemDelete {
        public int position;
        public String questionId;
    }

}
