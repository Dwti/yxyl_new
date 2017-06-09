package com.yanxiu.gphone.student.util;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;

/**
 * Created by 戴延枫 on 2017/5/25.
 */

public class StringUtil {
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
}
