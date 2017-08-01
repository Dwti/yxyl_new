package com.yanxiu.gphone.student.user.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.constant.Constants;

import java.lang.reflect.Method;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/28 11:47.
 * Function :
 */
public class PrivacyActivity extends YanxiuBaseActivity implements View.OnClickListener{

    private static final String URL="url";

    private Context mContext;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private ProgressBar mProgressBar;
    private WebView mWebView;

    private String mBaseUrl;

    private Handler handler=new Handler();

    public static void LaunchActivity(Context context,String url){
        Intent intent=new Intent(context,PrivacyActivity.class);
        url = url.replaceAll(" ", "");
        if (!TextUtils.isEmpty(url)) {
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }
            intent.putExtra(URL, url);
        }
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        mContext=PrivacyActivity.this;
        setContentView(R.layout.activity_privacy);
        mBaseUrl = getIntent().getStringExtra(URL);
        initView();
        listener();
        initData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    private void listener() {
        mBackView.setOnClickListener(PrivacyActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.setting_about);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        mTitleView.setText(getText(R.string.privacy_policy_txt));

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUserAgentString(createUA());
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new LetvWebViewClient());
        mWebView.setWebChromeClient(new LetvWebViewChromeClient());
        mWebView.loadUrl(mBaseUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                PrivacyActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        callHiddenWebViewMethod("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        callHiddenWebViewMethod("onResume");
    }

    private void callHiddenWebViewMethod(String name) {
        if (mWebView != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(mWebView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LetvWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                view.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished (WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            if (title.equals("- no title specified")){
                    mTitleView.setText(R.string.privacy_policy_txt);
            }else {
//                mTitleView.setText(title);
            }
        }
    }

    private class LetvWebViewChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mProgressBar.getVisibility() != View.VISIBLE) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        releaseWebView();
        super.onBackPressed();
    }

    private  void releaseWebView(){
        try {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            if (mWebView != null) {
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.stopLoading();
                mWebView.clearAnimation();
                mWebView.setVisibility(View.GONE);
                mWebView.removeAllViews();
                long timeout = ViewConfiguration.getZoomControlsTimeout();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.destroy();
                    }
                },timeout);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createUA() {
        String ua = "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) " + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1";
        ua += " " + "YanxiuMobileClient_" + Constants.version + "_android";
        return ua;
    }

}
