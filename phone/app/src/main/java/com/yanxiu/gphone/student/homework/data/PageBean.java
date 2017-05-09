package com.yanxiu.gphone.student.homework.data;

/**
 * Created by sunpeng on 2017/5/8.
 */

public class PageBean {
    private int totalPage ;
    private int pageSize ;
    private int nextPage;
    private int totalCou ;

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
