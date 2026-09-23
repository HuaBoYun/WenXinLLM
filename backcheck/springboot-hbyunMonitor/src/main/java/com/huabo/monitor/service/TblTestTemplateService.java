package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblTestTemplate;

import java.math.BigDecimal;


public interface TblTestTemplateService {

    JsonBean getTestTemp(String token, Integer pageNumber, TblTestTemplate testTemplate);

    JsonBean add(TblTestTemplate template);

    JsonBean updateById(BigDecimal testtemid);

    JsonBean deleteById(String templeNumber);

    JsonBean selectList(String token, Integer pageNumber, Integer pageSize, Integer templeNumber, String templename);

    JsonBean save(TblTestTemplate tblTestTemplate);
}
