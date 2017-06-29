package com.yanxiu.gphone.student.questions.answerframe.listener;

import com.test.yanxiu.network.RequestBase;

public interface SubmitAnswerCallback<T> {
    void onSuccess();

    void onFail();

    void onUpdate(int count, int index);

    void onDataError(String msg);

}
