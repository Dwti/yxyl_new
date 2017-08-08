package com.srt.refresh;

/**
 * Created by 戴延枫 on 2017/8/3.
 * 刷新回调接口
 */
public interface PullToRefreshListener {
    /**
     * 刷新中。。。
     * @param baseRefreshLayout
     */
    void onRefresh(BaseRefreshLayout baseRefreshLayout);

    /**
     * 加载更多中
     * @param baseRefreshLayout
     */
    void onLoadMore(BaseRefreshLayout baseRefreshLayout);
}
