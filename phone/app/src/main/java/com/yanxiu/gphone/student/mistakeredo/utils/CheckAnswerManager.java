package com.yanxiu.gphone.student.mistakeredo.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.mistakeredo.request.CheckAnswerRequest;
import com.yanxiu.gphone.student.mistakeredo.response.CheckAnswerResponse;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/12/20 14:48.
 * Function :
 */
public class CheckAnswerManager {

    public interface onCheckAnswerListener{
        void onCheckAnswerStart();
        void onCheckAnswerSuccess(BaseQuestion question,CheckAnswerResponse response);
        void onCheckAnswerError(String error);
        void onCheckAnswerEnd();
    }

    private static final String CORRECTANS="correctAns";
    private static final String TEMPLATE="template";
    private static final String TYPEID="typeId";
    private static final String USERANS="userAns";

    private onCheckAnswerListener mCheckAnswerListener;

    private CheckAnswerManager(){}

    public static CheckAnswerManager create(){
        return new CheckAnswerManager();
    }

    public void setCheckAnswerListener(onCheckAnswerListener checkAnswerListener){
        this.mCheckAnswerListener=checkAnswerListener;
    }

    public void start(final BaseQuestion question){

        if (mCheckAnswerListener!=null){
            mCheckAnswerListener.onCheckAnswerStart();
        }

        Gson gson=new Gson();
        JSONArray array=new JSONArray();

        boolean isShouldRequest=false;

        if (question.isComplexQuestion()) {
            List<BaseQuestion> children = question.getChildren();
            for (BaseQuestion child:children){
                if(!child.getTemplate().equals(QuestionTemplate.ANSWER)) {
                    String param = getParams(child, gson);
                    if (TextUtils.isEmpty(param)) {
                        return;
                    }
                    isShouldRequest=true;
                    array.put(param);
                }
            }
        }else {
            String param=getParams(question,gson);
            if (TextUtils.isEmpty(param)){
                return;
            }
            isShouldRequest=true;
            array.put(param);
        }

        if (!isShouldRequest){
            if (mCheckAnswerListener!=null){
                mCheckAnswerListener.onCheckAnswerSuccess(question,null);
                mCheckAnswerListener.onCheckAnswerEnd();
            }
            return;
        }
        CheckAnswerRequest answerRequest=new CheckAnswerRequest();
        answerRequest.answers=array.toString();
        answerRequest.startRequest(CheckAnswerResponse.class, new HttpCallback<CheckAnswerResponse>() {
            @Override
            public void onSuccess(RequestBase request, CheckAnswerResponse ret) {
                if (mCheckAnswerListener!=null){
                    mCheckAnswerListener.onCheckAnswerSuccess(question,ret);
                    mCheckAnswerListener.onCheckAnswerEnd();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (mCheckAnswerListener!=null){
                    mCheckAnswerListener.onCheckAnswerError(error.getMessage());
                    mCheckAnswerListener.onCheckAnswerEnd();
                }
            }
        });
    }

    private String getParams(BaseQuestion question,Gson gson){
        String template=question.getTemplate();
        String typeId=question.getType_id();
        String correctAns=gson.toJson(question.getBean().getQuestions().getAnswer());
        try {
            JSONArray array=new JSONArray(correctAns);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String userAns=gson.toJson(question.getAnswer());


        if (question.getTemplate().equals(QuestionTemplate.CLASSIFY)) {
            List<String> answerList = new ArrayList<>();
            List<List<String>> listList = (List<List<String>>) question.getAnswer();
            for (List<String> stringList : listList) {
                StringBuilder answerString = new StringBuilder();
                for (String s : stringList) {
                    answerString.append(s).append(",");
                }
                if (!TextUtils.isEmpty(answerString.toString())) {
                    answerString = new StringBuilder(answerString.substring(0, answerString.length() - 1));
                }
                answerList.add(answerString.toString());
            }
            userAns=gson.toJson(answerList);
        }

        try {
            JSONArray array=new JSONArray(userAns);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject object=new JSONObject();
            object.put(TEMPLATE,template);
            object.put(TYPEID,typeId);
            object.put(CORRECTANS,correctAns);
            object.put(USERANS,userAns);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
