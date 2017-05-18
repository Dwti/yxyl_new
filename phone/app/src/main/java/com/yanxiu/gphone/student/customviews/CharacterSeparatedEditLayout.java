package com.yanxiu.gphone.student.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by sp on 17-5-15.
 */

public class CharacterSeparatedEditLayout extends FrameLayout{
    private GridView mGridView;
    private EditText mEditText;
    private static final int ITEM_COUNT = 8;
    private Context mContext;
    private GridAdapter mAdapter;
    public CharacterSeparatedEditLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CharacterSeparatedEditLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CharacterSeparatedEditLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context){
        mContext = context;
        View root = inflate(getContext(), R.layout.characterseparated_view,this);
        mGridView = (GridView) root.findViewById(R.id.gridView);
        mEditText = (EditText) root.findViewById(R.id.et_num);

        mAdapter = new GridAdapter(context,"");
        mGridView.setAdapter(mAdapter);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.replaceData(s.toString());
            }
        });

        mEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setSelection(mEditText.getText().toString().length());
            }
        });

        mEditText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        postDelayed(new TimerTask() {
            @Override
            public void run() {
                imm.showSoftInput(mEditText,0);
            }
        }, 100);
    }

    public String getText(){
        return mEditText.getText().toString();
    }
    private static class GridAdapter extends BaseAdapter{

        private Context context;
        private String str;

        public GridAdapter(Context context, String str) {
            this.context = context;
            this.str = str;
        }

        @Override
        public int getCount() {
            return ITEM_COUNT;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void replaceData(String data){
            if(data != null){
                str = data;
                notifyDataSetChanged();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.character_item,parent,false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tv_text);
            View separateLine = convertView.findViewById(R.id.separate_line);
            if(position == getCount() -1){
                separateLine.setVisibility(INVISIBLE);
            }
            if(position < str.length()){
                textView.setText(String.valueOf(str.charAt(position)));
            }else {
                textView.setText("");
            }
            return convertView;
        }
    }
}
