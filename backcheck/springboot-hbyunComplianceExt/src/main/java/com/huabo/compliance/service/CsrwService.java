package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CsrwService {


    IPage<TblTestplan> findAllRwToOrg(Integer pageNumber, String userid, String orgid,Integer pageSize);

    List<Tree> getTreeCSRWByChildsReturn(String toString, String toString1, String planid);

    List<Tree> getTreeCSRWByChilds(String toString, String toString1, String planid);

    IPage<Map<String, Object>> fingByTreeCSRWByUserReturn(String node, String templId, String planid, BigDecimal staffid, Integer pageNumber);

    IPage<Map<String, Object>> fingByTreeCSRWByUser(String node, String templId, String planid, BigDecimal staffid, Integer pageNumber);

    String saveAll(BigDecimal bigDecimal, BigDecimal staffid) throws Exception;
}
