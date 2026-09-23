package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsPlanfiling;

import java.math.BigDecimal;

public interface TblYqnsPlanfilingService extends IService<TblYqnsPlanfiling> {
    JsonBean getPlanFilingList(String token, Integer pageNumber, Integer pageSize, TblYqnsPlanfiling vo) throws Exception;

    JsonBean saveOrUpdate(String token, TblYqnsPlanfiling vo) throws Exception;

    JsonBean delete(String token, BigDecimal id) throws Exception;

    JsonBean detail(String token, TblYqnsPlanfiling vo) throws Exception;
}
