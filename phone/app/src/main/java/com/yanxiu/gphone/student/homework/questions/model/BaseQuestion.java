package com.yanxiu.gphone.student.homework.questions.model;

import com.yanxiu.gphone.student.homework.questions.QuestionConvertFactory;
import com.yanxiu.gphone.student.homework.questions.QuestionShowType;
import com.yanxiu.gphone.student.homework.questions.QuestionTemplate;
import com.yanxiu.gphone.student.homework.questions.bean.PadBean;
import com.yanxiu.gphone.student.homework.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.homework.questions.bean.PointBean;
import com.yanxiu.gphone.student.homework.questions.fragment.ExerciseFragmentBase;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/5/11.
 */

public abstract class BaseQuestion implements Serializable {
    protected String id;
    protected String correctRate;
    protected String difficulty;
    protected String isfavorite;
    protected String knowledgepoint;
    protected String parentid;
    protected String pid;
    protected String qid;
    protected String qtype;
    protected String analysis;
    //下面这些字段需要需要具体的题型去设置，并不是每个题型都有的（到时候字段的设置需要再检查一遍）
//    protected List<Object> answer;
//    protected ContentBean content;
//    protected String memo;
    protected PadBean pad;
    protected List<PointBean> point;
    protected String stem;
    protected String submit_way;
    protected String template;
    protected String type_id;
    protected String sectionid;
    protected String typeid;
    protected ArrayList<BaseQuestion> children;
    protected QuestionShowType showType;

    public BaseQuestion(PaperTestBean bean,QuestionShowType showType){
        this.id = bean.getId();
        this.correctRate = bean.getCorrectRate();
        this.difficulty = bean.getDifficulty();
        this.isfavorite = bean.getIsfavorite();
        this.knowledgepoint = bean.getKnowledgepoint();
        this.parentid = bean.getParentid();
        this.pid = bean.getPid();
        this.qid = bean.getQid();
        this.qtype = bean.getQtype();
        this.analysis = bean.getQuestions().getAnalysis();
        this.pad = bean.getQuestions().getPad();
        this.point = bean.getQuestions().getPoint();
        this.stem = bean.getQuestions().getStem();
        this.submit_way = bean.getQuestions().getSubmit_way();
        this.template = bean.getQuestions().getTemplate();
        this.type_id = bean.getQuestions().getType_id();
        this.sectionid = bean.getSectionid();
        this.typeid = bean.getTypeid();
        children = QuestionConvertFactory.convertQuestion(bean.getQuestions().getChildren(),showType);
        if(children == null)
            children = new ArrayList<>();
        this.showType = showType;
    }

    public ExerciseFragmentBase getFragment() {
        ExerciseFragmentBase fm = null;
        if (showType.equals(QuestionShowType.ANSWER)) {
            fm = answerFragment();
        }

        if (showType.equals(QuestionShowType.ANALYSIS)) {
            fm = analysisFragment();
        }

        fm.setData(this);
        return fm;
    }

    abstract ExerciseFragmentBase answerFragment();

    abstract ExerciseFragmentBase analysisFragment();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(String correctRate) {
        this.correctRate = correctRate;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(String isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getKnowledgepoint() {
        return knowledgepoint;
    }

    public void setKnowledgepoint(String knowledgepoint) {
        this.knowledgepoint = knowledgepoint;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public PadBean getPad() {
        return pad;
    }

    public void setPad(PadBean pad) {
        this.pad = pad;
    }

    public List<PointBean> getPoint() {
        return point;
    }

    public void setPoint(List<PointBean> point) {
        this.point = point;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public String getSubmit_way() {
        return submit_way;
    }

    public void setSubmit_way(String submit_way) {
        this.submit_way = submit_way;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public ArrayList<BaseQuestion> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<BaseQuestion> children) {
        this.children = children;
    }

    public QuestionShowType getShowType() {
        return showType;
    }

    public void setShowType(QuestionShowType showType) {
        this.showType = showType;
    }

    public ArrayList<Integer> levelPositions = new ArrayList<>();//标记该题所处的准确位置
    public int prefixNumber;//题号前缀--分子
    public int postfixNumber;//题号后缀--分母


    /**
     * 标记层级和所处层级的index
     * @param level
     * @param position
     * @param levelPosition
     */
    public void markLevelAndPosition(int level, int position, ArrayList<Integer> levelPosition) {
//        this.level = level;
//        this.position = position;
        if (levelPosition != null) {
            this.levelPositions.addAll(levelPosition);
        }
        this.levelPositions.add(position);

        int childPosition = 0;
        for (BaseQuestion node : children) {
            node.markLevelAndPosition(level+1, childPosition++, this.levelPositions);
        }
    }

    /**
     * 生成总数量
     * @param total
     * @param indexOfSameLevel
     * @param totalOfSameLevel
     * @return
     */
    public int generateTotalNumber(int total, int indexOfSameLevel, int totalOfSameLevel) {
        prefixNumber = total + 1;

        if (isNodeCountForTotal()) { //该题目是记题号的
            total++;
        }

        if (isChildNodeCountForTotal()) { //该题目下的子题不重新记题号
            int childPosition = 0;
            for (BaseQuestion node : children) {
                total = node.generateTotalNumber(total, childPosition, children.size());   // 子题也改变total值
            }
        } else { //子题重新记题号
            int childPosition = 0;
            for (BaseQuestion node : children) {
                node.generateTotalNumber(0, childPosition, children.size());   // 子题不改变total值
            }
        }

        return total;
    }

    /**
     *
     * @param total
     */
    public void setPostfixNumber(int total) {
        this.postfixNumber = total;
        if (!isChildNodeCountForTotal()) {  // 子题重新计算total
            total = 0;
            int childPosition = 0;
            for (BaseQuestion node : children) {
                total = node.generateTotalNumber(total, childPosition, children.size());
            }
            for (BaseQuestion node : children) {
                node.setPostfixNumber(total);
            }
            return;
        }

        // 子题继续父题的total
        for (BaseQuestion node : children) {
            node.setPostfixNumber(total);
        }
    }

    public String numberStringForShow() {
        if (!isNodeCountForTotal()) {
            return "";
        }

        return prefixNumber + " / " + postfixNumber;
    }

    /**
     * 判断当前题目是否是记题号的
     * @return
     */
    public Boolean isNodeCountForTotal() {
        if (template.equals(QuestionTemplate.CLOZE)) {
            return false;
        }
        return true;
    }

    /**
     * 判断该题目下的子题是否重记题号
     * 如果当前题目是计数的，那么其子题题号重记；
     * 判断如果是不计入题号的题，子题不重新记号；如果是计入题号的题，子题重新记号
     * true 不重新记号
     * false 重新记号
     * @return
     */
    public Boolean isChildNodeCountForTotal() {
        if (template.equals(QuestionTemplate.CLOZE)) {
            return true;
        }

        // 子题不改变total值，则需要重新计数，total为此层级的Total
        return false;
    }

    /**
     * 答题卡生成题号
     * @return
     */
    public ArrayList<BaseQuestion> allNodesThatHasNumber() {
        ArrayList<BaseQuestion> retNodes = new ArrayList<>();
        if (isNodeCountForTotal()) {
            retNodes.add(this);
        }

        if (children.size() > 0) {
            for (BaseQuestion node : children) {
                retNodes.addAll(node.allNodesThatHasNumber());
            }
        }

        return retNodes;
    }

    protected void clearAllNumberData() {
//        level = 0;
//        position = 0;
        levelPositions.clear();
        prefixNumber = 0;
        postfixNumber = 0;
        if (children != null) {
            for (BaseQuestion node : children) {
                node.clearAllNumberData();
            }
        }
    }
}
