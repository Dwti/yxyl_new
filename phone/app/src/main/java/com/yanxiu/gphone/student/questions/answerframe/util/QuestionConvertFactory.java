package com.yanxiu.gphone.student.questions.answerframe.util;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.choose.MultiChoiceQuestion;
import com.yanxiu.gphone.student.questions.cloze.ClozeComplexQuestion;
import com.yanxiu.gphone.student.questions.fillblank.FillBlankQuestion;
import com.yanxiu.gphone.student.questions.listencomplex.ListenComplexQuestion;
import com.yanxiu.gphone.student.questions.readingcomplex.ReadingComplexQuestion;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveFragment;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.questions.yesno.YesNoQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public class QuestionConvertFactory {

    /**
     * public static Paper convertSourcePaperData(PaperBean bean, QuestionShowType showType){
     * if (bean == null)
     * return null;
     * Paper paper = new Paper();
     * paper.setAuthorid(bean.getAuthorid());
     * paper.setBedition(bean.getBedition());
     * paper.setBegintime(bean.getBegintime());
     * paper.setBuildtime(bean.getBuildtime());
     * paper.setChapterid(bean.getChapterid());
     * paper.setClassid(bean.getClassid());
     * paper.setEditionName(bean.getEditionName());
     * paper.setEndtime(bean.getEndtime());
     * paper.setId(bean.getId());
     * paper.setName(bean.getName());
     * paper.setPaperStatus(bean.getPaperStatus());
     * paper.setParentId(bean.getParentId());
     * paper.setPtype(bean.getPtype());
     * paper.setQuesnum(bean.getQuesnum());
     * paper.setRedoDays(bean.getRedoDays());
     * paper.setSectionid(bean.getSectionid());
     * paper.setShowana(bean.getShowana());
     * paper.setStageName(bean.getStageName());
     * paper.setStageid(bean.getStageid());
     * paper.setStatus(bean.getStatus());
     * paper.setSubjectName(bean.getSubjectName());
     * paper.setSubjectid(bean.getSubjectid());
     * paper.setSubquesnum(bean.getSubquesnum());
     * paper.setVolume(bean.getVolume());
     * paper.setVolumeName(bean.getVolumeName());
     * <p>
     * paper.setQuestions(convertQuestion(bean.getPaperTest(),showType));
     * <p>
     * return paper;
     * }
     **/

    public static ArrayList<BaseQuestion> convertQuestion(List<PaperTestBean> list, QuestionShowType showType, String paperStatus) {
        if (list == null || list.size() == 0)
            return null;
        ArrayList<BaseQuestion> questions = new ArrayList<>();
        for (PaperTestBean paperTestBean : list) {
            switch (paperTestBean.getQuestions().getTemplate()) {
                case QuestionTemplate.SINGLE_CHOICE:
                    SingleChoiceQuestion singleChoiceQuestion = new SingleChoiceQuestion(paperTestBean, showType, paperStatus);
                    questions.add(singleChoiceQuestion);
                    break;
                case QuestionTemplate.MULTI_CHOICES:
                    MultiChoiceQuestion multiChoiceQuestion = new MultiChoiceQuestion(paperTestBean, showType, paperStatus);
                    questions.add(multiChoiceQuestion);
                    break;
                case QuestionTemplate.FILL:
                    FillBlankQuestion fillBlankQuestion = new FillBlankQuestion(paperTestBean, showType, paperStatus);
                    questions.add(fillBlankQuestion);
                    break;
                case QuestionTemplate.ALTER:
                    YesNoQuestion yesNoQuestion = new YesNoQuestion(paperTestBean, showType, paperStatus);
                    questions.add(yesNoQuestion);
                    break;
                case QuestionTemplate.CONNECT:
                    break;
                case QuestionTemplate.CLASSIFY:
                    break;
                case QuestionTemplate.ANSWER:
                    SubjectiveQuestion subjectiveQuestion = new SubjectiveQuestion(paperTestBean, showType, paperStatus);
                    questions.add(subjectiveQuestion);
                    break;
//                case QuestionTemplate.READING:
//                    ReadingComplexQuestion readingComplexQuestion = new ReadingComplexQuestion(paperTestBean, showType);
//                    questions.add(readingComplexQuestion);
//                    break;
                case QuestionTemplate.CLOZE:
                    ClozeComplexQuestion clozeComplexQuestion = new ClozeComplexQuestion(paperTestBean, showType, paperStatus);
                    questions.add(clozeComplexQuestion);
                    break;
                case QuestionTemplate.READING:
                case QuestionTemplate.LISTEN:
                    //复合题需要判断子题数量
                    convertQuestionComplesToSimple(questions, paperTestBean, showType, paperStatus);
                    break;
                default:
                    break;
            }
        }
        return questions;
    }

    /**
     * 只有一个子题的复合题，需要转成单题显示
     *
     * @param questions
     * @param paperTestBean
     * @param showType
     * @return
     */
    public static void convertQuestionComplesToSimple(ArrayList<BaseQuestion> questions, PaperTestBean paperTestBean,
                                                      QuestionShowType showType, String paperStatus) {
        if (questions == null)
            return;

        List<PaperTestBean> ChildQuestion = paperTestBean.getQuestions().getChildren();

        if (null == ChildQuestion || ChildQuestion.size() < 1)
            return;

        String template = paperTestBean.getQuestions().getTemplate();//复合题的template
        String type_id = paperTestBean.getQuestions().getType_id();//复合题的type_id
        String stem_complex = paperTestBean.getQuestions().getStem();//复合题的题干
        String url_complex_listen = paperTestBean.getQuestions().getUrl();//听力复合题的url
        int childCount = ChildQuestion.size();

        if (childCount == 1) { //只有一个子题，转成单题
            PaperTestBean childQuestion = ChildQuestion.get(0);//唯一子题
            String childTemplate = childQuestion.getQuestions().getTemplate();//唯一子题的template
            switch (childTemplate) {
                case QuestionTemplate.SINGLE_CHOICE:
                    SingleChoiceQuestion singleChoiceQuestion = new SingleChoiceQuestion(childQuestion, showType, paperStatus);
                    singleChoiceQuestion.setStem_complexToSimple(stem_complex);
                    singleChoiceQuestion.setTemplate_complexToSimple(template);
                    singleChoiceQuestion.setTypeId_complexToSimple(type_id);
                    singleChoiceQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    questions.add(singleChoiceQuestion);
                    break;
                case QuestionTemplate.MULTI_CHOICES:
                    MultiChoiceQuestion multiChoiceQuestion = new MultiChoiceQuestion(childQuestion, showType, paperStatus);
                    multiChoiceQuestion.setStem_complexToSimple(stem_complex);
                    multiChoiceQuestion.setTemplate_complexToSimple(template);
                    multiChoiceQuestion.setTypeId_complexToSimple(type_id);
                    multiChoiceQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    questions.add(multiChoiceQuestion);
                    break;
                case QuestionTemplate.FILL:
                    FillBlankQuestion fillBlankQuestion = new FillBlankQuestion(childQuestion, showType, paperStatus);
                    fillBlankQuestion.setStem_complexToSimple(stem_complex);
                    fillBlankQuestion.setTemplate_complexToSimple(template);
                    fillBlankQuestion.setTypeId_complexToSimple(type_id);
                    fillBlankQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    questions.add(fillBlankQuestion);
                    break;
                case QuestionTemplate.ALTER:
                    YesNoQuestion yesNoQuestion = new YesNoQuestion(childQuestion, showType, paperStatus);
                    yesNoQuestion.setStem_complexToSimple(stem_complex);
                    yesNoQuestion.setTemplate_complexToSimple(template);
                    yesNoQuestion.setTypeId_complexToSimple(type_id);
                    yesNoQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    questions.add(yesNoQuestion);
                    break;
                case QuestionTemplate.CONNECT:
                    break;
                case QuestionTemplate.CLASSIFY:
                    break;
                case QuestionTemplate.ANSWER:
                    SubjectiveQuestion subjectiveQuestion = new SubjectiveQuestion(childQuestion, showType, paperStatus);
                    subjectiveQuestion.setStem_complexToSimple(stem_complex);
                    subjectiveQuestion.setTemplate_complexToSimple(template);
                    subjectiveQuestion.setTypeId_complexToSimple(type_id);
                    subjectiveQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    questions.add(subjectiveQuestion);
                    break;
                default:
                    break;
            }
        } else { //多个子题，还是复合题不变
            switch (template) {
                case QuestionTemplate.READING:
                    ReadingComplexQuestion readingComplexQuestion = new ReadingComplexQuestion(paperTestBean, showType, paperStatus);
                    questions.add(readingComplexQuestion);
                    break;
                case QuestionTemplate.LISTEN:
                    ListenComplexQuestion listenerComplexQuestion = new ListenComplexQuestion(paperTestBean, showType, paperStatus);
                    questions.add(listenerComplexQuestion);
                    break;
                default:
                    break;
            }
        }

    }
}
