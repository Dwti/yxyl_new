package com.yanxiu.gphone.student.questions.answerframe.util;

import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.choose.MultiChoiceQuestion;
import com.yanxiu.gphone.student.questions.classify.ClassifyQuestion;
import com.yanxiu.gphone.student.questions.cloze.ClozeComplexQuestion;
import com.yanxiu.gphone.student.questions.connect.ConnectQuestion;
import com.yanxiu.gphone.student.questions.fillblank.FillBlankQuestion;
import com.yanxiu.gphone.student.questions.listencomplex.ListenComplexQuestion;
import com.yanxiu.gphone.student.questions.operation.OperationQuestion;
import com.yanxiu.gphone.student.questions.readingcomplex.ReadingComplexQuestion;
import com.yanxiu.gphone.student.questions.choose.SingleChoiceQuestion;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.spoken.SpokenQuestion;
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
                    ConnectQuestion connectQuestion = new ConnectQuestion(paperTestBean,showType,paperStatus);
                    questions.add(connectQuestion);
                    break;
                case QuestionTemplate.CLASSIFY:
                    ClassifyQuestion classifyQuestion = new ClassifyQuestion(paperTestBean,showType,paperStatus);
                    questions.add(classifyQuestion);
                    break;
                case QuestionTemplate.OPERATION:
                    OperationQuestion operationQuestion = new OperationQuestion(paperTestBean,showType,paperStatus);
                    questions.add(operationQuestion);
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
                    for(PaperTestBean child : paperTestBean.getQuestions().getChildren()){
                        child.setWqid(paperTestBean.getWqid());
                    }
                    ClozeComplexQuestion clozeComplexQuestion = new ClozeComplexQuestion(paperTestBean, showType, paperStatus);
                    questions.add(clozeComplexQuestion);
                    break;
                case QuestionTemplate.READING:
                case QuestionTemplate.LISTEN:
                    //复合题需要判断子题数量
                    convertQuestionComplesToSimple(questions, paperTestBean, showType, paperStatus);
                    break;
                case QuestionTemplate.SPOKEN:
                    SpokenQuestion spokenQuestion=new SpokenQuestion(paperTestBean,showType,paperStatus);
                    questions.add(spokenQuestion);
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
        String padId_complex = paperTestBean.getQuestions().getPad().getId();//复合题的pad里的id,上传答案时需要
        String ptid_complex = paperTestBean.getId();//复合题的上传答案时的ptid
        String qid_complex = paperTestBean.getQid();//复合题的上传答案时的qid
        int wqid=paperTestBean.getWqid();
        int wqnumber=paperTestBean.getWqnumber();
        int childCount = ChildQuestion.size();

        for (PaperTestBean testBean:ChildQuestion){
            testBean.setWqid(wqid);
            testBean.setWqnumber(wqnumber);
        }

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
                    singleChoiceQuestion.setPadId_ComplexToSimple(padId_complex);
                    singleChoiceQuestion.setPtid_ComplexToSimple(ptid_complex);
                    singleChoiceQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(singleChoiceQuestion);
                    break;
                case QuestionTemplate.MULTI_CHOICES:
                    MultiChoiceQuestion multiChoiceQuestion = new MultiChoiceQuestion(childQuestion, showType, paperStatus);
                    multiChoiceQuestion.setStem_complexToSimple(stem_complex);
                    multiChoiceQuestion.setTemplate_complexToSimple(template);
                    multiChoiceQuestion.setTypeId_complexToSimple(type_id);
                    multiChoiceQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    multiChoiceQuestion.setPadId_ComplexToSimple(padId_complex);
                    multiChoiceQuestion.setPtid_ComplexToSimple(ptid_complex);
                    multiChoiceQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(multiChoiceQuestion);
                    break;
                case QuestionTemplate.FILL:
                    FillBlankQuestion fillBlankQuestion = new FillBlankQuestion(childQuestion, showType, paperStatus);
                    fillBlankQuestion.setStem_complexToSimple(stem_complex);
                    fillBlankQuestion.setTemplate_complexToSimple(template);
                    fillBlankQuestion.setTypeId_complexToSimple(type_id);
                    fillBlankQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    fillBlankQuestion.setPadId_ComplexToSimple(padId_complex);
                    fillBlankQuestion.setPtid_ComplexToSimple(ptid_complex);
                    fillBlankQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(fillBlankQuestion);
                    break;
                case QuestionTemplate.ALTER:
                    YesNoQuestion yesNoQuestion = new YesNoQuestion(childQuestion, showType, paperStatus);
                    yesNoQuestion.setStem_complexToSimple(stem_complex);
                    yesNoQuestion.setTemplate_complexToSimple(template);
                    yesNoQuestion.setTypeId_complexToSimple(type_id);
                    yesNoQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    yesNoQuestion.setPadId_ComplexToSimple(padId_complex);
                    yesNoQuestion.setPtid_ComplexToSimple(ptid_complex);
                    yesNoQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(yesNoQuestion);
                    break;
                case QuestionTemplate.CONNECT:
                    ConnectQuestion connectQuestion = new ConnectQuestion(childQuestion,showType,paperStatus);
                    connectQuestion.setStem_complexToSimple(stem_complex);
                    connectQuestion.setTemplate_complexToSimple(template);
                    connectQuestion.setTypeId_complexToSimple(type_id);
                    connectQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    connectQuestion.setPadId_ComplexToSimple(padId_complex);
                    connectQuestion.setPtid_ComplexToSimple(ptid_complex);
                    connectQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(connectQuestion);
                    break;
                case QuestionTemplate.CLASSIFY:
                    ClassifyQuestion classifyQuestion = new ClassifyQuestion(childQuestion,showType,paperStatus);
                    classifyQuestion.setStem_complexToSimple(stem_complex);
                    classifyQuestion.setTemplate_complexToSimple(template);
                    classifyQuestion.setTypeId_complexToSimple(type_id);
                    classifyQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    classifyQuestion.setPadId_ComplexToSimple(padId_complex);
                    classifyQuestion.setPtid_ComplexToSimple(ptid_complex);
                    classifyQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(classifyQuestion);
                    break;
                case QuestionTemplate.OPERATION:
                    OperationQuestion operationQuestion = new OperationQuestion(childQuestion,showType,paperStatus);
                    operationQuestion.setStem_complexToSimple(stem_complex);
                    operationQuestion.setTemplate_complexToSimple(template);
                    operationQuestion.setTypeId_complexToSimple(type_id);
                    operationQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    operationQuestion.setPadId_ComplexToSimple(padId_complex);
                    operationQuestion.setPtid_ComplexToSimple(ptid_complex);
                    operationQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(operationQuestion);
                    break;
                case QuestionTemplate.ANSWER:
                    SubjectiveQuestion subjectiveQuestion = new SubjectiveQuestion(childQuestion, showType, paperStatus);
                    subjectiveQuestion.setStem_complexToSimple(stem_complex);
                    subjectiveQuestion.setTemplate_complexToSimple(template);
                    subjectiveQuestion.setTypeId_complexToSimple(type_id);
                    subjectiveQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    subjectiveQuestion.setPadId_ComplexToSimple(padId_complex);
                    subjectiveQuestion.setPtid_ComplexToSimple(ptid_complex);
                    subjectiveQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(subjectiveQuestion);
                    break;
                case QuestionTemplate.SPOKEN:
                    SpokenQuestion spokenQuestion = new SpokenQuestion(childQuestion, showType, paperStatus);
                    spokenQuestion.setStem_complexToSimple(stem_complex);
                    spokenQuestion.setTemplate_complexToSimple(template);
                    spokenQuestion.setTypeId_complexToSimple(type_id);
                    spokenQuestion.setUrl_listenComplexToSimple(url_complex_listen);
                    spokenQuestion.setPadId_ComplexToSimple(padId_complex);
                    spokenQuestion.setPtid_ComplexToSimple(ptid_complex);
                    spokenQuestion.setQid_ComplexToSimple(qid_complex);
                    questions.add(spokenQuestion);
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
