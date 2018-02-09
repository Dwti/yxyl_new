package com.yanxiu.gphone.student.questions.operation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.OnPermissionCallback;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.answerbase.AnswerSimpleExerciseBaseFragment;
import com.yanxiu.gphone.student.questions.answerframe.ui.fragment.base.ExerciseBaseFragment;
import com.yanxiu.gphone.student.util.FileUtil;
import com.yanxiu.gphone.student.util.HtmlImageGetterNew;
import com.yanxiu.gphone.student.util.StemUtil;
import com.yanxiu.gphone.student.util.StringUtil;
import com.yanxiu.gphone.student.util.ToastManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by sunpeng on 2017/12/25.
 */

public class OperationFragment extends AnswerSimpleExerciseBaseFragment {
    private OperationQuestion mQuestion;
    private View mRootView;
    private TextView mStem;
    private List<String> mImgUrls;
    private GridView mGridView;
    private OperationAdapter mAdapter;
    List<OperationBean> mOperationBeanList = new ArrayList<>();
    @Override
    public void setData(BaseQuestion node) {
        super.setData(node);
        mQuestion = (OperationQuestion) node;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setData((OperationQuestion) savedInstanceState.getSerializable(KEY_NODE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ExerciseBaseFragment.KEY_NODE, mQuestion);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(OperationFragment.this);
        mRootView  = inflater.inflate(R.layout.fragment_operation,container,false);
        initView();
        initData();
        setQaNumber(mRootView);
        setQaName(mRootView);
        initComplexStem(mRootView,mQuestion);
        return mRootView;
    }

    private void initView() {
        mStem = (TextView) mRootView.findViewById(R.id.tv_stem);
        mGridView = (GridView) mRootView.findViewById(R.id.gridView);
    }

    private void initData() {
        mStem.setText(Html.fromHtml(StemUtil.initOperationStem(mQuestion.getStem()),new HtmlImageGetterNew(mStem),null));
        mImgUrls = mQuestion.getOperateImgUrls();

        if(mImgUrls == null || mImgUrls.isEmpty()){
            OperationBean bean = new OperationBean();
            String fileName;
            fileName = SaveAnswerDBHelper.makeId(mQuestion);
            String filePath = FileUtil.getSavePicturePath(fileName);
            bean.setStoredFilePath(filePath);
            mOperationBeanList.add(bean);
        }else {
            for(String url: mImgUrls){
                OperationBean bean = new OperationBean();
                bean.setImageUrl(url);
                String fileName;
                if(TextUtils.isEmpty(url)){
                    fileName = SaveAnswerDBHelper.makeId(mQuestion);
                }else {
                    fileName = SaveAnswerDBHelper.makeId(mQuestion) + StringUtil.getPictureName(url);
                }
                String filePath = FileUtil.getSavePicturePath(fileName);
                bean.setStoredFilePath(filePath);
                mOperationBeanList.add(bean);
            }
        }
        setupAnswerList(mOperationBeanList);
        mAdapter = new OperationAdapter(mOperationBeanList,mOnStartAnswerClickListener);
        if(mOperationBeanList.size() > 1){
            mGridView.setNumColumns(2);
            mGridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
        }
        mGridView.setAdapter(mAdapter);
    }

    private OperationAdapter.OnStartAnswerClickListener mOnStartAnswerClickListener = new OperationAdapter.OnStartAnswerClickListener() {
        @Override
        public void onStartAnswerClick(final String storedFilePath, final String imgUrl) {
            YanxiuBaseActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                @Override
                public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                    PaletteActivity.invoke(OperationFragment.this.getActivity(),storedFilePath,imgUrl,OperationFragment.this.hashCode());
                }

                @Override
                public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                    ToastManager.showMsg(R.string.no_storage_permissions);
                }
            });
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(OperationFragment.this);
    }

    public void onEventMainThread(PictureModifiedMessage message){
        if(message != null && message.fromId == this.hashCode()){
            setupAnswerList(mOperationBeanList);
            if(!mQuestion.getAnswerList().isEmpty() && mQuestion.getAnswerList().size() == mOperationBeanList.size()){
                mQuestion.setHasAnswered(true);
            }else {
                mQuestion.setHasAnswered(false);
            }
            saveAnswer(mQuestion);
            updateProgress();
            mAdapter = new OperationAdapter(mOperationBeanList,mOnStartAnswerClickListener);
            mGridView.setAdapter(mAdapter);
        }
    }

    private void setupAnswerList(List<OperationBean> list){
        mQuestion.getAnswerList().clear();
        for(OperationBean bean : list){
            String picPath = bean.getStoredFilePath() + PaletteActivity.SUFFIX;
            if(OperationUtils.hasStoredBitmap(picPath)){
                mQuestion.getAnswerList().add(picPath);
            }else {
                mQuestion.getAnswerList().add(bean.getImageUrl());
            }
        }
    }
}
