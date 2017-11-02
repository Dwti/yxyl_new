package com.yanxiu.gphone.student.questions.spoken;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/11/2 10:57.
 * Function :
 */
public class SpokenPermissionDialog {

    private Dialog mDialog;

    public static SpokenPermissionDialog creat(Context context){
        return new SpokenPermissionDialog(context);
    }

    private SpokenPermissionDialog(Context context){
        mDialog=new Dialog(context, R.style.AnswerCarDialog);
        View view= LayoutInflater.from(context).inflate(R.layout.spoken_permission_dialog,null,false);
        view.findViewById(R.id.button_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view);
    }

    public void show(){
        if (mDialog!=null&&!mDialog.isShowing()) {
            mDialog.show();
        }
    }

}
