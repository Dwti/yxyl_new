package com.yanxiu.gphone.student.util;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 11:25.
 * Function :
 */
@SuppressWarnings("unused")
public class EditTextManger implements TextWatcher {

    public interface onTextLengthChangedListener {
        void onChanged(View view, String value, boolean isEmpty);
    }

    private static final String INPUT_NUMBER = "0123456789";
    private static final String INPUT_LETTER_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String INPUT_LETTER_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String INPUT_SYMBOL="`~!@#$%^&*()_+-=[]{}|;':";

    private onTextLengthChangedListener mTextLengthChangedListener;
    private View view;

    public static EditTextManger getManager(View view) {
        return new EditTextManger(view);
    }

    private EditTextManger(View view) {
        this.view=view;
    }

    public void setTextChangedListener(onTextLengthChangedListener listener) {
        this.mTextLengthChangedListener=listener;
        if (view!=null){
            if (view instanceof EditText){
                ((EditText)view).addTextChangedListener(EditTextManger.this);
            }else if (view instanceof TextView){
                ((TextView)view).addTextChangedListener(EditTextManger.this);
            }else {
                setTypeError();
            }
        }
    }

    private void setTypeError(){
        throw new IllegalAccessError("The view type error,The view must be TextView or EditText");
    }

    private boolean checkViewIsEditText(){
        if (view!=null){
            if (view instanceof EditText){
                return true;
            }
        }
        setTypeError();
        return false;
    }

    public EditTextManger setInputOnlyNumber() {
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_NUMBER);
        }
        return this;
    }

    public EditTextManger setInputOnlyLetterLowercase() {
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_LETTER_LOWERCASE);
        }
        return this;
    }

    public EditTextManger setInputOnlyLetterUppercase() {
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_LETTER_UPPERCASE);
        }
        return this;
    }

    public EditTextManger setInputNumberAndLetterLowercase() {
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_NUMBER + INPUT_LETTER_LOWERCASE);
        }
        return this;
    }

    public EditTextManger setInputNumberAndLetterUppercase() {
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_NUMBER + INPUT_LETTER_UPPERCASE);
        }
        return this;
    }

    public EditTextManger setInputNumberAndLetter() {
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_NUMBER + INPUT_LETTER_LOWERCASE + INPUT_LETTER_UPPERCASE);
        }
        return this;
    }

    public EditTextManger setInputAllNotHanzi(){
        if (checkViewIsEditText()){
            setKeyType((EditText) view, INPUT_LETTER_LOWERCASE+INPUT_LETTER_UPPERCASE+INPUT_NUMBER+INPUT_SYMBOL);
        }
        return this;
    }

    private void setKeyType(EditText editText, final String chars) {
        final int inputType = editText.getInputType();
        editText.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return chars.toCharArray();
            }

            @Override
            public int getInputType() {
                return inputType == InputType.TYPE_NULL ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : inputType;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String value = s.toString();
        if (value.length() > 0) {
            setChanged(value, false);
        } else {
            setChanged(value, true);
        }
    }

    private void setChanged(String value, boolean isEmpty) {
        if (mTextLengthChangedListener!=null){
            if (view!=null){
                mTextLengthChangedListener.onChanged(view, value, isEmpty);
            }
        }
    }
}
