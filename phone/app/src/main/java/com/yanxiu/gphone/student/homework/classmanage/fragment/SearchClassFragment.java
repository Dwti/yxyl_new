package com.yanxiu.gphone.student.homework.classmanage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.CharacterSeparatedEditLayout;
import com.yanxiu.gphone.student.customviews.WavesLayout;
import com.yanxiu.gphone.student.homepage.MainActivity;
import com.yanxiu.gphone.student.homework.classmanage.activity.HowToJoinClassActivity;
import com.yanxiu.gphone.student.homework.classmanage.activity.JoinClassActivity;
import com.yanxiu.gphone.student.homework.response.ClassBean;
import com.yanxiu.gphone.student.homework.request.SearchClassRequest;
import com.yanxiu.gphone.student.homework.response.SearchClassResponse;
import com.yanxiu.gphone.student.base.EXueELianBaseCallback;
import com.yanxiu.gphone.student.util.ToastManager;

/**
 * Created by sp on 17-5-15.
 */

public class SearchClassFragment extends Fragment {

    private static final int REQUEST_JOIN_CLASS = 0x01;

    private OnJoinClassCompleteListener mOnJoinClassCompleteListener;

    private Button mBtnNext;

    private WavesLayout mWavesLayout;

    private View mRootView, mHowToJoinClass;

    private int mBottom;

    private boolean mKeyBoardVisible = false;

    private CharacterSeparatedEditLayout mInputNumWidget;

    public static SearchClassFragment getInstance() {
        return new SearchClassFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_search_class, container, false);
        initView();
        initListener();
        return mRootView;
    }

    private void initView(){
        mInputNumWidget = (CharacterSeparatedEditLayout) mRootView.findViewById(R.id.input_number_layout);
        TextView title = (TextView) mRootView.findViewById(R.id.tv_title);
        title.setText(R.string.title_homework);
        mBtnNext = (Button) mRootView.findViewById(R.id.btn_next);
        mWavesLayout = (WavesLayout) mRootView.findViewById(R.id.wavesLayout);
        mHowToJoinClass = mRootView.findViewById(R.id.how_to_join_class);

        mWavesLayout.setCanShowWave(false);
        mBtnNext.setEnabled(false);
    }

    private void initListener(){
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInputNumWidget.getText().length() < 8){
                    ToastManager.showMsg(getString(R.string.input_correct_class_number));
                }else if(mInputNumWidget.getText().length() == 8){
                    searchClass(mInputNumWidget.getText());
                }
            }
        });
        mHowToJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HowToJoinClassActivity.class);
                startActivity(intent);
            }
        });
        mInputNumWidget.setOnTextChangedListener(new CharacterSeparatedEditLayout.OnTextChangedListener() {
            @Override
            public void onTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    mWavesLayout.setCanShowWave(true);
                    mBtnNext.setEnabled(true);
                } else {
                    mWavesLayout.setCanShowWave(false);
                    mBtnNext.setEnabled(false);
                }
            }
        });

        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mBottom = mRootView.getBottom();
            }
        });

        getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    //根据键盘的弹出收起，去隐藏显示下面的导航栏
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if(mRootView.getBottom() < mBottom && !mKeyBoardVisible){
                ((MainActivity)getActivity()).setBottomNaviBarsVisibility(View.GONE);
                mRootView.requestLayout();
                mKeyBoardVisible = true;
            }else if(mRootView.getBottom() >= mBottom && mKeyBoardVisible){
                ((MainActivity)getActivity()).setBottomNaviBarsVisibility(View.VISIBLE);
                mKeyBoardVisible = false;
            }
        }
    };

    private void searchClass(String id) {
        SearchClassRequest request = new SearchClassRequest();
        request.setClassId(id);
        request.startRequest(SearchClassResponse.class, mSearchClassCallback);
    }

    public void setOnJoinClassCompleteListener(OnJoinClassCompleteListener listener) {
        mOnJoinClassCompleteListener = listener;
    }

    HttpCallback<SearchClassResponse> mSearchClassCallback = new EXueELianBaseCallback<SearchClassResponse>() {
        @Override
        public void onResponse(RequestBase request, SearchClassResponse ret) {
            if (ret.getStatus().getCode() == 0 && (ret.getData().get(0).getStatus() == 0 || ret.getData().get(0).getStatus() == 1)) {
                ClassBean bean = ret.getData().get(0);
                Intent intent = new Intent(getActivity(), JoinClassActivity.class);
                intent.putExtra(JoinClassActivity.EXTRA_CLASS_INFO, bean);
                startActivityForResult(intent, REQUEST_JOIN_CLASS);
            } else if(ret.getStatus().getCode() == 0 && ret.getData().get(0).getStatus() == 2){
                ToastManager.showMsg(getString(R.string.class_not_allowed_join));
            }else {
                ToastManager.showMsg(ret.getStatus().getDesc());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastManager.showMsg(error.getLocalizedMessage());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_JOIN_CLASS:
                if (resultCode == getActivity().RESULT_OK) {
                    if (mOnJoinClassCompleteListener != null) {
                        mOnJoinClassCompleteListener.onJoinClassComplete();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        super.onDestroy();
    }

    public interface OnJoinClassCompleteListener {
        void onJoinClassComplete();
    }
}
