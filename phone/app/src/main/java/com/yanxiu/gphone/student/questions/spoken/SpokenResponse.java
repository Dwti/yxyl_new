package com.yanxiu.gphone.student.questions.spoken;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/16 16:38.
 * Function :
 */
public class SpokenResponse {

    /**音频地址，手动放置*/
    public String url;
    /**每行输入文本的评测结果*/
    public List<Lines> lines;
    /**结果格式版本及版本号*/
    public String version;

    public class Lines{
        /**开始时间，单位为秒*/
        public double begin;
        /**结束时间，单位为秒*/
        public double end;
        /**录入语音的流利度*/
        public double fluency;
        /**录入语音的完整度*/
        public double integrity;
        /**录入语音的标准度*/
        public double pronunciation;
        /**输入的标准文本*/
        public String sample;
        /**分值  0-100*/
        public double score;
        /**用户实际朗读的文本（语音识别结果）*/
        public String usertext;
        /**每个词的评测结果*/
        public List<Words> wordses;

        public class Words{
            /**开始时间，单位为秒*/
            public double begin;
            /**结束时间，单位为秒*/
            public double end;
            /**分值  0-100*/
            public double score;
            /**结果格式版本及版本号*/
            public String text;
            /**类型，共有6种类型，分别是：0 多词；1 漏词；2 正常词；3 错误词；4 静音；5 重复词；8 生词*/
            public int type;
            /**音量  0-100*/
            public double volume;
        }
    }
}
