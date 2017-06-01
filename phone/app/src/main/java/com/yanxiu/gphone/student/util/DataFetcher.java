package com.yanxiu.gphone.student.util;

import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 戴延枫 on 2017/5/24.
 * 大数据存取工具类
 */

public class DataFetcher {
    public static final String TAG = DataFetcher.class.getSimpleName();
    private static DataFetcher INSTACNE;
//    private static class SingletonHolder {
//        private static final DataFetcher INSTACNE = new DataFetcher();
//    }

    public static final DataFetcher getInstance() {
        if (INSTACNE == null) {
            INSTACNE = new DataFetcher();
        }
        return INSTACNE;
    }

    private DataFetcher() {

    }

    private Map mMap = new HashMap();

    /**
     * 这个方法需要在调用时传入对象
     * Paper p2 = new Paper();
     * p2 = DataFetcher.getInstance().get("dyf",p2);
     *
     * @param key
     * @param <T> 目前只支持paper和list，其余数据使用intent传递
     * @return
     */
    public synchronized <T> T get(String key, T t) {
        Object value;
        try {
            value = mMap.get(key);
            if (value instanceof Paper && t instanceof Paper) {
                t = (T) value;
            } else if (value instanceof ArrayList && t instanceof ArrayList) {
                t = (T) value;
            } else {
                //目前只支持paper和list，其余数据使用intent传递
                t = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public synchronized <T> void save(String key, T t) {
        mMap.put(key, t);
    }

    /**
     * 获取paper对象
     *
     * @param key
     * @return
     */
    public synchronized Paper getPaper(String key) {
        Paper paper = null;
        try {
            Object value = mMap.get(key);
            if (value instanceof Paper) {
                paper = (Paper) mMap.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paper;
    }

    /**
     * 退出程序时调用
     */
    public void destory() {
        try {
            mMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap = null;
        INSTACNE = null;
    }

}
