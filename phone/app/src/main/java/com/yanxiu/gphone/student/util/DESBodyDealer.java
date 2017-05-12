package com.yanxiu.gphone.student.util;

import com.test.yanxiu.network.ResponseBodyDealer;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class DESBodyDealer implements ResponseBodyDealer {
    @Override
    public String dealWithBody(String body) {
        return SysEncryptUtil.decryptDES(body,"DES");
    }
}
