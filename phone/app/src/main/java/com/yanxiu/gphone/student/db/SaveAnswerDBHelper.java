package com.yanxiu.gphone.student.db;


import com.yanxiu.gphone.student.questions.answerframe.bean.AnswerBean;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.videoplay.VideoConfigBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class SaveAnswerDBHelper {

    public static boolean save(AnswerBean bean) {
        String aid = bean.getAid();
        DataSupport.deleteAll(AnswerBean.class, "aid = ? ", aid);//每次存储前，先把相同id的数据删除
        return bean.save();
    }

//    public static String getAnswerJson2(long id) {
//        String json = null;
//        AnswerBean answerBean = DataSupport.find(AnswerBean.class, id);
//        if (null != answerBean) {
//            json = answerBean.getAnswerJson();
//        }
//        return json;
//    }

    public static String getAnswerJson(String id) {
        String json = null;
        List<AnswerBean> mAnswerBeanList = DataSupport.select("answerJson").where("aid = ?", id).find(AnswerBean.class);
        if (null != mAnswerBeanList && mAnswerBeanList.size() > 0) {
            AnswerBean ab = mAnswerBeanList.get(0);
            json = ab.getAnswerJson();
        }
        return json;
    }

    public static boolean getIsAnswered(String id) {
        boolean isAnswered = false;
        List<AnswerBean> mAnswerBeanList = DataSupport.select("isAnswerd").where("aid = ?", id).find(AnswerBean.class);
        if (null != mAnswerBeanList && mAnswerBeanList.size() > 0) {
            AnswerBean ab = mAnswerBeanList.get(0);
            isAnswered = ab.isAnswerd();
        }
        return isAnswered;
    }

    public static void deleteAllAnswer(List<BaseQuestion> list) {
        for(BaseQuestion question : list){
            DataSupport.deleteAll(AnswerBean.class, "aid = ?", makeId(question));
        }

    }

    public static boolean isHasShowVideTips(String paperId){
        boolean hasShow = false;
        List<VideoConfigBean> result = DataSupport.where("paperId = ?", paperId).find(VideoConfigBean.class);
        if(result != null && result.size() > 0){
            hasShow = result.get(0).isHasShowVideoTips();
        }
        return hasShow;
    }

    public static void setHasShowVideoTips(String paperId, boolean show){
        VideoConfigBean bean = new VideoConfigBean();
        bean.setPaperId(paperId);
        bean.setHasShowVideoTips(show);
        bean.save();
    }
    public static String makeId(BaseQuestion question) {
        String userId = LoginInfo.getUID();
        String id = null;
        try {
            id = userId + question.getId() + question.getPid() + question.getQid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
