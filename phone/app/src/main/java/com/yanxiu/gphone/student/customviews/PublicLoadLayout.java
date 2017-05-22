package com.yanxiu.gphone.student.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by 戴延枫 on 2017/5/17.
 */

public class PublicLoadLayout extends FrameLayout{

    private Context mContext;
    private FrameLayout mContentViewContainer;//加载具体页面的contentView
    private RelativeLayout mErrorLayoutContainer;//错误页面容器，默认网络错误页面
    private View mNetErrorLayout;//错误页面容器，默认网络错误页面
    private Button mRetry_button;//重试按钮
    private ImageView mLoadingView;// loadingView
    private Animation mLoadingAnim;//loadingView动画

    public PublicLoadLayout(Context context) {
        this(context, null);
    }

    public PublicLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublicLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        mContext = context;
        inflate(context, R.layout.public_load_layout, this);
        mContentViewContainer = (FrameLayout) findViewById(R.id.contentViewContainer);
        mErrorLayoutContainer = (RelativeLayout) findViewById(R.id.errorLayoutContainer);
        initDefaultLayout();
        initLoadingView();
    }

    /**
     * 加载默认布局（网络错误页面）
     */
    private void initDefaultLayout() {
        mNetErrorLayout = inflate(mContext, R.layout.net_error_layout, mErrorLayoutContainer);
        mRetry_button = (Button) findViewById(R.id.retry_button);
    }

    /**
     * 初始化加载动画布局
     */
    private void initLoadingView() {
        mLoadingView = (ImageView) findViewById(R.id.loadingView);
        mLoadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.lodingview_progress);
        LinearInterpolator lin = new LinearInterpolator();
        mLoadingAnim.setInterpolator(lin);
        mLoadingView.setVisibility(GONE);
    }

    /**
     * 显示loadingView
     */
    public void showLoadingView() {
        if (mLoadingView != null && mLoadingAnim != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            mLoadingView.clearAnimation();
            mLoadingView.startAnimation(mLoadingAnim);
        }
    }

    /**
     * 隐藏LoadingView
     */
    public void hiddenLoadingView() {
        if (mLoadingView != null && mLoadingAnim != null) {
            mLoadingView.clearAnimation();
            mLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 给默认的网络错误界面的重试button添加点击事件。
     * 如果是网络错误界面，必须传入该监听，否则，点击无效。
     *
     * @param onclickListener
     */
    public void setRetryButtonOnclickListener(OnClickListener onclickListener) {
        mRetry_button.setOnClickListener(onclickListener);
    }

    /**
     * 获取重试按钮
     * @return
     */
    public View getReturyButton(){
        return mRetry_button;
    }


    /**
     * 设置rootview
     *
     * @param rootViewId 页面的根viewid
     */
    public void setContentView(int rootViewId) {
        inflate(mContext, rootViewId, mContentViewContainer);//加载传进来的具体页面的contentView

    }
    /**
     * 设置rootview
     *
     * @param rootView 页面的根viewid
     */
    public void setContentView(View rootView) {
        mContentViewContainer.addView(rootView);//加载传进来的具体页面的contentView
    }

    /**
     * 设置要展示的view（如数据为空的view）
     */
    public void setCustomView(int viewId) {
        int childCound = mErrorLayoutContainer.getChildCount();
        if (childCound > 0)
            mErrorLayoutContainer.removeAllViews();//先移除容器内的默认布局
        inflate(mContext, viewId, mErrorLayoutContainer);//加载其他页面（如数据为空页面）
    }
    /**
     * 设置要展示的view（如数据为空的view）
     */
    public void setCustomView(View view) {
        int childCound = mErrorLayoutContainer.getChildCount();
        if (childCound > 0)
            mErrorLayoutContainer.removeAllViews();//先移除容器内的默认布局
        mErrorLayoutContainer.addView(view);//加载其他页面（如数据为空页面）
    }

    /**
     * Errorlayout默认topMargin = 55dp，使rootview中的titlebar能够露出来
     * 调用该方法topMargin = 0dp，errorlayout全屏
     *
     */
    public void setErrorLayoutFullScreen() {
        LayoutParams lp = (LayoutParams) mErrorLayoutContainer.getLayoutParams();
        lp.topMargin = 0;
        mErrorLayoutContainer.setLayoutParams(lp);
//      mErrorLayoutContainer.setPadding(0,0,0,0);
    }

    /**
     * 隐藏异常界面和lodingView，展示正常界面
     */
    public void finish(){
        hiddenLoadingView();
        mErrorLayoutContainer.setVisibility(GONE);
    }

}
