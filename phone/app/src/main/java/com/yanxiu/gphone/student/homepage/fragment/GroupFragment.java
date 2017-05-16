package com.yanxiu.gphone.student.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.homework.classmanage.JoinClassFragment;

/**
 * 首页 作业列表Fragment
 */
public class GroupFragment extends Fragment {
//    HomeWorkFragment
    private final static String TAG = GroupFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);
        Button mJoinClass = (Button) view.findViewById(R.id.btn_join_class);
        mJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(getId(),JoinClassFragment.getInstance()).commit();
            }
        });
        return view;
    }
}
