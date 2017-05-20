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

/**
 * Created by sp on 17-5-15.
 */

public class SearchClassFragment extends Fragment{

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
        Button btn_next = (Button) root.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            searchClass(inputNum.getText());
            }
        });
        return root;
    }

    private void searchClass(String id){
        SearchClassRequest request = new SearchClassRequest();
        request.setClassId(id);
        request.startRequest(SearchClassResponse.class,mSearchClassCallback);
    }

    HttpCallback<SearchClassResponse> mSearchClassCallback = new HttpCallback<SearchClassResponse>() {
        @Override
        public void onSuccess(RequestBase request, SearchClassResponse ret) {
            if(ret.getStatus().getCode() == 0 ){
                ClassBean bean = ret.getData().get(0);
                Intent intent = new Intent(getActivity(),JoinClassActivity.class);
                intent.putExtra(JoinClassActivity.EXTRA_CLASS_INFO,bean);
                startActivity(intent);
            }else {
                Toast.makeText(getActivity(),ret.getStatus().getDesc(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            Toast.makeText(getActivity(),error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    };
}
