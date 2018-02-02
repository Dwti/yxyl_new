package com.yanxiu.gphone.student.learning.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;
import com.yanxiu.gphone.student.learning.bean.VideoDataBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufengqing on 2018/1/30.
 */

public class GetResourceListDataResponse extends EXueELianBaseResponse {
    /**
     * page : {"totalPage":1,"pageSize":10,"nextPage":0,"totalCou":1}
     * property : {"view":"thumb"}
     * data : [{"adminUserId":1,"chapter":["205321","205322","205323","205324"],"adminUserName":"admin","updateTime":1516168368,"res_size_format":"9.1MB","rid":28940016,"title":"同步微课","res_name":"同步微课.mp4","subjectId":1103,"point":[""],"res_type":"mp4","termId":1203,"res_download_url":"http://upload.ugc.yanxiu.com/video/bfc5d11239a9edb11dba1711bfb05682.mp4?from=106&rid=28940016&filename=%25E5%2590%258C%25E6%25AD%25A5%25E5%25BE%25AE%25E8%25AF%25BE.mp4&attachment=true&uploadto=0&convert_result=%7B%22self%22%3A%22bfc5d11239a9edb11dba1711bfb05682%22%7D&res_source=21","viewnum":9,"createTime":1516168368,"id":192,"res_thumb":"http://upload.ugc.yanxiu.com/video/bfc5d11239a9edb11dba1711bfb05682_0.jpg?from=106&rid=28940016&uploadto=0&convert_result=%7B%22self%22%3A%22bfc5d11239a9edb11dba1711bfb05682%22%7D&res_source=21","category":["3001"],"fileType":2,"status":2}]
     */

    private PageBean page;
    private PropertyBean property;
    private List<VideoDataBean> data;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public PropertyBean getProperty() {
        return property;
    }

    public void setProperty(PropertyBean property) {
        this.property = property;
    }

    public List<VideoDataBean> getData() {
        return data;
    }

    public void setData(List<VideoDataBean> data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * totalPage : 1
         * pageSize : 10
         * nextPage : 0
         * totalCou : 1
         */

        private int totalPage;
        private int pageSize;
        private int nextPage;
        private int totalCou;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getTotalCou() {
            return totalCou;
        }

        public void setTotalCou(int totalCou) {
            this.totalCou = totalCou;
        }
    }

    public static class PropertyBean {
        /**
         * view : thumb
         */

        private String view;

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }
    }
}
