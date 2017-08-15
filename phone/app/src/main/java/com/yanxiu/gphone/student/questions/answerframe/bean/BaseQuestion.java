package com.yanxiu.gphone.student.questions.answerframe.bean;

import android.text.TextUtils;

import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionConvertFactory;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionUtil;
import com.yanxiu.gphone.student.questions.bean.JsonNoteBean;
import com.yanxiu.gphone.student.questions.bean.PadBean;
import com.yanxiu.gphone.student.questions.bean.PaperTestBean;
import com.yanxiu.gphone.student.questions.bean.PointBean;
import com.yanxiu.gphone.student.util.StringUtil;

import java.io.Serializable;
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
    protected int wqid;
    protected int wqnumber;
    protected String qtype;
    protected String analysis;
    //下面这些字段需要需要具体的题型去设置，并不是每个题型都有的（到时候字段的设置需要再检查一遍）
    protected List<Object> server_answer;//就是answer字段，因为已经定义了保存答案的answer字段，避免重名
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
    protected JsonNoteBean jsonNoteBean;

    private Object ansewr;//已回答的问题

    private String stem_complexToSimple;//只有一个子题的复合题的主题干(复合题转成单题显示)
    private String template_complexToSimple;//只有一个子题的复合题的大题的template(复合题转成单题显示)
    private String type_id_complexToSimple;//只有一个子题的复合题的大题的type_id(复合题转成单题显示)
    private String url_listenComplexToSimple;//只有一个子题的听力复合题的大题的url(复合题转成单题显示)
    private String padId_ComplexToSimple;//只有一个子题的复合题的大题的pad里的id(复合题转成单题，提交答案时使用)
    private String ptid_ComplexToSimple;//只有一个子题的复合题的大题的上传答案时的ptid(复合题转成单题，提交答案时使用)
    private String qid_ComplexToSimple;//只有一个子题的复合题的大题的上传答案时的qid(复合题转成单题，提交答案时使用)

    protected boolean isAnswer;
    protected long costtime;//答题时间

    protected int status = 3;// 题目状态 0 回答正确， 1 回答错误，  2 半对   3 未作答案  4 标示主观题 已作答

    protected boolean isComplexQuestion;//是否是复合题 true : 是

    protected int parentNumber = -1;//答题卡父题记题号的子题，需要；
    protected String parentType_id;//答题报告里，需要展示父题的题型名称

    private ReportAnswerBean reportAnswerBean;
    private String qaName;//每个题型对应type_id的汉字名称，用来在答题时显示题目类型

    /**
     * 听力是否需要播放
     * */
    public boolean mIsShouldPlay=false;
    /**
     * 听力是否处于暂停状态
     * */
    public boolean mIsPause=false;
    /**
     * 听力最大时长
     * */
    public int mMax=0;
    /**
     * 听力当前进度
     * */
    public int mProgress=0;
    private String mPaperStatus;//数据来源：paperStatus-status。解析力需要判断paperStatus

    public BaseQuestion(PaperTestBean bean,QuestionShowType showType,String paperStatus){
        this.id = bean.getId();
        this.correctRate = bean.getCorrectRate();
        this.difficulty = bean.getDifficulty();
        this.isfavorite = bean.getIsfavorite();
        this.knowledgepoint = bean.getKnowledgepoint();
        this.parentid = bean.getParentid();
        this.pid = bean.getPid();
        this.qid = bean.getQid();
        this.wqid=bean.getWqid();
        try {
            this.jsonNoteBean=bean.getQuestions().getJsonNote();
            if (jsonNoteBean==null){
                jsonNoteBean=new JsonNoteBean();
            }
            jsonNoteBean.setQid(qid);
            jsonNoteBean.setWqid(String.valueOf(wqid));
        }catch (Exception e){
            e.printStackTrace();
        }
        this.wqnumber=bean.getWqnumber();
        this.qtype = bean.getQtype();
        server_answer = bean.getQuestions().getAnswer();
        this.analysis = bean.getQuestions().getAnalysis();
//        this.pad = bean.getQuestions().getPad();
        this.point = bean.getQuestions().getPoint();
        this.stem = bean.getQuestions().getStem();
        this.submit_way = bean.getQuestions().getSubmit_way();
        this.template = bean.getQuestions().getTemplate();
        this.type_id = bean.getQuestions().getType_id();
        this.sectionid = bean.getSectionid();
        this.typeid = bean.getTypeid();
        children = QuestionConvertFactory.convertQuestion(bean.getQuestions().getChildren(),showType,paperStatus);
        if(children == null){
            children = new ArrayList<>();
            isComplexQuestion = false;
        }else if(children.size() > 0){
            isComplexQuestion = true;
        }

        this.showType = showType;

        if(showType.equals(QuestionShowType.ANSWER)){ //答题，加载本地数据库答案
            String answerJson = SaveAnswerDBHelper.getAnswerJson(SaveAnswerDBHelper.makeId(this));
            if(!TextUtils.isEmpty(answerJson))
                bean.getQuestions().getPad().setAnswer(answerJson);
            boolean isAnswered = SaveAnswerDBHelper.getIsAnswered(SaveAnswerDBHelper.makeId(this));
            isAnswer = isAnswered;
        }
        this.pad = bean.getQuestions().getPad();
        try{
            int type_id = Integer.parseInt(bean.getTypeid());
            qaName = QuestionUtil.getQuestionTypeNameByParentTypeId(type_id);
        }catch(Exception e){
            e.printStackTrace();
        }
        mPaperStatus = paperStatus;
        initTemplateOfAnswerStem();
    }

    /**
     *计算题和及答题的小题，题干需要拼接小题的index，诸如：（1）、（2）
     */
    public void initTemplateOfAnswerStem(){
        try{
            int type_id = Integer.parseInt(getType_id());
            if(type_id == 8 || type_id == 22){ //计算或解答题
                if(children != null && !children.isEmpty()){
                    for(int index =0; index<children.size();index++){
                        String stem = "(" + (index + 1) + ")" + "<br/>" + children.get(index).getStem();
                        children.get(index).setStem(stem);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ExerciseBaseFragment getFragment() {
        ExerciseBaseFragment fm = null;
        if (showType.equals(QuestionShowType.ANSWER)) {
            fm = answerFragment();
        }

        if (showType.equals(QuestionShowType.ANALYSIS)) {
            fm = analysisFragment();
        }

        if (showType.equals(QuestionShowType.MISTAKE_ANALYSIS)){
            fm=wrongFragment();
        }

        fm.setData(this);
        return fm;
    }

    public abstract ExerciseBaseFragment answerFragment();

    public abstract ExerciseBaseFragment analysisFragment();

    public abstract ExerciseBaseFragment wrongFragment();

    /**
     * 获取答案
     */
    public abstract Object getAnswer();

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

    public int getWqid() {
        return wqid;
    }

    public void setWqid(int wqid) {
        this.wqid = wqid;
    }

    public JsonNoteBean getJsonNoteBean() {
        return jsonNoteBean;
    }

    public void setJsonNoteBean(JsonNoteBean jsonNoteBean) {
        this.jsonNoteBean = jsonNoteBean;
    }

    public int getWqnumber() {
        return wqnumber;
    }

    public void setWqnumber(int wqnumber) {
        this.wqnumber = wqnumber;
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

    public boolean getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(boolean answer) {
        isAnswer = answer;
    }

    public String getStem_complexToSimple() {
        return stem_complexToSimple;
    }

    public void setStem_complexToSimple(String stem_complexToSimple) {
        this.stem_complexToSimple = stem_complexToSimple;
    }
    public String getTemplate_complexToSimple() {
        return template_complexToSimple;
    }

    public void setTemplate_complexToSimple(String template_complexToSimple) {
        this.template_complexToSimple = template_complexToSimple;
    }
    public String getTypeId_complexToSimple() {
        return type_id_complexToSimple;
    }

    public void setTypeId_complexToSimple(String type_id_complexToSimple) {
        this.type_id_complexToSimple = type_id_complexToSimple;
    }
    public String getUrl_listenComplexToSimple() {
        return url_listenComplexToSimple;
    }

    public void setUrl_listenComplexToSimple(String url_listenComplexToSimple) {
        this.url_listenComplexToSimple = url_listenComplexToSimple;
    }

    public int getParentNumber() {
        return parentNumber;
    }


    public abstract int getStatus();

    public long getCosttime() {
        return costtime;
    }

    public void setCosttime(long costtime) {
        this.costtime = costtime;
    }

    public boolean isComplexQuestion() {
        return isComplexQuestion;
    }

    public ReportAnswerBean getReportAnswerBean() {
        if(reportAnswerBean == null){
            reportAnswerBean = new ReportAnswerBean();
        }
        return reportAnswerBean;
    }

    public void setReportAnswerBean(ReportAnswerBean reportAnswerBean) {
        this.reportAnswerBean = reportAnswerBean;
    }

    public List<Object> getServer_answer() {
        return server_answer;
    }

    public void setServer_answer(List<Object> server_answer) {
        this.server_answer = server_answer;
    }

    public String getQaName() {
        return qaName;
    }

    public void setQaName(String qaName) {
        this.qaName = qaName;
    }

    public String getPaperStatus() {
        return mPaperStatus;
    }

    public void setPaperStatus(String mPaperStatus) {
        this.mPaperStatus = mPaperStatus;
    }

    public String getParentType_id() {
        return parentType_id;
    }

    public String getPadId_ComplexToSimple() {
        return padId_ComplexToSimple;
    }

    public void setPadId_ComplexToSimple(String padId_ComplexToSimple) {
        this.padId_ComplexToSimple = padId_ComplexToSimple;
    }

    public String getPtid_ComplexToSimple() {
        return ptid_ComplexToSimple;
    }

    public void setPtid_ComplexToSimple(String ptid_ComplexToSimple) {
        this.ptid_ComplexToSimple = ptid_ComplexToSimple;
    }

    public String getQid_ComplexToSimple() {
        return qid_ComplexToSimple;
    }

    public void setQid_ComplexToSimple(String qid_ComplexToSimple) {
        this.qid_ComplexToSimple = qid_ComplexToSimple;
    }


    /**
     * 题号逻辑开始
     */
    private ArrayList<Integer> levelPositions = new ArrayList<>();//标记该题所处的准确位置
    private int prefixNumber;//题号前缀--分子
    private int postfixNumber;//题号后缀--分母

    /**
     * 错题记题号设置题号分子
     * @param prefixNumber
     */
    public  void setPrefixNumberForWrongQuestion(int prefixNumber) {
        this.prefixNumber = prefixNumber;
    }
    /**
     * 错题记题号设置题号分母
     * @param postfixNumber
     */
    public void setPostfixNumberForWrongQuestion(int postfixNumber) {
        this.postfixNumber = postfixNumber;
    }

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
        if (!showType.equals(QuestionShowType.MISTAKE_ANALYSIS)&&!isNodeCountForTotal()) {
            return "";
        }

        return prefixNumber + " / " + postfixNumber;
    }

    /**
     * 判断当前题目是否是记题号的
     * @return
     */
    public Boolean isNodeCountForTotal() {
//        if (template.equals(QuestionTemplate.CLOZE)) {
        if (isComplexQuestion && !type_id.equals("8") && !type_id.equals("22")) {
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
//        if (template.equals(QuestionTemplate.CLOZE)) {
        if (isComplexQuestion && !type_id.equals("8") && !type_id.equals("22")) {
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
        boolean parentIsNodeCountForTotal = false;
        if (isNodeCountForTotal()) {
            if(!isComplexQuestion)
                retNodes.add(this);
            parentIsNodeCountForTotal = true;
        }

        if (children.size() > 0) {
            for (BaseQuestion node : children) {
                if(parentIsNodeCountForTotal){
                    node.parentNumber = prefixNumber;
                }
                node.parentType_id = type_id;
                retNodes.addAll(node.allNodesThatHasNumber());
            }
        }

        return retNodes;
    }

    /**
     * 答题卡复合题获取分子数字
     * @return
     */
    public int getAnswerCardPrefixNumber(){
        if(parentNumber != -1){
            return parentNumber;
        }
        return -1;
    }

    /**
     * 答题卡复合题获取分母数字
     * @return
     */
    public int getAnswerCardPostfixNumber(){
        if(parentNumber != -1){
            return prefixNumber;
        }
        return -1;
    }

    /**
     * 答题卡单题题号显示
     * @return
     */
    public String getAnswerCardSimpleNumber() {

        return prefixNumber +"";
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

    public ArrayList<Integer> getLevelPositions() {
        return levelPositions;
    }
}
