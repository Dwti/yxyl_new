package com.yanxiu.gphone.student.questions.spoken;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cangHaiXiao.
 * Time : 2016/12/22 16:48.
 * Function :
 */

public class CatchUtils {

    private static CatchUtils utils;
    private Map<String,WeakReference<Bitmap>> map=new HashMap<>();


    public static CatchUtils getInstence(){
        if (utils==null){
            utils=new CatchUtils();
        }
        return utils;
    }

    public void put(String key, Bitmap bitmap){
        if (map==null){
            map=new HashMap<>();
        }
        map.put(key,new WeakReference<>(bitmap));
    }

    public Bitmap get(String key){
        if (map!=null){
            WeakReference<Bitmap> weak=map.get(key);
            if (weak!=null){
                return weak.get();
            }
        }
        return null;
    }

}
