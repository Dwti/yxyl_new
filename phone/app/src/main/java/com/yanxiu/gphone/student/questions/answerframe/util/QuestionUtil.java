package com.yanxiu.gphone.student.questions.answerframe.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.bean.ReportAnswerBean;
import com.yanxiu.gphone.student.questions.classify.ClassifyQuestion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 戴延枫 on 2017/6/30.
 */

public class QuestionUtil {

    public static final int ANSER_RIGHT = 0;     //正确
    public static final int ANSER_WRONG = 1;     //错误
    public static final int ANSER_HALF_RIGHT = 2;  //半对         //只有主观题才有半对状态，服务器不会回传这个值，对于主观题的status服务器回传的只会是 3 4 5 ；
    public static final int ANSER_UNFINISH = 3;  //未作答
    public static final int ANSER_FINISH = 4;    //主观题已作答
    public static final int ANSER_READED = 5;    //主观题已批改
    public static final int ANSER_UNFINISHS = 6;   //未完成

    /**
     * 给答题卡和答题报告设置题号
     *
     * @return
     */
    public static ArrayList<BaseQuestion> allNodesThatHasNumber(ArrayList<BaseQuestion> questions) {
        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
        for (BaseQuestion node : questions) {
            retNodes.addAll(node.allNodesThatHasNumber());
        }

        return retNodes;
    }

    /**
     * 对题目按照题型进行分类
     *
     * @param list
     * @return
     */
    public static Map<String, List<BaseQuestion>> classifyQuestionByType(List<BaseQuestion> list) {
        if (list == null || list.size() == 0)
            return null;
        TreeMap<String, List<BaseQuestion>> treeMap = new TreeMap<>(new QuestionTypeComparator());
        int size = list.size();
        BaseQuestion questionEntity;
        try {
            for (int i = 0; i < size; i++) {
                questionEntity = list.get(i);
//            String typeName = getQuestionTypeNameByParentTypeId(questionEntity.getParent_type_id());
                String typeName = null;
                if (questionEntity.isComplexQuestion()) {
                    typeName = getQuestionTypeNameByParentTypeId(Integer.valueOf(questionEntity.getType_id()));
                    List<BaseQuestion> childrenList = questionEntity.getChildren();
                    for (int j = 0; j < childrenList.size(); j++) {
                        BaseQuestion childQuestion = childrenList.get(j);

                        if (!treeMap.containsKey(typeName)) {
                            List<BaseQuestion> tempList = new ArrayList<>();
                            tempList.add(childQuestion);
                            treeMap.put(typeName, tempList);
                        } else {
                            List<BaseQuestion> valueList = treeMap.get(typeName);
                            valueList.add(childQuestion);
                        }
                    }
                } else {
                    String parentType_id = questionEntity.getParentType_id();
                    if (!TextUtils.isEmpty(parentType_id)) {
                        typeName = getQuestionTypeNameByParentTypeId(Integer.valueOf(parentType_id));
                    } else {
                        typeName = getQuestionTypeNameByParentTypeId(Integer.valueOf(questionEntity.getType_id()));
                    }

                    if (!treeMap.containsKey(typeName)) {
                        List<BaseQuestion> tempList = new ArrayList<>();
                        tempList.add(questionEntity);
                        treeMap.put(typeName, tempList);
                    } else {
                        List<BaseQuestion> valueList = treeMap.get(typeName);
                        valueList.add(questionEntity);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeMap;
    }

    public static String getQuestionTypeNameByParentTypeId(int typeId) {
        String name = "";
        switch (typeId) {
            case 1:
                name = "单选题";
                break;
            case 2:
                name = "多选题";
                break;
            case 3:
                name = "填空题";
                break;
            case 4:
                name = "判断题";
                break;
            case 5:
                name = "材料阅读";
                break;
            case 6:
                name = "问答题";
                break;
            case 7:
                name = "连线题";
                break;
            case 8:
                name = "计算题";
                break;
            case 13:
                name = "归类题";
                break;
            case 14:
                name = "阅读理解";
                break;
            case 15:
                name = "完形填空";
                break;
            case 16:
                name = "翻译题";
                break;
            case 17:
                name = "改错题";
                break;
            case 20:
                name = "排序题";
                break;
            case 22:
                name = "解答题";
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 18:
            case 19:
            case 21:
                name = "听力题";
                break;
            default:
                break;

        }
        return name;
    }

    /**
     * 按照getIntValue（）中的顺序， 排序
     */
    public static class QuestionTypeComparator implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            return getIntValue(lhs) - getIntValue(rhs);
        }
    }

    public enum QUESTION_TYP {
        QUESTION_SINGLE_CHOICES(1, YanxiuApplication.getInstance().getResources().getString(R.string.question_choice_single)),
        QUESTION_MULTI_CHOICES(2, YanxiuApplication.getInstance().getResources().getString(R.string.question_choice_multi)),
        QUESTION_FILL_BLANKS(3, YanxiuApplication.getInstance().getResources().getString(R.string.question_fill_blanks)),
        QUESTION_JUDGE(4, YanxiuApplication.getInstance().getResources().getString(R.string.question_judge)),
        QUESTION_READING(5, YanxiuApplication.getInstance().getResources().getString(R.string.question_reading)),
        QUESTION_SUBJECTIVE(6, YanxiuApplication.getInstance().getResources().getString(R.string.question_subjective)),
        QUESTION_CONNECT(7, YanxiuApplication.getInstance().getResources().getString(R.string.question_connect)),
        QUESTION_COMPUTE(8, YanxiuApplication.getInstance().getResources().getString(R.string.question_compute)),
        QUESTION_LISTEN_COMPLEX(9, YanxiuApplication.getInstance().getResources().getString(R.string.question_listen_complex)),
        QUESTION_CLASSFY(13, YanxiuApplication.getInstance().getResources().getString(R.string.question_classfy)),
        QUESTION_READ_COMPLEX(14, YanxiuApplication.getInstance().getResources().getString(R.string.question_read_complex)),
        QUESTION_CLOZE_COMPLEX(15, YanxiuApplication.getInstance().getResources().getString(R.string.question_cloze_complex)),
        QUESTION_TRANSLATION(16, YanxiuApplication.getInstance().getResources().getString(R.string.question_translation)),
        QUESTION_SUBJECTSWERE(17, YanxiuApplication.getInstance().getResources().getString(R.string.question_subjects_were)),
        QUESTION_SORTING(20, YanxiuApplication.getInstance().getResources().getString(R.string.question_sorting)),
        QUESTION_SOLVE_COMPLEX(22, YanxiuApplication.getInstance().getResources().getString(R.string.question_solve_complex));

        public int type;
        public String name;

        QUESTION_TYP(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    public static int getIntValue(String str) {
        int result = 0;
        switch (str) {
            case "听力题":
                result = 1;
                break;
            case "单选题":
                result = 2;
                break;
            case "多选题":
                result = 3;
                break;
            case "判断题":
                result = 4;
                break;
            case "连线题":
                result = 5;
                break;
            case "归类题":
                result = 6;
                break;
            case "排序题":
                result = 7;
                break;
            case "完形填空":
                result = 8;
                break;
            case "阅读理解":
                result = 9;
                break;
            case "填空题":
                result = 10;
                break;
            case "改错题":
                result = 11;
                break;
            case "翻译题":
                result = 12;
                break;
            case "计算题":
                result = 13;
                break;
            case "解答题":
                result = 14;
                break;
            case "问答题":
                result = 15;
                break;
            case "材料阅读":
                result = 16;
                break;

        }
        return result;
    }

    /**
     * 答题报告数据转换
     * dyf
     *
     * @param paper
     */
    public static void initDataWithAnswer(Paper paper) {
        if (paper != null && paper.getQuestions() != null && !paper.getQuestions().isEmpty()) {
            List<BaseQuestion> questionsList = paper.getQuestions();

            if (questionsList == null) {
                return;
            }

            int count = questionsList.size();
            for (int i = 0; i < count; i++) {
                if (questionsList.get(i) != null) {

                    BaseQuestion outQuestion = questionsList.get(i);
                    String template = outQuestion.getTemplate();
                    if (outQuestion.getPad() == null)
                        continue;
                    String jsonAnswer = new Gson().toJson(outQuestion.getPad().getJsonAnswer());

                    //如果是复合类的题
                    if (outQuestion.getTemplate().equals(QuestionTemplate.READING)
                            || outQuestion.getTemplate().equals(QuestionTemplate.CLOZE)
                            || outQuestion.getTemplate().equals(QuestionTemplate.LISTEN)) {

                        List<BaseQuestion> childrenQuestionList = outQuestion.getChildren();
                        if (childrenQuestionList != null && !childrenQuestionList.isEmpty()) {
                            int childrenCount = childrenQuestionList.size();
                            for (int j = 0; j < childrenCount; j++) {
                                BaseQuestion childQuestion = childrenQuestionList.get(j);
                                if (childQuestion == null || childQuestion.getPad() == null)
                                    continue;
                                childQuestion.getReportAnswerBean().setConsumeTime(Integer.parseInt(childQuestion.getPad().getCosttime()));
                                List<String> answerChildList = null;
                                String childJsonAnswer = new Gson().toJson(childQuestion.getPad().getJsonAnswer());
//                                answerChildList = JSON.parseArray(childJsonAnswer,String.class);
                                try {
                                    JSONArray array;
                                    array = new JSONArray(childJsonAnswer);
                                    answerChildList = new ArrayList<>();
                                    for (int x = 0; x < array.length(); x++) {
                                        answerChildList.add(array.getString(x));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                List<Object> rightAnswer = childQuestion.getServer_answer();
                                ReportAnswerBean reportAnswerBean = childQuestion.getReportAnswerBean();
                                int status = childQuestion.getPad().getStatus();
                                String childTemplate = childQuestion.getTemplate();
                                reportAnswerBean.setStatus(status);
                                //此处分为需要老师批改跟不需要老师批改两种情况处理（即主观题与非主观题）
                                if (!QuestionTemplate.ANSWER.equals(childTemplate)) {
                                    //非主观题（就是有正确答案，直接能在客户端判定的题）
                                    if (answerChildList != null && !answerChildList.isEmpty()) {

                                        reportAnswerBean.setIsFinish(true);
                                        //如果是填空题，需要再次确认一下是不是所有的空全填完了（没填完的空是""）
                                        if (QuestionTemplate.FILL.equals(childTemplate)) {
                                            for (String str : answerChildList) {
                                                if (TextUtils.isEmpty(str)) {
                                                    reportAnswerBean.setIsFinish(false);
                                                    break;
                                                }
                                            }
                                        }

                                        if (QuestionTemplate.SINGLE_CHOICE.equals(childTemplate) || QuestionTemplate.ALTER.equals(childTemplate)) {
                                            reportAnswerBean.setSelectType(answerChildList.get(0));
                                            if (rightAnswer != null && !rightAnswer.isEmpty()) {
                                                if (!TextUtils.isEmpty(answerChildList.get(0)) && answerChildList.get(0).equals(String.valueOf(rightAnswer.get(0)))) {
                                                    reportAnswerBean.setIsRight(true);
                                                } else {
                                                    reportAnswerBean.setIsRight(false);
                                                }
                                            }
                                        } else if (QuestionTemplate.CONNECT.equals(childTemplate)) {
//                                            try {
//                                                JSONArray array=new JSONArray(childJsonAnswer);
//                                                for (int k=0;k<array.length();k++){
//                                                    String arrayJSONArray[]= array.getString(k).split(",");
//                                                    ArrayList<String> list=new ArrayList<String>();
//                                                    for (int l=0;l<arrayJSONArray.length;l++){
//                                                        if (!TextUtils.isEmpty(arrayJSONArray[l])) {
//                                                            list.add(arrayJSONArray[l]);
//                                                        }
//                                                    }
//                                                    reportAnswerBean.getConnect_classfy_answer().add(list);
//                                                }
//
//
//
//                                                if (reportAnswerBean.getConnect_classfy_answer().size()>0 ){
//                                                    int num = 0;
//                                                    for (ArrayList<String> listStr:reportAnswerBean.getConnect_classfy_answer()) {
//                                                        num = num + listStr.size();
//                                                    }
//                                                    if (QuestionTemplate.CLASSIFY.equals(childTemplate)) {
////                                                    if (QuestionTemplate.CLASSIFY.equals(childTemplate) && num < childQuestion.getContent().getChoices().size()) {
//                                                        reportAnswerBean.setIsFinish(false);
//                                                        reportAnswerBean.setIsRight(false);
//                                                    }else if (QuestionTemplate.CONNECT.equals(childTemplate) &&reportAnswerBean.getConnect_classfy_answer().size()<childQuestion.getServer_answer().size()){
//                                                        reportAnswerBean.setIsFinish(false);
//                                                        reportAnswerBean.setIsRight(false);
//                                                    }else {
//                                                        reportAnswerBean.setIsFinish(true);
//                                                        List<String> list = childQuestion.getServer_answer();
//                                                        reportAnswerBean.setIsRight(CheckConnect_classfy_answer(list, reportAnswerBean.getConnect_classfy_answer(), childTemplate));
//                                                    }
//                                                }else {
//                                                    reportAnswerBean.setIsFinish(false);
//                                                    reportAnswerBean.setIsRight(false);
//                                                }
//                                            }catch (Exception e){
//
//                                            }
                                        } else {
                                            List<String> rightAnswerStr = new ArrayList<>();
                                            for (Object o : rightAnswer) {
                                                rightAnswerStr.add(String.valueOf(o));
                                            }
                                            if (QuestionTemplate.MULTI_CHOICES.equals(childTemplate)) {
                                                reportAnswerBean.setMultiSelect((ArrayList<String>) answerChildList);
                                            } else if (QuestionTemplate.FILL.equals(childTemplate)) {
                                                reportAnswerBean.setFillAnswers((ArrayList<String>) answerChildList);
                                            }
                                            if (compareListByOrder(answerChildList, rightAnswerStr)) {
                                                reportAnswerBean.setIsRight(true);
                                            } else {
                                                reportAnswerBean.setIsRight(false);
                                            }
                                        }

                                    } else {
                                        reportAnswerBean.setIsFinish(false);
                                    }
                                } else {
                                    //如果是主观题
                                    if (childrenQuestionList.get(j).getPad().getTeachercheck() != null) {
                                        int score = childrenQuestionList.get(j).getPad().getTeachercheck().getScore();  //老师打的分数，数值范围0-5，0:错；0~4：半对； 5：对
                                        if (QuestionUtil.ANSER_READED == status) {
                                            if (score == 0) {
                                                reportAnswerBean.setIsRight(false);
                                            } else if (score == 5) {
                                                reportAnswerBean.setIsRight(true);
                                            } else if (score > 0 && score < 5) {
                                                reportAnswerBean.setIsHalfRight(true);
                                            }
                                            reportAnswerBean.setIsFinish(true);
                                        }
                                    }
                                    if (answerChildList != null && !answerChildList.isEmpty()) {
                                        reportAnswerBean.setIsFinish(true);
                                        reportAnswerBean.setSubjectivImageUri((ArrayList<String>) answerChildList);
                                    }
                                    reportAnswerBean.setIsSubjective(true);
                                }
                            }
                        }
                    } else {
                        int status = outQuestion.getPad().getStatus();
                        int costTime = Integer.valueOf(outQuestion.getPad().getCosttime());
                        ReportAnswerBean answerBean = outQuestion.getReportAnswerBean();
                        answerBean.setConsumeTime(costTime);
                        answerBean.setStatus(status);
                        List<String> answerList = null;
                        try {
                            JSONArray array;
                            array = new JSONArray(jsonAnswer);
                            answerList = new ArrayList<>();
                            for (int x = 0; x < array.length(); x++) {
                                answerList.add(array.getString(x));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //此处分为需要老师批改跟不需要老师批改两种情况处理（即主观题与非主观题）
                        if (!QuestionTemplate.ANSWER.equals(template)) {
                            //非主观题
                            if (!QuestionTemplate.CONNECT.equals(template) && !QuestionTemplate.CLASSIFY.equals(template)) {
                                if (answerList != null && !answerList.isEmpty()) {
                                    if (status == QuestionUtil.ANSER_RIGHT)
                                        answerBean.setIsRight(true);
                                    answerBean.setIsFinish(true);
                                    if (QuestionTemplate.SINGLE_CHOICE.equals(template)) {
                                        answerBean.setSelectType(answerList.get(0));
                                    } else if (QuestionTemplate.MULTI_CHOICES.equals(template)) {
                                        answerBean.setMultiSelect((ArrayList<String>) answerList);
                                    } else if (QuestionTemplate.ALTER.equals(template)) {
                                        answerBean.setSelectType(answerList.get(0));
                                    } else if (QuestionTemplate.FILL.equals(template)) {
                                        answerBean.setFillAnswers((ArrayList<String>) answerList);
                                    }
                                    //如果是填空题，需要再次确认一下是不是所有的空全填完了（没填完的空是""）
                                    if (QuestionTemplate.FILL.equals(template)) {
                                        for (String str : answerList) {
                                            if (TextUtils.isEmpty(str)) {
                                                answerBean.setIsFinish(false);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    answerBean.setIsFinish(false);
                                }
                            } else {
                                try {
                                    JSONArray array = new JSONArray(jsonAnswer);
                                    for (int k = 0; k < array.length(); k++) {
                                        String arrayJSONArray[] = array.getString(k).split(",");
                                        ArrayList<String> list = new ArrayList<String>();
                                        for (int l = 0; l < arrayJSONArray.length; l++) {
                                            if (!TextUtils.isEmpty(arrayJSONArray[l])) {
                                                list.add(arrayJSONArray[l]);
                                            }
                                        }
                                        answerBean.getConnect_classfy_answer().add(list);
                                    }


//                                    if (answerBean.getConnect_classfy_answer().size() > 0) {
//                                        int num = 0;
//                                        for (ArrayList<String> listStr : answerBean.getConnect_classfy_answer()) {
//                                            num = num + listStr.size();
//                                        }
//                                        if (QuestionTemplate.CLASSIFY.equals(template) && num < ((ClassifyQuestion)outQuestion).getChoice().size()) {
//                                            answerBean.setIsFinish(false);
//                                            answerBean.setIsRight(false);
//                                        }
////                                        else if (QuestionTemplate.CONNECT.equals(template) && answerBean.getConnect_classfy_answer().size() < outQuestion.getServer_answer().size()) {
////                                            answerBean.setIsFinish(false);
////                                            answerBean.setIsRight(false);
////                                        }
////                                        else {
////                                            answerBean.setIsFinish(true);
////                                            List<String> list = outQuestion.getServer_answer();
////                                            answerBean.setIsRight(CheckConnect_classfy_answer(list, answerBean.getConnect_classfy_answer(), template));
////                                        }
//                                    } else {
//                                        answerBean.setIsFinish(false);
//                                        answerBean.setIsRight(false);
//                                    }
                                    if (Constants.ANSWER_STATUS_RIGHT == outQuestion.getPad().getStatus()) {
                                        answerBean.setIsFinish(true);
                                        answerBean.setIsRight(true);

                                    } else if (Constants.ANSWER_STATUS_WRONG == outQuestion.getPad().getStatus()) {
                                        answerBean.setIsFinish(true);
                                        answerBean.setIsRight(false);
                                    } else {
                                        answerBean.setIsFinish(false);
                                        answerBean.setIsRight(false);
                                    }

                                } catch (Exception e) {

                                }
                            }

                        } else {
                            //主观题
                            if (outQuestion.getPad().getTeachercheck() != null) {
                                int score = outQuestion.getPad().getTeachercheck().getScore();
                                switch (status) {
                                    //已批改状态，未批改的话不做处理
                                    case QuestionUtil.ANSER_READED:
                                        if (score == 0) {
                                            answerBean.setIsRight(false);
                                        } else if (score == 5) {
                                            answerBean.setIsRight(true);
                                        } else if (score > 0 && score < 5) {
                                            answerBean.setIsHalfRight(true);
                                        }
                                        answerBean.setIsFinish(true);
                                        break;
                                }
                            }
                            if (answerList != null && !answerList.isEmpty()) {
                                answerBean.setIsFinish(true);
                                answerBean.setSubjectivImageUri((ArrayList<String>) answerList);
                            }
                            answerBean.setIsSubjective(true);

                        }
                    }
                }
            }
        }
    }

    public static <T extends Comparable<T>> boolean compareListByOrder(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;

        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).compareTo(b.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算正确率,计算规则：主观题只有被批改之后，才会计入在内
     *
     * @param list 拆分处理之后的单题集合
     * @return
     */
    public static float calculateRightRate(List<BaseQuestion> list) {
        if (list == null || list.size() == 0)
            return 0;
        float totalCount = 0;
        float rightCount = 0;
        float result = 0;
        for (BaseQuestion entity : list) {
            if (entity == null)
                continue;
            if (QuestionTemplate.ANSWER.equals(entity.getTemplate())) {
                if (ReportAnswerBean.ANSER_READED == entity.getReportAnswerBean().getRealStatus()) {
                    if (entity.getReportAnswerBean().isRight()) {
                        rightCount += 1;
                    } else if (entity.getReportAnswerBean().isHalfRight()) {
                        //半对状态算50%正确率
                        rightCount += 0.5;
                    }
                    totalCount += 1;
                }
            } else {
                if (entity.getReportAnswerBean().isRight()) {
                    rightCount += 1;
                }
                totalCount += 1;
            }
        }
//        return Float.parseFloat(String.format("%.2f", rightCount / totalCount));
        return rightCount / totalCount;
    }

    /**
     * 计算做对的题目数
     *
     * @param list
     * @return
     */
    public static int calculateRightCount(List<BaseQuestion> list) {
        int totalCount = 0;
        HashMap<String, Integer> map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            BaseQuestion question = list.get(i);
            String prefixNumber = String.valueOf(question.getAnswerCardPrefixNumber());
            if ("-1".equals(prefixNumber)) { //答题报告逻辑里的单题（除了8，22类型的题）
                if (question.getReportAnswerBean().isRight())
                    totalCount++;
            } else {
                if (!map.containsKey(prefixNumber)) {
                    if (question.getReportAnswerBean().isRight()) {
                        map.put(prefixNumber, 1);
                    } else {
                        map.put(prefixNumber, -10);
                    }
                } else { //map里已经存在了，那么判断如果key的value为-10,那么说明该大题有错题
                    if (-10 == map.get(prefixNumber)) { //已经有错题了，那么这个就是错误的，无需再判断

                    } else {
                        if (question.getReportAnswerBean().isRight()) {
                            map.put(prefixNumber, 1);
                        } else {
                            map.put(prefixNumber, -10);
                        }
                    }
                }
            }
        }
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int val = (int) entry.getValue();
            if (val == 1)
                totalCount++;
        }
        return totalCount;
    }

    /**
     * 答题报告里，获取总共多少题
     *
     * @param list
     * @return
     */
    public static int getTotalCount(List<BaseQuestion> list) {
        int totalCount = 0;
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            BaseQuestion question = list.get(i);
            String prefixNumber = String.valueOf(question.getAnswerCardPrefixNumber());
            if ("-1".equals(prefixNumber)) { //答题报告逻辑里的单题（除了8，22类型的题）
                totalCount++;
            } else {
                if (!tempList.contains(prefixNumber)) {
                    tempList.add(prefixNumber);
                    totalCount++;
                }
            }
        }
        return totalCount;
    }

    /**
     * 计算已经完成的题目数
     * 答题没有提交答案的情况下，保存本地数据库。作业列表需要
     *
     * @param questionList
     * @return
     */
    public static int calculateCompleteCount(ArrayList<BaseQuestion> questionList) {
        ArrayList<BaseQuestion> list = (ArrayList<BaseQuestion>)questionList.clone();
        list = allNodesThatHasNumber(list);
        int totalCount = 0;
        HashMap<String, Integer> map = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            BaseQuestion question = list.get(i);
            String prefixNumber = String.valueOf(question.getAnswerCardPrefixNumber());
            if ("-1".equals(prefixNumber)) { //答题报告逻辑里的单题（除了8，22类型的题）
                if (question.getIsAnswer())
                    totalCount++;
            } else {
                if (!map.containsKey(prefixNumber)) {
                    if (question.getIsAnswer()) {
                        map.put(prefixNumber, 1);
                    } else {
                        map.put(prefixNumber, -10);
                    }
                } else { //map里已经存在了，那么判断如果key的value为-10,那么说明该大题有未答题
                    if (-10 == map.get(prefixNumber)) { //已经有未答题了，那么这个就是未答的，无需再判断

                    } else {
                        if (question.getIsAnswer()) {
                            map.put(prefixNumber, 1);
                        } else {
                            map.put(prefixNumber, -10);
                        }
                    }
                }
            }
        }
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int val = (int) entry.getValue();
            if (val == 1)
                totalCount++;
        }
        return totalCount;
    }
}
