package com.huabo.compliance.util;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

import java.util.Objects;

public interface ConstClass {

    public static final int DEFAULT_SIZE = 20;


    static JsonBean tokenFailure(){
       return new JsonBean(200,"没有token,或者token错误.","");
    }

}
