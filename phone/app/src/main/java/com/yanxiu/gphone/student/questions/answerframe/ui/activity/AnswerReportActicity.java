package com.yanxiu.gphone.student.questions.answerframe.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;

import de.greenrobot.event.EventBus;

import static com.yanxiu.gphone.student.constant.Constants.NOTES_KEY;

/**
 * Created by 戴延枫 on 2017/6/9.
 */

public class AnswerReportActicity extends YanxiuBaseActivity {

    private TextView mCommit;
    private EditText mNotesEditText;
    private String mNotesContent;
    private int mViewHashCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initData();
        initView();
    }

    private void initView() {
        mCommit = (TextView) findViewById(R.id.commit);
        mNotesEditText = (EditText) findViewById(R.id.notesContent);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotesContent = mNotesEditText.getText().toString();
                NotesMessage notesMessage = new NotesMessage(mViewHashCode,mNotesContent);
                EventBus.getDefault().post(notesMessage);
                finish();
            }
        });
    }

    private void initData() {
        mViewHashCode = getIntent().getIntExtra(NOTES_KEY, -1);
    }

    /**
     * 跳转NotesActicity
     *
     * @param activity
     */
    public static void invoke(Activity activity, int key) {
        Intent intent = new Intent(activity, AnswerReportActicity.class);
        intent.putExtra(NOTES_KEY, key);
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
