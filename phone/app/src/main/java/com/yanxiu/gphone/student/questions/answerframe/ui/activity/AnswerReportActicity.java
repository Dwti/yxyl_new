package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.http.request.AnswerReportRequest;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.user.activity.JoinClassActivity;
import com.yanxiu.gphone.student.user.activity.LoginActivity;
import com.yanxiu.gphone.student.user.response.LoginResponse;
import com.yanxiu.gphone.student.util.DESBodyDealer;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.constant.Constants.NOTES_KEY;
import static com.yanxiu.gphone.student.constant.Constants.PPID_KEY;

/**
 * Created by 戴延枫 on 2017/6/9.
 */

public class AnswerReportActicity extends YanxiuBaseActivity {

    private String mPPid;
    private AnswerReportRequest mRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initData();
        initView();
    }

    private void initView() {

    }

    private void initData() {
        mPPid = getIntent().getStringExtra(PPID_KEY);
        requestData();
    }
    private void requestData(){
        if(TextUtils.isEmpty(mPPid)){
            return;
        }
        if(mRequest != null){
            mRequest.cancelRequest();
            mRequest = null;
        }
        mRequest = new AnswerReportRequest(mPPid);
        mRequest.bodyDealer = new DESBodyDealer();
        mRequest.startRequest(PaperResponse.class, new HttpCallback<PaperResponse>() {
            @Override
            public void onSuccess(RequestBase request, PaperResponse ret) {
                if(ret.getStatus().getCode() == 0){
                    if(ret.getData().size() > 0){
                        Paper paper = new Paper(ret.getData().get(0), QuestionShowType.ANSWER);
//                        DataFetcher.getInstance().save(paper.getId(),paper);
                    }else {
                    }
                }else {
                    ToastManager.showMsg("666");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
            }
        });
    }
    /**
     * 跳转NotesActicity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String ppid) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(PPID_KEY, ppid);
        activity.startActivity(intent);
    }

    /**
     * 笔记封装数据
     */
    public static class NotesMessage {
        public int mViewHashCode;//接收view的id
        public String mNotesContent;//笔记内容

        public NotesMessage(int viewHashCode, String notesContent) {
            mViewHashCode = viewHashCode;
            mNotesContent = notesContent;
        }
    }
}
