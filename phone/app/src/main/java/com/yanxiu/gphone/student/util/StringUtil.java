package com.yanxiu.gphone.student.util;

import android.text.TextPaint;
import android.text.TextUtils;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 戴延枫 on 2017/5/25.
 */

public class StringUtil {

    private static final char SBC_CHAR_START = 65281;
    private static final char SBC_CHAR_END = 65374;
    private static final int CONVERT_STEP = 65248;
    private static final char SBC_SPACE = 12288;
    private static final char DBC_SPACE = ' ';
    /**
     * 获取temple对应的汉字名称
     * @param template
     * @return
     */
    public static String getTemplateName(String template) {
        String templateString;
        switch (template) {
            case QuestionTemplate.SINGLE_CHOICE:
                templateString = YanxiuApplication.getInstance().getString(R.string.SINGLE_CHOICE);
                break;
            case QuestionTemplate.MULTI_CHOICES:
                templateString = YanxiuApplication.getInstance().getString(R.string.MULTI_CHOICES);
                break;
            case QuestionTemplate.FILL:
                templateString = YanxiuApplication.getInstance().getString(R.string.FILL_BLANK);
                break;
            case QuestionTemplate.ALTER:
                templateString = YanxiuApplication.getInstance().getString(R.string.JUDGE);
                break;
            case QuestionTemplate.CONNECT:
                templateString = YanxiuApplication.getInstance().getString(R.string.CONNECT);
                break;
            case QuestionTemplate.CLASSIFY:
                templateString = YanxiuApplication.getInstance().getString(R.string.CLASSIFY);
                break;
            case QuestionTemplate.ANSWER:
                templateString = YanxiuApplication.getInstance().getString(R.string.ANSWER);
                break;
            case QuestionTemplate.READING:
                templateString = YanxiuApplication.getInstance().getString(R.string.READING);
                break;
            case QuestionTemplate.CLOZE:
                templateString = YanxiuApplication.getInstance().getString(R.string.CLOZE);
                break;
            case QuestionTemplate.LISTEN:
                templateString = YanxiuApplication.getInstance().getString(R.string.LISTEN);
                break;
            default:
                templateString = null;
                break;
        }
        return templateString;
    }

    public static boolean isEmpty(String str) {
        if (null != str) {
            if (str.length() > 4) {
                return false;
            }
        }
        return null == str || "".equals(str) || "NULL"
                .equals(str.toUpperCase());
    }


    public static float computeStringWidth(String str, TextPaint textPaint){
        return textPaint.measureText(str);
    }

    public static String full2half(String src) {
        if (src == null) {
            return src;
        }
        StringBuilder buf = new StringBuilder(src.length());
        char[] ca = src.toCharArray();
        for (int i = 0; i < src.length(); i++) {
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) {
                buf.append((char) (ca[i] - CONVERT_STEP));
            } else if (ca[i] == SBC_SPACE) {
                buf.append(DBC_SPACE);
            } else {
                buf.append(ca[i]);
            }
        }
        return buf.toString();
    }

    public static List<String> full2half(List<String> srcList){
        List<String> list = new ArrayList<>();
        if(srcList == null)
            return srcList;
        for(String str : srcList){
            list.add(full2half(str));
        }
        return list;
    }

    public static char getChoiceByIndex(int index){
        return (char)(index + 65);
    }

    public static String getPictureName(String url){
        String picName = null;
        if(TextUtils.isEmpty(url)){
            throw new IllegalArgumentException("url can not be empty");
        }
        int index = url.lastIndexOf("/");
        if(index != -1){
            picName = url.substring(index + 1,url.length());
        }
        int index1 = picName.lastIndexOf(".");
        if(index != -1){
            picName = picName.substring(0,index1);
        }
        return picName;
    }
}
