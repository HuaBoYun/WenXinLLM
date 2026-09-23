package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CsjgService {


    IPage<Map<String, Object>> findAllCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal orgid,Integer pageSize);

    IPage<Map<String, Object>> findAllWCSjCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal staffid,Integer pageSize);

    List<Tree> getTreeLeftyx(String testtemid, String planid, String s, String userid);

    List<Tree> getTreeLeftwx(String toString, String planid, String s, String toString1);

    List<Tree> getTreeLeftbxy(String toString, String planid, String s, String toString1);

    IPage<Map<String, Object>> fingByTreeCSRW(String node, String templId, String planid, Integer pageNumber);

    IPage<Map<String, Object>> fingByTreeCSJGY(String node, String templId, String planid, Integer pageNumber);

    IPage<Map<String, Object>> fingByTreeCSJGW(String node, String templId, String planid, Integer pageNumber);

    IPage<Map<String, Object>> fingByTreeCSJGB(String node, String templId, String planid, Integer pageNumber);

    IPage<Map<String, Object>> findAllnoSjResult(BigDecimal planid, Integer pageNumber);

    List<Tree> getTreeCSHZByChilds(String toString, String userid, String planid);

    IPage<Map<String, Object>> fingByTreeCSHZByUser(String node, String templId, String planid, String userid, Integer pageNumber);

    void saveBack(String taskid, String proposal, String planid, String userid);

    List<Object[]> findElementByPlanid(String planid);
}
