package com.yanxiu.gphone.student.user.userinfo.response;

import com.yanxiu.gphone.student.base.BaseBean;
import com.yanxiu.gphone.student.base.EXueELianBaseResponse;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/4 13:00.
 * Function :
 */
public class UserUpdataImageResponse extends EXueELianBaseResponse {

    private List<ImagePathBean> data;

    public List<ImagePathBean> getData() {
        return data;
    }

    public void setData(List<ImagePathBean> data) {
        this.data = data;
    }

    public class ImagePathBean extends BaseBean {
        private String id;
        private String head;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }
    }
}
