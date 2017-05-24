package com.yanxiu.gphone.student.homework.questions;

import com.yanxiu.gphone.student.homework.questions.model.BaseQuestion;
import com.yanxiu.gphone.student.homework.questions.model.Paper;
import com.yanxiu.gphone.student.homework.questions.model.ReadingComplexQuestion;
import com.yanxiu.gphone.student.homework.questions.model.SingleChoiceQuestion;
import com.yanxiu.gphone.student.homework.questions.bean.PaperBean;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;

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

    public static List<BaseQuestion> convertQuestion(List<PaperTestBean> list, QuestionShowType showType){
        if(list == null || list.size()==0)
            return null;
        List<BaseQuestion> questions = new ArrayList<>();
        for(PaperTestBean paperTestBean: list){
            switch (paperTestBean.getQuestions().getTemplate()){
                case QuestionTemplate.SINGLE_CHOICE:
                    SingleChoiceQuestion singleChoiceQuestion = new SingleChoiceQuestion(paperTestBean,showType);
                    questions.add(singleChoiceQuestion);
                    break;
                case QuestionTemplate.MULTI_CHOICES:
                    //TODO 下面类似的写法
                    break;
                case QuestionTemplate.FILL_BLANK:
                    break;
                case QuestionTemplate.JUDGE:
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
                    break;
                default:
                    break;
            }
        }
        return questions;
    }
}
