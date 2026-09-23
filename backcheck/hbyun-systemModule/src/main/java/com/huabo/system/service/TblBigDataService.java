package com.huabo.system.service;

import com.huabo.system.entity.TblJobGrade;

import java.math.BigDecimal;
import java.util.List;

public interface TblBigDataService {
    void syncBigData(String result) throws Exception;

    void syncBigDataTCY(String result) throws Exception;

}
