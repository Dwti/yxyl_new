package com.yanxiu.gphone.student.util;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;

import com.yanxiu.gphone.student.YanxiuApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/6/12.
 */

public class StemUtil {
    private static final String MARK_GREEN_START = "<font color='#89e00d'>";
    private static final String MARK_ORANGE_START = "<font color='#ff7a05'>";
    private static final String MARK_END = "</font>";


    /**
     * 初始化填空题题干
     *
     * @param stem          题干内容
     * @param filledAnswers 空的答案
     * @return 新的题干
     */
    public static String initFillBlankStem(@NonNull String stem, @NonNull List<String> filledAnswers) {
        //<fill>标签 表示空中有答案，需要展示  <empty>标签表示空中没有内容，需要显示为空白
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(ScreenUtils.dpToPx(YanxiuApplication.getContext(), 17));
        float defaultWidth = StringUtil.computeStringWidth("oooooo", textPaint);
        float placeHolderWidth = StringUtil.computeStringWidth("\u00A0", textPaint);

        int i = 0;
        while (stem.contains("(_)")) {
            boolean isFrontChar = isFrontChar(stem);
            stem = replaceFirstChar(stem, MARK_GREEN_START);
            if (i > filledAnswers.size() - 1 || TextUtils.isEmpty(filledAnswers.get(i))) {
                if (i > filledAnswers.size() - 1)
                    filledAnswers.add("");
                stem = stem.replaceFirst("\\(_\\)", "<empty>oooooo</empty>");
            } else {
                String replace;
                float answerWidth = StringUtil.computeStringWidth(filledAnswers.get(i), textPaint);
                if (answerWidth + placeHolderWidth < defaultWidth) {
                    int holderCount = (int) Math.floor((defaultWidth - answerWidth) / placeHolderWidth);
                    PlaceHolderGravity holderGravity = isFrontChar ? PlaceHolderGravity.LEFT : PlaceHolderGravity.CENTER;
                    replace = generateSpaces(filledAnswers.get(i), holderCount, holderGravity);
                } else {
                    replace = filledAnswers.get(i);
                }
//                stem = stem.replaceFirst("\\(_\\)", "<fill>"+filledAnswers.get(i)+"</fill>");
                stem = stem.replaceFirst("\\(_\\)", "<fill>" + replace + "</fill>");
            }
            i++;
        }
        //如果自定义标签标签为第一个字符时，taghandler解析的时候会有一个bug，导致第一个解析会跳过，然后会引起后面的图片显示也有问题
        if (stem.startsWith("<empty>") || stem.startsWith("<fill>")) {
            stem = "&zwj" + stem;
        }

        StringBuilder sb = new StringBuilder(stem);

        ArrayList<String> tags = new ArrayList<>();
        tags.add("empty");
        tags.add("fill");
        //就是两个空连起来的情况，需要中间加一个空格
        stem = addSpace(stem, tags);

        return stem;
    }

    /**
     * 初始化填空题解析题干
     *
     * @param stem           题干内容
     * @param filledAnswers  所填的答案
     * @param correctAnswers 正确答案
     * @return 新的题干
     */
    public static String initAnalysisFillBlankStem(@NonNull String stem, @NonNull List<String> filledAnswers, @NonNull List<String> correctAnswers) {
        //<fill>标签 表示空中有答案，需要展示  <empty>标签表示空中没有内容，需要显示为空白
        int i = 0;
        while (stem.contains("(_)")) {
            if (i > filledAnswers.size() - 1 || TextUtils.isEmpty(filledAnswers.get(i))) {
                if (i > filledAnswers.size() - 1)
                    filledAnswers.add("");
                stem = replaceFirstChar(stem, MARK_ORANGE_START);
                stem = stem.replaceFirst("\\(_\\)", "<empty>oooooo</empty>");
            } else if (!correctAnswers.get(i).equals(filledAnswers.get(i))) {
                stem = replaceFirstChar(stem, MARK_ORANGE_START);
                stem = stem.replaceFirst("\\(_\\)", "<wrong>" + filledAnswers.get(i) + "</wrong>");
            } else {
                stem = replaceFirstChar(stem, MARK_GREEN_START);
                stem = stem.replaceFirst("\\(_\\)", "<right>" + filledAnswers.get(i) + "</right>");
            }
            i++;
        }
        //如果自定义标签标签为第一个字符时，taghandler解析的时候会有一个bug，导致第一个解析会跳过，然后会引起后面的图片显示也有问题
        if (stem.startsWith("<empty>") || stem.startsWith("<wrong>") || stem.startsWith("<right>")) {
            stem = "&zwj" + stem;
        }

        StringBuilder sb = new StringBuilder(stem);

        ArrayList<String> tags = new ArrayList<>();
        tags.add("empty");
        tags.add("right");
        tags.add("wrong");
        stem = addSpace(stem, tags);

        return stem;
    }


    public static String generateSpaces(String source, int count, PlaceHolderGravity holderGravity) {
        String space = "\u00A0";
        if (holderGravity == PlaceHolderGravity.LEFT) {
            for (int i = 0; i < count; i ++) {
                source += space;
            }
        } else {
            for (int i = 0; i < count; i ++) {
                if(i % 2 == 0){
                    source+=space;
                }else {
                    source = space + source;
                }
            }
        }

        return source;
    }

    /**
     * 两个连起来的空之间添加空格
     *
     * @param stem 题干
     * @param tags 解析的标签
     * @return
     */

    public static String addSpace(String stem, List<String> tags) {
        StringBuilder sb = new StringBuilder(stem);
        for (String e : tags) {
            String startTag = "</" + e + ">";
            String endTag;
            String str;
            for (String t : tags) {
                endTag = "<" + t + ">";
                str = startTag + endTag;
                while (stem.contains(str)) {
                    int index = sb.indexOf(str);
                    sb = sb.insert(index + startTag.length(), "&nbsp");
                    stem = sb.toString();
                }
            }
        }

        return stem;
    }

    /**
     * 如果空前面有首字母，加上html变色处理
     *
     * @param source 源题干
     * @return 新的题干
     */
    public static String replaceFirstChar(String source, String markStart) {
        StringBuilder sb = new StringBuilder(source);
        int index = source.indexOf("(_)");
        if (index > 0) {
            if (source.length() > 4) {
                char c1 = sb.charAt(index - 1);
                char c2 = sb.charAt(index - 2);
                if (((c1 >= 'a' && c1 <= 'z') || (c1 >= 'A' && c1 <= 'Z')) && !((c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z'))) {
                    sb.insert(index - 1, markStart);
                    sb.insert(index + markStart.length(), MARK_END);
                }
            } else if (source.length() == 4) {
                sb.insert(0, markStart);
                sb.insert(markStart.length(), MARK_END);
            }
            return sb.toString();
        } else {
            return source;
        }
    }

    public static boolean isFrontChar(String source) {
        boolean isFrontChar = false;
        StringBuilder sb = new StringBuilder(source);
        int index = source.indexOf("(_)");
        if (index > 0) {
            if (source.length() > 4) {
                char c1 = sb.charAt(index - 1);
                char c2 = sb.charAt(index - 2);
                if (((c1 >= 'a' && c1 <= 'z') || (c1 >= 'A' && c1 <= 'Z')) && !((c2 >= 'a' && c2 <= 'z') || (c2 >= 'A' && c2 <= 'Z'))) {
                    isFrontChar = true;
                }
            } else if (source.length() == 4) {
                isFrontChar = true;
            }
        }
        return isFrontChar;
    }

    public static String initClozeStem(String stem) {
        if (stem == null)
            return null;
        String str = stem.replaceAll("\\(_\\)", "<empty>empty</empty>");
        if (str.startsWith("<empty>"))
            str = "&zwj;" + str;                   //如果<Blank>标签为第一个字符时，taghandler解析的时候会有一个bug，导致第一个解析会跳过，然后会引起后面的图片显示也有问题
        StringBuilder sb = new StringBuilder(str);
        while (str.contains("</empty><empty>")) {   //就是两个空连起来的情况，需要中间加一个空格
            int index = sb.indexOf("</empty><empty>");
            sb = sb.insert(index + 8, "&nbsp");
            str = sb.toString();
        }
        return str;
    }

    public enum PlaceHolderGravity {
        LEFT, CENTER
    }
}
