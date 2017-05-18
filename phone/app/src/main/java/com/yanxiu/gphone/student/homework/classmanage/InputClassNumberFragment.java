package com.yanxiu.gphone.student.homework.classmanage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yanxiu.gphone.student.R;

/**
 * Created by sp on 17-5-15.
 */

public class InputClassNumberFragment extends Fragment{

    public static InputClassNumberFragment getInstance(){
        return new InputClassNumberFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_join_class,container,false);
        Button btn_next = (Button) root.findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),JoinClassActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
