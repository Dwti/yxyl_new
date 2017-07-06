package com.yanxiu.gphone.student.questions.bean;

import com.yanxiu.gphone.student.base.BaseBean;

import java.util.List;

/**
 * Created by sunpeng on 2017/5/10.
 */

public class QuestionBean extends BaseBean {
    private String analysis;
    private List<String> answer;
    private ContentBean content;
    private List<PaperTestBean> children;
    private String difficulty;
    private String id;
    private String memo;
    private PadBean pad = new PadBean();
    private Extend extend;
    private List<PointBean> point;
    private String stem;
    private String submit_way;
    private String template;
    private String type_id;
    private String url;

    public Extend getExtend() {
        return extend;
    }

    public void setExtend(Extend extend) {
        this.extend = extend;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<PaperTestBean> getChildren() {
        return children;
    }

    public void setChildren(List<PaperTestBean> children) {
        this.children = children;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
