package com.yanxiu.gphone.student.homework.classmanage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.customviews.CharacterSeparatedEditLayout;
import com.yanxiu.gphone.student.homework.data.ClassBean;
import com.yanxiu.gphone.student.homework.data.SearchClassRequest;
import com.yanxiu.gphone.student.homework.data.SearchClassResponse;
import com.yanxiu.gphone.student.base.ExerciseBaseCallback;

/**
 * Created by sp on 17-5-15.
 */

public class SearchClassFragment extends Fragment{
    private static final int REQUEST_JOIN_CLASS = 0x01;
    private OnJoinClassCompleteListener mOnJoinClassCompleteListener;
    public static SearchClassFragment getInstance(){
        return new SearchClassFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_class,container,false);
        final CharacterSeparatedEditLayout inputNum = (CharacterSeparatedEditLayout) root.findViewById(R.id.input_number_layout);
        TextView title = (TextView) root.findViewById(R.id.tv_title);
        title.setText(R.string.title_homework);
        Button next = (Button) root.findViewById(R.id.btn_next);
        View howToJoinClass = root.findViewById(R.id.how_to_join_class);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            searchClass(inputNum.getText());
            }
        });
        howToJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HowToJoinClassActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void searchClass(String id){
        SearchClassRequest request = new SearchClassRequest();
        request.setClassId(id);
        request.startRequest(SearchClassResponse.class,mSearchClassCallback);
    }

    public void setOnJoinClassCompleteListener(OnJoinClassCompleteListener listener){
        mOnJoinClassCompleteListener = listener;
    }

    HttpCallback<SearchClassResponse> mSearchClassCallback = new ExerciseBaseCallback<SearchClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, SearchClassResponse ret) {
            super.onSuccess(request,ret);
            if(ret.getStatus().getCode() == 0 ){
                ClassBean bean = ret.getData().get(0);
                Intent intent = new Intent(getActivity(),JoinClassActivity.class);
                intent.putExtra(JoinClassActivity.EXTRA_CLASS_INFO,bean);
                startActivityForResult(intent,REQUEST_JOIN_CLASS);
            }else {
                Toast.makeText(getActivity(),ret.getStatus().getDesc(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(getActivity(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_JOIN_CLASS:
                if(resultCode == getActivity().RESULT_OK ){
                    if(mOnJoinClassCompleteListener != null){
                        mOnJoinClassCompleteListener.onJoinClassComplete();
                    }
                }
                break;
            default:
                break;
        }
    }


    public interface OnJoinClassCompleteListener {
        void onJoinClassComplete();
    }
}
