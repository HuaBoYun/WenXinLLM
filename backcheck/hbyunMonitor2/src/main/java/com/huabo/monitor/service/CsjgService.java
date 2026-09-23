package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CsjgService {


    IPage<Map<String, Object>> findAllCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal orgid,BigDecimal staffid,Integer authorityType);

    IPage<Map<String, Object>> findAllWCSjCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal staffid,Integer authorityType);
/////
    PageInfo<Map<String, Object>> findAllCSHZNew(TblTestplan plan, Integer pageNumber, BigDecimal orgid,BigDecimal staffid,Integer authorityType,TblStaffUtil user)throws Exception;

    PageInfo<Map<String, Object>> findAllWCSjCSHZNew(TblTestplan plan, Integer pageNumber, BigDecimal staffid,Integer authorityType,TblStaffUtil user)throws Exception;

    PageInfo<Map<String, Object>> findAll(TblTestplan plan, Integer pageNumber,TblStaffUtil user)throws Exception;

    
    List<Tree> getTreeLeftyx(String testtemid, String planid, String s, String userid);

    List<Tree> getTreeLeftwx(String toString, String planid, String s, String toString1);

    List<Tree> getTreeLeftbxy(String toString, String planid, String s, String toString1);

    PageInfo<Map<String, Object>> fingByTreeCSRW(String node, String templId, String planid, Integer pageNumber,Integer pageSize);

    PageInfo<Map<String, Object>> fingByTreeCSJGY(String node, String templId, String planid, Integer pageNumber,Integer pageSize);

    PageInfo<Map<String, Object>> fingByTreeCSJGW(String node, String templId, String planid, Integer pageNumber,Integer pageSize);

    PageInfo<Map<String, Object>> fingByTreeCSJGB(String node, String templId, String planid, Integer pageNumber,Integer pageSize);

    IPage<Map<String, Object>> findAllnoSjResult(BigDecimal planid, Integer pageNumber);
  //
    PageInfo<Map<String, Object>> findAllnoSjResultNew(BigDecimal planid, Integer pageNumber);

    
    List<Tree> getTreeCSHZByChilds(String toString, String userid, String planid);

    PageInfo<Map<String, Object>> fingByTreeCSHZByUser(String node, String templId, String planid, String userid, Integer pageNumber, Integer pageSize);

    void saveBack(String taskid, String proposal, String planid, String userid);

    List<Object[]> findElementByPlanid(String planid);
}
