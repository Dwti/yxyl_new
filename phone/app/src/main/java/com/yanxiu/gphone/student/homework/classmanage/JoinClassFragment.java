package com.yanxiu.gphone.student.homework.classmanage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.R;

/**
 * Created by sp on 17-5-15.
 */

public class JoinClassFragment extends Fragment{

    public static JoinClassFragment getInstance(){
        return new JoinClassFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_join_class,container,false);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
