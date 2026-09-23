package com.huabo.system.service;



import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huabo.system.entity.TblFinanceData;

/**
 * Created by mangel on 2015/3/14.
 */

public interface TblFinanceDataService {


    Map<String, Object>  del(String orderId);

    Map<String, Object> findByCompanyId(Integer pageNumber, String token, String staffId, Integer pageSize);

    TblFinanceData get(String selectid);

    String selectDateByCompanyid(HttpServletRequest request,String token, String staffId, TblFinanceData tlf,String parseStart,String pendDate,String fid) throws Exception;

    String findModelName(Integer type, BigDecimal orgid);
}
