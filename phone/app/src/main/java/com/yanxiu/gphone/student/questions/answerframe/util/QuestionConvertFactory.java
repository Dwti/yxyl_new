package com.yanxiu.gphone.student.questions.answerframe.util;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.choose.MultiChoiceQuestion;
import com.yanxiu.gphone.student.questions.listencomplex.ListenComplexQuestion;
import com.yanxiu.gphone.student.questions.readingcomplex.ReadingComplexQuestion;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.yesno.YesNoQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class QuestionConvertFactory {

    /**
    public static Paper convertSourcePaperData(PaperBean bean, QuestionShowType showType){
        if (bean == null)
            return null;
        Paper paper = new Paper();
        paper.setAuthorid(bean.getAuthorid());
        paper.setBedition(bean.getBedition());
        paper.setBegintime(bean.getBegintime());
        paper.setBuildtime(bean.getBuildtime());
        paper.setChapterid(bean.getChapterid());
        paper.setClassid(bean.getClassid());
        paper.setEditionName(bean.getEditionName());
        paper.setEndtime(bean.getEndtime());
        paper.setId(bean.getId());
        paper.setName(bean.getName());
        paper.setPaperStatus(bean.getPaperStatus());
        paper.setParentId(bean.getParentId());
        paper.setPtype(bean.getPtype());
        paper.setQuesnum(bean.getQuesnum());
        paper.setRedoDays(bean.getRedoDays());
        paper.setSectionid(bean.getSectionid());
        paper.setShowana(bean.getShowana());
        paper.setStageName(bean.getStageName());
        paper.setStageid(bean.getStageid());
        paper.setStatus(bean.getStatus());
        paper.setSubjectName(bean.getSubjectName());
        paper.setSubjectid(bean.getSubjectid());
        paper.setSubquesnum(bean.getSubquesnum());
        paper.setVolume(bean.getVolume());
        paper.setVolumeName(bean.getVolumeName());

        paper.setQuestions(convertQuestion(bean.getPaperTest(),showType));

        return paper;
    }**/

    public static ArrayList<BaseQuestion> convertQuestion(List<PaperTestBean> list, QuestionShowType showType){
        if(list == null || list.size()==0)
            return null;
        ArrayList<BaseQuestion> questions = new ArrayList<>();
        for(PaperTestBean paperTestBean: list){
            switch (paperTestBean.getQuestions().getTemplate()){
                case QuestionTemplate.SINGLE_CHOICE:
                    SingleChoiceQuestion singleChoiceQuestion = new SingleChoiceQuestion(paperTestBean,showType);
                    questions.add(singleChoiceQuestion);
                    break;
                case QuestionTemplate.MULTI_CHOICES:
                    MultiChoiceQuestion multiChoiceQuestion=new MultiChoiceQuestion(paperTestBean,showType);
                    questions.add(multiChoiceQuestion);
                    break;
                case QuestionTemplate.FILL:
                    break;
                case QuestionTemplate.ALTER:
                    YesNoQuestion yesNoQuestion = new YesNoQuestion(paperTestBean,showType);
                    questions.add(yesNoQuestion);
                    break;
                case QuestionTemplate.CONNECT:
                    break;
                case QuestionTemplate.CLASSIFY:
                    break;
                case QuestionTemplate.ANSWER:
                    break;
                case QuestionTemplate.READING:
                    ReadingComplexQuestion readingComplexQuestion = new ReadingComplexQuestion(paperTestBean,showType);
                    questions.add(readingComplexQuestion);
                    break;
                case QuestionTemplate.CLOZE:
                    break;
                case QuestionTemplate.LISTEN:
                    ListenComplexQuestion listenerComplexQuestion=new ListenComplexQuestion(paperTestBean,showType);
                    questions.add(listenerComplexQuestion);
                    break;
                default:
                    break;
            }
        }
        return questions;
    }
}
