package com.yanxiu.gphone.student.homework.classmanage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;

/**
 * Created by sp on 17-5-18.
 */

public class HowToJoinClassActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_join_class);
        ImageView back = (ImageView) findViewById(R.id.iv_left);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
