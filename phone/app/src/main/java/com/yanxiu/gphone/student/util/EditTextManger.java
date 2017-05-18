package com.yanxiu.gphone.student.util;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.widget.EditText;


/**
 * Created by Canghaixiao.
 * Time : 2017/5/10 11:25.
 * Function :
 */
@SuppressWarnings("unused")
public class EditTextManger implements TextWatcher {

    public interface onTextLengthChangedListener {
        void onChanged(EditText view, String value, boolean isEmpty);
    }

    private static final String INPUT_NUMBER = "0123456789";
    private static final String INPUT_LETTER_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String INPUT_LETTER_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String INPUT_SYMBOL="`~!@#$%^&*()_+-=[]{}|;':";

    private onTextLengthChangedListener mTextLengthChangedListener;
    private EditText mEditText;

    public static EditTextManger getManager(EditText editText) {
        return new EditTextManger(editText);
    }

    private EditTextManger(EditText editText) {
        this.mEditText=editText;
    }

    public void setTextChangedListener(onTextLengthChangedListener listener) {
        this.mTextLengthChangedListener=listener;
        if (mEditText!=null){
           mEditText.addTextChangedListener(EditTextManger.this);
        }
    }

    public EditTextManger setInputOnlyNumber() {
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_NUMBER);
        }
        return this;
    }

    public EditTextManger setInputOnlyLetterLowercase() {
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_LETTER_LOWERCASE);
        }
        return this;
    }

    public EditTextManger setInputOnlyLetterUppercase() {
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_LETTER_UPPERCASE);
        }
        return this;
    }

    public EditTextManger setInputNumberAndLetterLowercase() {
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_NUMBER + INPUT_LETTER_LOWERCASE);
        }
        return this;
    }

    public EditTextManger setInputNumberAndLetterUppercase() {
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_NUMBER + INPUT_LETTER_UPPERCASE);
        }
        return this;
    }

    public EditTextManger setInputNumberAndLetter() {
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_NUMBER + INPUT_LETTER_LOWERCASE + INPUT_LETTER_UPPERCASE);
        }
        return this;
    }

    public EditTextManger setInputAllNotHanzi(){
        if (mEditText!=null){
            setKeyType(mEditText, INPUT_LETTER_LOWERCASE+INPUT_LETTER_UPPERCASE+INPUT_NUMBER+INPUT_SYMBOL);
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
            if (mEditText!=null){
                mTextLengthChangedListener.onChanged(mEditText, value, isEmpty);
            }
        }
    }
}
