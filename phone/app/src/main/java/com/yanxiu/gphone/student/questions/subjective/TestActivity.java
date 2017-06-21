package com.yanxiu.gphone.student.questions.subjective;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/20 10:17.
 * Function :
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubjectiveFragment.SubJectiveMessage message= (SubjectiveFragment.SubJectiveMessage) getIntent().getSerializableExtra(SubjectiveFragment.RESULTCODE);
        List<String> list=new ArrayList<>();
        list.add("http://s1.sinaimg.cn/middle/83e953190774fd8808990&690");
        message.paths=list;
        EventBus.getDefault().post(message);
        TestActivity.this.finish();
    }
}
