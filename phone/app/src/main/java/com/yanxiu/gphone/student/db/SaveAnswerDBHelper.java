package com.yanxiu.gphone.student.db;


import com.yanxiu.gphone.student.questions.answerframe.bean.AnswerBean;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;

import org.litepal.crud.DataSupport;

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
        List<AnswerBean> mAnswerBeanList = DataSupport.where("aid = ?", id).find(AnswerBean.class);
        if (null != mAnswerBeanList && mAnswerBeanList.size() > 0) {
            AnswerBean ab = mAnswerBeanList.get(0);
            json = ab.getAnswerJson();
        }
        return json;
    }

    public static void deleteAllAnswer() {
        DataSupport.deleteAll(AnswerBean.class);
    }

    public static String makeId(BaseQuestion question) {
        String id = null;
        try {
            id = question.getId() + question.getPid() + question.getQid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
