package com.yanxiu.gphone.student.questions.subjective;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.common.Bean.PhotoDeleteBean;
import com.yanxiu.gphone.student.common.activity.CameraActivity;
import com.yanxiu.gphone.student.common.activity.CropImageActivity;
import com.yanxiu.gphone.student.common.activity.PhotoActivity;
import com.yanxiu.gphone.student.customviews.AlbumGridView;
import com.yanxiu.gphone.student.customviews.spantextview.SubjectClozeTextView;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.util.StemUtil;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/14 11:02.
 * Function :
 */
public class SubjectiveFragment extends AnswerSimpleExerciseBaseFragment implements AlbumGridView.onClickListener, AlbumGridView.onItemChangedListener, OnPermissionCallback {

    private SubjectiveQuestion mData;
    private SubjectClozeTextView mQuestionView;
    private AlbumGridView mAnswerView;

    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mData = (SubjectiveQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && mData == null) {
            setData((SubjectiveQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_NODE, mData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(SubjectiveFragment.this);
        View view = inflater.inflate(R.layout.fragment_subjective, container, false);
        setQaNumber(view);
        setQaName(view);
        initView(view);
        initComplexStem(view, mData);
        initData();
        listener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(SubjectiveFragment.this);
    }

    private void initView(View view) {
        mQuestionView = (SubjectClozeTextView) view.findViewById(R.id.tv_question);
        mAnswerView = (AlbumGridView) view.findViewById(R.id.ag_image);
    }

    private void initData() {
        String string= StemUtil.initClozeStem(mData.getStem());
        mQuestionView.setText(string);
        mAnswerView.setData(mData.answerList);
    }

    private void listener() {
        mAnswerView.addClickListener(this);
        mAnswerView.addItemChangedListener(this);
    }

    @Override
    public void onClick(int Type, int position) {
        switch (Type) {
            case AlbumGridView.TYPE_CAMERA:
                YanxiuBaseActivity.requestCameraPermission(SubjectiveFragment.this);
                break;
            case AlbumGridView.TYPE_IMAGE:
                PhotoActivity.LaunchActivity(getContext(), mData.answerList, position, SubjectiveFragment.this.hashCode(),PhotoActivity.DELETE_CAN);
                break;
        }
    }

    @Override
    public void onChanged(ArrayList<String> paths) {
        mData.answerList.clear();
        if (paths != null) {
            mData.answerList.addAll(paths);
        }
        if (mData.answerList.size() > 0) {
            mData.setIsAnswer(true);
        } else {
            mData.setIsAnswer(false);
        }
        saveAnswer(mData);
        updateProgress();
    }

    public void onEventMainThread(CropImageActivity.CropCallbackMessage message) {
        if (message != null && message.fromId == SubjectiveFragment.this.hashCode()) {
            if (message.path != null) {
                mAnswerView.addData(message.path);
            }
        }
    }

    public void onEventMainThread(PhotoDeleteBean deleteBean) {
        if (deleteBean != null && deleteBean.formId == SubjectiveFragment.this.hashCode()) {
            mAnswerView.remove(deleteBean.deleteId);
        }
    }

    /**
     * 获取权限
     *
     * @param deniedPermissions
     */
    @Override
    public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
        CameraActivity.LaunchActivity(getContext(), SubjectiveFragment.this.hashCode());
    }

    @Override
    public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
        ToastManager.showMsg(R.string.no_camera_permissions);
    }
}
