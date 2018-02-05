package com.yanxiu.gphone.student.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.login.request.LoginThridRequest;
import com.yanxiu.gphone.student.login.response.LoginResponse;
import com.yanxiu.gphone.student.login.request.LoginRequest;
import com.yanxiu.gphone.student.login.response.ThridMessageBean;
import com.yanxiu.gphone.student.login.response.UserMessageBean;
import com.yanxiu.gphone.student.user.setting.activity.BindMobileActivity;
import com.yanxiu.gphone.student.util.ActivityManger;
import com.yanxiu.gphone.student.util.EditTextManger;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.NoticeUtil;
import com.yanxiu.gphone.student.util.SysEncryptUtil;
import com.yanxiu.gphone.student.util.SystemUtil;
import com.yanxiu.gphone.student.util.ToastManager;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.util.UpdateUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
@SuppressWarnings("all")
/**
 * Created by Canghaixiao.
 * Time : 2017/5/8 16:23.
 * Function :
 */

public class LoginActivity extends YanxiuBaseActivity implements View.OnClickListener, EditTextManger.onTextLengthChangedListener, OnPermissionCallback {

    public static final String THRID_LOGIN="thrid";
    public static final String TYPE="type";
    public static final String TYPE_DEFAULT="default";
    public static final String TYPE_THRID="thrid";

    private Context mContext;

    private EditText mUserNameView;
    private ImageView mClearView;
    private EditText mPassWordView;
    private ImageView mCipherView;
    private TextView mLoginView;
    private TextView mForgetPassWordView;
    private TextView mFastRegisteredView;
    private ImageView mThirdQQView;
    private ImageView mThirdWXView;
    private WavesLayout mWavesView;

    /**
     * The default username and password are empty
     */
    private boolean isUserNameReady = false;
    private boolean isPassWordReady = false;
    /**
     * The default password is cipher
     */
    private boolean isCipher = true;

    /**
     * check is number
     * */
    private Pattern pattern = Pattern.compile("[0-9]*");
    private LoginRequest mLoginRequest;
    private PublicLoadLayout rootView;
    private UMShareAPI mUMShareAPI;
    private ThridMessageBean mThridMessageBean;
    private LoginThridRequest mLoginThridRequest;
    private LinearLayout mThridLoginView;

    public static void LaunchActivity(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        mContext=LoginActivity.this;
        rootView=new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_login);
        UpdateUtil.Initialize(mContext,false);
        NoticeUtil.Initialize(mContext);
        setContentView(rootView);
        mUMShareAPI=UMShareAPI.get(mContext);
        initView();
        listener();
        initData();
    }

    private OnPermissionCallback callback=new OnPermissionCallback() {
        @Override
        public void onPermissionsGranted(List<String> deniedPermissions) {
            UpdateUtil.Initialize(mContext,false);
        }

        @Override
        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
//            ToastManager.showMsg(R.string.no_storage_permissions);
        }
    };

    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.ed_user_name);
        mClearView = (ImageView) findViewById(R.id.iv_clear);
        mPassWordView = (EditText) findViewById(R.id.ed_pass_word);
        mCipherView = (ImageView) findViewById(R.id.iv_cipher);
        mLoginView = (TextView) findViewById(R.id.tv_login);
        mForgetPassWordView = (TextView) findViewById(R.id.tv_forget_password);
        mFastRegisteredView = (TextView) findViewById(R.id.tv_fast_registered);
        mThirdQQView = (ImageView) findViewById(R.id.iv_third_qq);
        mThirdWXView = (ImageView) findViewById(R.id.iv_third_wx);
        mWavesView= (WavesLayout) findViewById(R.id.wl_login_waves);
        mThridLoginView= (LinearLayout) findViewById(R.id.ll_thrid_login);
        checkInstallThridSystem();
    }

    private void initData() {
        mClearView.setEnabled(false);
        mLoginView.setEnabled(false);

        mUserNameView.setText("c10000481s001");
        mPassWordView.setText("123456");
    }

    private void listener() {
        mClearView.setOnClickListener(LoginActivity.this);
        mCipherView.setOnClickListener(LoginActivity.this);
        mLoginView.setOnClickListener(LoginActivity.this);
        mForgetPassWordView.setOnClickListener(LoginActivity.this);
        mFastRegisteredView.setOnClickListener(LoginActivity.this);
        mThirdQQView.setOnClickListener(LoginActivity.this);
        mThirdWXView.setOnClickListener(LoginActivity.this);
        EditTextManger.getManager(mPassWordView).setInputAllNotHanzi().setTextChangedListener(LoginActivity.this);
        EditTextManger.getManager(mUserNameView).setInputNumberAndLetter().setTextChangedListener(LoginActivity.this);
    }

    @Override
    public void onBackPressed() {
        ActivityManger.destoryAll();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginRequest!=null){
            mLoginRequest.cancelRequest();
            mLoginRequest=null;
        }
        if (mLoginThridRequest!=null){
            mLoginThridRequest.cancelRequest();
            mLoginThridRequest=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInstallThridSystem();
//        YanxiuBaseActivity.requestWriteAndReadPermission(LoginActivity.this);
    }

    private void checkInstallThridSystem(){
        boolean isInstanllWx=SystemUtil.checkBrowser(mContext, "com.tencent.mm");
        boolean isInstallQq=SystemUtil.checkBrowser(mContext,"com.tencent.mobileqq");

        //隐藏三方登陆
        isInstallQq=false;
        isInstanllWx=false;

        if (!isInstallQq&&!isInstanllWx){
            mThridLoginView.setVisibility(View.GONE);
        }else {
            mThridLoginView.setVisibility(View.VISIBLE);
            if (isInstanllWx){
                mThirdWXView.setVisibility(View.VISIBLE);
            }else {
                mThirdWXView.setVisibility(View.GONE);
            }
            if (isInstallQq){
                mThirdQQView.setVisibility(View.VISIBLE);
            }else {
                mThirdQQView.setVisibility(View.GONE);
            }
        }
    }

    private void setEditUserNameIsEmpty(boolean isEmpty) {
        if (isEmpty) {
            mClearView.setEnabled(false);
            mClearView.setVisibility(View.INVISIBLE);
        } else {
            mClearView.setEnabled(true);
            mClearView.setVisibility(View.VISIBLE);
        }
    }

    private void setEditPassWordChange(boolean isCipher) {
        if (isCipher) {
            mCipherView.setBackgroundResource(R.drawable.selector_password_hide);
            mPassWordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mCipherView.setBackgroundResource(R.drawable.selector_password_show);
            mPassWordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        String text = mPassWordView.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            mPassWordView.setSelection(text.length());
        }
    }

    private void setButtonFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mWavesView.setCanShowWave(true);
            mLoginView.setEnabled(true);
        } else {
            mWavesView.setCanShowWave(false);
            mLoginView.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        String userName;
        String passWord;
        switch (v.getId()) {
            case R.id.iv_clear:
                mUserNameView.setText("");
                break;
            case R.id.iv_cipher:
                this.isCipher = !isCipher;
                setEditPassWordChange(isCipher);
                break;
            case R.id.tv_login:
                userName = mUserNameView.getText().toString().trim();
                passWord = mPassWordView.getText().toString().trim();
                if (userName.length()<11||userName.length()>16){
                    ToastManager.showMsg(getText(R.string.input_true_account));
                    return;
                }
                if (passWord.length()<6||passWord.length()>18){
                    ToastManager.showMsg(getText(R.string.input_true_password));
                    return;
                }
                LoginByAccount(userName, passWord);
                break;
            case R.id.tv_forget_password:
                userName = mUserNameView.getText().toString().trim();
                if (userName.length()!=11||!userName.substring(0,1).equals("1")||!pattern.matcher(userName).matches()){
                    userName="";
                }
                ForgetPassWordActivity.LaunchActivity(mContext,userName);
//                ResetPassWordActivity.LaunchActivity(mContext);
                break;
            case R.id.tv_fast_registered:
                RegisterActivity.LaunchActivity(mContext);
//                JoinClassActivity.LaunchActivity(mContext);
                break;
            case R.id.iv_third_qq:
                LoginByQQ();
                break;
            case R.id.iv_third_wx:
                LoginByWX();
                break;
        }
    }

    private void LoginByAccount(final String user_name, final String pass_word) {
        rootView.showLoadingView();
        mLoginRequest = new LoginRequest();
        mLoginRequest.mobile= user_name;
        mLoginRequest.password=SysEncryptUtil.getMd5_32(pass_word);
        mLoginRequest.startRequest(LoginResponse.class, new EXueELianBaseCallback<LoginResponse>() {

            @Override
            protected void onResponse(RequestBase request, LoginResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0&&response.data!=null&&response.data.size()>0){
                    if (TextUtils.isEmpty(response.data.get(0).getMobile())){
                        LoginInfo.setCacheData(response.data.get(0));
                        LoginInfo.setLoginType(UserMessageBean.LOGIN_ACCOUNT);
                        BindMobileActivity.LaunchActivity(mContext,BindMobileActivity.COME_TYPE_LOGIN);
                    }else {
                        LoginInfo.saveCacheData(response.data.get(0));
                        LoginInfo.saveLoginType(UserMessageBean.LOGIN_ACCOUNT);
                        MainActivity.invoke(LoginActivity.this, true);
                        LoginActivity.this.finish();
                    }
                }else if (response.getStatus().getCode()==80){
                    LoginInfo.setMobile(user_name);
                    LoginInfo.setPassWord(pass_word);
                    JoinClassActivity.LaunchActivity(mContext);
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    private void LoginByWX() {
        SHARE_MEDIA media=SHARE_MEDIA.WEIXIN;
        thridLoginStart(media);
    }

    private void LoginByQQ() {
        SHARE_MEDIA media=SHARE_MEDIA.QQ;
        thridLoginStart(media);
    }

    /**
     * get thrid message
     * */
    private void thridLoginStart(final SHARE_MEDIA platform){
        mUMShareAPI.doOauthVerify(LoginActivity.this, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
//                ToastManager.showMsg("doOauth "+"onStart");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                ToastManager.showMsg(R.string.thrid);
                mUMShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
//                            ToastManager.showMsg("PlatformInfo "+"onStart");
                        }

                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                            ToastManager.showMsg("PlatformInfo "+"onComplete");
                            mThridMessageBean =new ThridMessageBean();
                            String openid=map.get("openid");
                            String platform="";
                            if (share_media.toString().endsWith(SHARE_MEDIA.QQ.toString())){
                                platform=ThridMessageBean.TYPE_QQ;
                            }else if (share_media.toString().endsWith(SHARE_MEDIA.WEIXIN.toString())){
                                platform=ThridMessageBean.TYPE_WX;
                            }
                            mThridMessageBean.setType(platform);
                            String uniqid="";
                            mThridMessageBean.openid=openid;
                            mThridMessageBean.platform=platform;
                            mThridMessageBean.uniqid=uniqid;
                            mThridMessageBean.head=map.get("iconurl");
                            mThridMessageBean.sex=map.get("gender").equals(getText(R.string.man).toString())?"1":"0";
                            thridLoginEnd(openid,platform,uniqid);
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            ToastManager.showMsg(getText(R.string.login_fail));
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                        }
                    });
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (share_media.toString().endsWith(SHARE_MEDIA.QQ.toString())){
                    ToastManager.showMsg(getText(R.string.no_install_qq));
                }else if (share_media.toString().endsWith(SHARE_MEDIA.WEIXIN.toString())){
                    ToastManager.showMsg(getText(R.string.no_install_weixin));
                }
//                ToastManager.showMsg("doOauth "+"onError");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
    }

    /**
     * thrid login
     * */
    private void thridLoginEnd(String openid, final String platform, String uniqid){
        rootView.showLoadingView();
        mLoginThridRequest=new LoginThridRequest();
        mLoginThridRequest.openid=openid;
        mLoginThridRequest.platform=platform;
        mLoginThridRequest.uniqid=uniqid;
        mLoginThridRequest.startRequest(LoginResponse.class, new EXueELianBaseCallback<LoginResponse>() {

            @Override
            protected void onResponse(RequestBase request, LoginResponse response) {
                rootView.hiddenLoadingView();
                if (response.getStatus().getCode()==0){
                    LoginInfo.saveCacheData(response.data.get(0));
                    if (platform.equals(ThridMessageBean.TYPE_QQ)){
                        LoginInfo.saveLoginType(UserMessageBean.LOGIN_QQ);
                    }else {
                        LoginInfo.saveLoginType(UserMessageBean.LOGIN_WX);
                    }
                    MainActivity.invoke(LoginActivity.this,true);
//                    LoginActivity.this.finish();
                }else if (response.getStatus().getCode()==80){
                    JoinClassActivity.LaunchActivity(mContext, mThridMessageBean);
                }else {
                    ToastManager.showMsg(response.getStatus().getDesc());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastManager.showMsg(error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUMShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChanged(View view, String value, boolean isEmpty) {
        if (view==mUserNameView){
            if (isEmpty){
                isUserNameReady = false;
            }else {
                isUserNameReady = true;
            }
            setEditUserNameIsEmpty(isEmpty);
        }else if (view==mPassWordView){
            if (isEmpty){
                isPassWordReady = false;
            }else {
                isPassWordReady = true;
            }
        }
        setButtonFocusChange(isUserNameReady&&isPassWordReady);
    }

    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        UpdateUtil.Initialize(mContext,false);
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {

    }
}
