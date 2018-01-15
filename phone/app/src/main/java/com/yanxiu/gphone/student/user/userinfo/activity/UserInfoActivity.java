package com.yanxiu.gphone.student.user.userinfo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.Bean.CropCallbackMessage;
import com.yanxiu.gphone.student.common.activity.AlbumActivity;
import com.yanxiu.gphone.student.common.activity.UserHeadCameraActivity;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.customviews.PublicLoadLayout;
import com.yanxiu.gphone.student.customviews.SinglineTextView;
import com.yanxiu.gphone.student.customviews.UserInfoHeadImageView;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.http.request.UpDataRequest;
import com.yanxiu.gphone.student.login.activity.ChooseLocationActivity;
import com.yanxiu.gphone.student.user.userinfo.dialog.EditorHeadImgDialog;
import com.yanxiu.gphone.student.user.userinfo.response.UserUpdataImageResponse;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/7/31 10:42.
 * Function :
 */
public class UserInfoActivity extends YanxiuBaseActivity implements View.OnClickListener, OnPermissionCallback {

    private Context mContext;
    private PublicLoadLayout rootView;
    private View mTopView;
    private ImageView mBackView;
    private TextView mTitleView;
    private UserInfoHeadImageView mUserHeadImgView;
    private SinglineTextView mUserNameView;
    private TextView mUserSexView;
    private SinglineTextView mUserSchoolView;

    private EditorHeadImgDialog mEditorHeadImgDialog;

    public static void LaunchActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = UserInfoActivity.this;
        EventBus.getDefault().register(mContext);
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_userinfo);
        setContentView(rootView);
        initView();
        listener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.iv_left);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTopView = findViewById(R.id.include_top);

        mUserHeadImgView = (UserInfoHeadImageView) findViewById(R.id.iv_head);
        mUserNameView = (SinglineTextView) findViewById(R.id.tv_name);
        mUserSexView = (TextView) findViewById(R.id.tv_sex);
        mUserSchoolView = (SinglineTextView) findViewById(R.id.tv_school);
    }

    private void listener() {
        mBackView.setOnClickListener(UserInfoActivity.this);
        mUserHeadImgView.setOnClickListener(UserInfoActivity.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.WHITE);
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(R.string.user_message);
        mTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));

        setHeadImg(LoginInfo.getHeadIcon());

        mUserNameView.post(new Runnable() {
            @Override
            public void run() {
                mUserNameView.setData(LoginInfo.getRealName());
            }
        });
        setSexText(LoginInfo.getSex());
        mUserSchoolView.post(new Runnable() {
            @Override
            public void run() {
                mUserSchoolView.setData(LoginInfo.getSchoolName());
            }
        });

        mBackView.setBackgroundResource(R.drawable.selector_back);
    }

    private void setHeadImg(String url) {
        if (!TextUtils.isEmpty(url)) {
            String[] strings = url.split("/");
            if (!"file_56a60c9d7cbd4.jpg".equals(strings[strings.length - 1])) {
                Glide.with(mContext).load(url).asBitmap().placeholder(R.drawable.user_info_headimg_default).format(DecodeFormat.PREFER_ARGB_8888).into(mUserHeadImgView);
            }
        }
    }

    private void setSexText(int sexId) {
        switch (sexId) {
            case Constants.Sex.SEX_TYPE_MAN:
                mUserSexView.setText(R.string.sex_man);
                break;
            case Constants.Sex.SEX_TYPE_WOMAN:
                mUserSexView.setText(R.string.sex_woman);
                break;
            case Constants.Sex.SEX_TYPE_UNKNOWN:
                mUserSexView.setText(R.string.sex_unknown);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.ll_name:
                UserEditNameActivity.LaunchActivity(mContext);
                break;
            case R.id.ll_sex:
                UserSexActivity.LaunchActivity(mContext);
                break;
            case R.id.ll_school:
//                ChooseLocationActivity.LaunchActivity(mContext, mContext.hashCode());
                break;
            case R.id.iv_head:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        if (mEditorHeadImgDialog == null) {
            mEditorHeadImgDialog = new EditorHeadImgDialog(mContext);
            mEditorHeadImgDialog.setClickListener(new EditorHeadImgDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    requestWriteAndReadPermission(UserInfoActivity.this);
                }

                @Override
                public void onCameraClick() {
                    requestCameraPermission(UserInfoActivity.this);
                }
            });
        }
        mEditorHeadImgDialog.show();
    }

    public void onEventMainThread(UserEditNameActivity.EditNameMessage message) {
        if (message != null) {
            mUserNameView.setData(message.name);
        }
    }

    public void onEventMainThread(ChooseLocationActivity.SchoolMessage message) {
        if (message != null && message.requestCode == mContext.hashCode()) {
            mUserSchoolView.setData(message.schoolName);
        }
    }

    public void onEventMainThread(UserSexActivity.SexMessage message) {
        if (message != null) {
            mUserSexView.setText(message.sexTxt);
        }
    }

    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (deniedPermissions.get(0).equals(Manifest.permission.CAMERA)) {
                UserHeadCameraActivity.LaunchActivity(mContext, mContext.hashCode());
            } else if (deniedPermissions.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE) || deniedPermissions.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlbumActivity.LaunchActivity(mContext, mContext.hashCode(), AlbumActivity.COME_FROM_USERINFO);
            }
        }
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (deniedPermissions.get(0).equals(Manifest.permission.CAMERA)) {
                ToastManager.showMsg(R.string.no_camera_permissions);
            } else if (deniedPermissions.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE) || deniedPermissions.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ToastManager.showMsg(R.string.no_storage_permissions);
            }
        }
    }

    public void onEventMainThread(final CropCallbackMessage message) {
        if (mContext.hashCode() == message.fromId) {
            UpDataRequest.getInstense().setConstantParams(new UpDataRequest.findConstantParams() {
                @NonNull
                @Override
                public String findUpdataUrl() {
                    return UrlRepository.getInstance().getServer() + "/common/uploadHeadImg.do?";
                }

                @Override
                public int findFileNumber() {
                    return 1;
                }

                @Nullable
                @Override
                public Map<String, String> findParams() {
                    return null;
                }
            }).setImgPath(new UpDataRequest.findImgPath() {
                @NonNull
                @Override
                public String getImgPath(int position) {
                    return message.path;
                }
            }).setProgressListener(null).setListener(new UpDataRequest.onUpDatalistener() {
                @Override
                public void onUpDataStart(int position, Object tag) {
                    rootView.showLoadingView();
                }

                @Override
                public void onUpDataSuccess(int position, Object tag, String jsonString) {
                    rootView.hiddenLoadingView();
                    UserUpdataImageResponse response = null;
                    try {
                        response = RequestBase.gson.fromJson(jsonString, UserUpdataImageResponse.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null) {
                        if (response.getStatus().getCode() == 0) {
                            String path = response.getData().get(0).getHead();
                            LoginInfo.saveHeadIcon(path);
                            setHeadImg(message.path);
                            ToastManager.showMsg(R.string.updata_headimg_success);
                        } else {
                            ToastManager.showMsg(R.string.updata_headimg_fails);
                        }
                    }
                }

                @Override
                public void onUpDataFailed(int position, Object tag, String failMsg) {
                    rootView.hiddenLoadingView();
                    ToastManager.showMsg(R.string.updata_headimg_fails);
                }

                @Override
                public void onError(String errorMsg) {
                    rootView.hiddenLoadingView();
                    ToastManager.showMsg(R.string.updata_headimg_fails);
                }
            });
        }
    }
}
