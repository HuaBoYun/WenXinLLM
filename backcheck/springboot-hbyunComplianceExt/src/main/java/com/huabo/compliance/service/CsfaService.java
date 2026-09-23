package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTestplanVo;
import com.huabo.compliance.entity.TblTesttempleVo;
import com.huabo.compliance.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CsfaService {





    boolean isSJByOrgId(String s);

    IPage<TblTestplan> findAll(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,Integer pageSize);

    IPage<TblTestplan> findAllnoSj(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String toString,Integer pageSize);

    TblTestplanVo queryOneTestPlanVo(BigDecimal selectProjectid);

    IPage<TblTesttempleVo> getTestTemp(BigDecimal orgid, Integer pageNumber, String templeNumber, String templename);

    void delTesttasksByPanId(BigDecimal testplanid);

     void deleteBytaskId(BigDecimal testtaskid);

    List<Tree> getTreeC(BigDecimal testtemid);

    IPage<Map<String,Object>> fingByTree(BigDecimal node, BigDecimal templId, BigDecimal planid, Integer pageNumber);

    void updateTesttsak(String task, String planid, String userid);

    Integer findByPlan(BigDecimal selectProjectid);

    Integer findByPlanwStra(BigDecimal selectProjectid);

    void deleteTestPlan(BigDecimal selectProjectid);
}
