package com.yanxiu.gphone.student.learning.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufengqing on 2018/1/31.
 */

public class VideoDataBean implements Serializable {
    /**
     * adminUserId : 1
     * chapter : ["205321","205322","205323","205324"]
     * adminUserName : admin
     * updateTime : 1516168368
     * res_size_format : 9.1MB
     * rid : 28940016
     * title : 同步微课
     * res_name : 同步微课.mp4
     * subjectId : 1103
     * point : [""]
     * res_type : mp4
     * termId : 1203
     * res_download_url : http://upload.ugc.yanxiu.com/video/bfc5d11239a9edb11dba1711bfb05682.mp4?from=106&rid=28940016&filename=%25E5%2590%258C%25E6%25AD%25A5%25E5%25BE%25AE%25E8%25AF%25BE.mp4&attachment=true&uploadto=0&convert_result=%7B%22self%22%3A%22bfc5d11239a9edb11dba1711bfb05682%22%7D&res_source=21
     * viewnum : 9
     * createTime : 1516168368
     * id : 192
     * res_thumb : http://upload.ugc.yanxiu.com/video/bfc5d11239a9edb11dba1711bfb05682_0.jpg?from=106&rid=28940016&uploadto=0&convert_result=%7B%22self%22%3A%22bfc5d11239a9edb11dba1711bfb05682%22%7D&res_source=21
     * category : ["3001"]
     * fileType : 2
     * status : 2
     */

    private int adminUserId;
    private String adminUserName;
    private int updateTime;
    private String res_size_format;
    private int rid;
    private String title;
    private String res_name;
    private int subjectId;
    private String res_type;
    private int termId;
    private String res_download_url;
    private int viewnum;
    private int createTime;
    private String id;
    private String res_thumb;
    private int fileType;
    @SerializedName("status")
    private int statusX;
    private List<String> chapter;
    private List<String> point;
    private List<String> category;
    private String points_string;

    public int getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(int adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public String getRes_size_format() {
        return res_size_format;
    }

    public void setRes_size_format(String res_size_format) {
        this.res_size_format = res_size_format;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getRes_download_url() {
        return res_download_url;
    }

    public void setRes_download_url(String res_download_url) {
        this.res_download_url = res_download_url;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRes_thumb() {
        return res_thumb;
    }

    public void setRes_thumb(String res_thumb) {
        this.res_thumb = res_thumb;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getStatusX() {
        return statusX;
    }

    public void setStatusX(int statusX) {
        this.statusX = statusX;
    }

    public List<String> getChapter() {
        return chapter;
    }

    public void setChapter(List<String> chapter) {
        this.chapter = chapter;
    }

    public List<String> getPoint() {
        return point;
    }

    public String getPoint_string() {
        String string="";
        for (String s:point){
            if (TextUtils.isEmpty(string)){
                string+=s;
            }else {
                string+=(","+s);
            }
        }
        this.points_string=string;
        return points_string;
    }

    public void setPoint(List<String> point) {
        this.point = point;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
