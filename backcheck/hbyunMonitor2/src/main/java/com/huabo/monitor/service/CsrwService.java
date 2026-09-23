package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CsrwService {


    IPage<TblTestplan> findAllRwToOrg(Integer pageNumber, String userid, String orgid);

    List<Tree> getTreeCSRWByChildsReturn(String toString, String toString1, String planid)throws Exception;

    List<Tree> getTreeCSRWByChilds(String toString, String toString1, String planid);

    PageInfo<Map<String, Object>> fingByTreeCSRWByUserReturn(String node, String templId, String planid, BigDecimal staffid, Integer pageNumber);
   
    PageInfo<Map<String, Object>> fingByTreeCSRWByUserReturngz(String node, String templId, String planid, BigDecimal staffid, Integer pageNumber);
    
    PageInfo<Map<String, Object>> fingByTreeCSRWByUser(String node, String templId, String planid, BigDecimal staffid, Integer pageNumber);

    PageInfo<Map<String, Object>> fingByTreeCSRWByUsergz(String node, String templId, String planid, BigDecimal staffid, Integer pageNumber);

    
    String saveAll(BigDecimal bigDecimal, BigDecimal staffid) throws Exception;
    
    PageInfo<TblTestplan> findAllRwToOrgNew(Integer pageNumber, String userid, String orgid,TblStaffUtil user) throws Exception;


    
}
