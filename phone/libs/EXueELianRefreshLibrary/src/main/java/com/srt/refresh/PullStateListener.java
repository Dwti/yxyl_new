package com.srt.refresh;

/**
 * Created by 戴延枫 on 2017/8/3.
 * 下拉状态回调监听
 */
public interface PullStateListener {
    /**
     * 下拉中
     * @param baseRefreshLayout
     * @param offsetY
     */
    void onPulling(BaseRefreshLayout baseRefreshLayout, float offsetY);

    /**
     * 下拉松开
     * @param baseRefreshLayout
     * @param offsetY
     */
    void onPullReleasing(BaseRefreshLayout baseRefreshLayout, float offsetY);

    /**
     * 加载更多的上拉中回调
     * @param baseRefreshLayout
     * @param offsetY
     */
    void onLoadMorePulling(BaseRefreshLayout baseRefreshLayout, float offsetY);
    /**
     * 加载更多的上拉松开回调
     * @param baseRefreshLayout
     * @param offsetY
     */
    void onLoadMorePullReleasing(BaseRefreshLayout baseRefreshLayout, float offsetY);

}
