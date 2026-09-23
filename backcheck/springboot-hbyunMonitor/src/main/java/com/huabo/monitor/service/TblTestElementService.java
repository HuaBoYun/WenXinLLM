package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblTestElement;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface TblTestElementService {

    /**
     * 根据测试方案查询模板右侧的总数
     *
     * @param planid
     * @return
     */
    Integer findByPlan(String planid);

    /**
     * 根据测试方案查询授权的总数
     *
     * @param planid
     * @return
     */
    Integer findByPlanwStra(String planid);

    boolean checkCode(String code, BigDecimal templId);

    String fingBynode(String node, String templId, Integer pageNumber, int pageSize);

    List<TblTestElement> fingBynode(String node, String templId);


    String fingByTree(String node, String templId, String planid, Integer pageNumber, int pageSize);

    String fingByTreeCSRW(String node, String templId, String planid, Integer pageNumber, int pageSize);

    String fingByTreeCSRWByUser(String node, String templId, String planid, String userid, Integer pageNumber, int pageSize);

    String fingByPlanAllCSRW(String planid, Integer pageNumber, int pageSize);


    String fingByPlanAll(String planid, Integer pageNumber, int pageSize);

    List<TblTestElement> findByPlanidAll(String planid);

    List<Object[]> fingByTreeVSRWCZ(String node, String templId, String planid, String taskid);

    List<Object[]> fingByTreeCSRWCZ(String templId);


    String fingByTreeCSJGY(String node, String templId, String planid, Integer pageNumber, int pageSize);


    String fingByTreeCSHZByUser(String node, String templId, String planid, String userid, Integer pageNumber, int pageSize);

    String fingByTreeCSJGW(String node, String templId, String planid, Integer pageNumber, int pageSize);

    JsonBean fingByTreeCSJGB(String token, Integer pageNumber, String node, String templId, String planid);

    /**
     * 根据模板删除
     *
     * @param templId
     */
    void deleteByTempId(BigDecimal templId);

    /**
     * @Author: TYB
     * @Date: 2017-01-17 下午 4:02
     * @Des: 测试模板导入
     */
    void csmbImport(String path, BigDecimal tmplId) throws IOException;

    /**
     * 查询用户退回的任务
     *
     * @param node
     * @param templId
     * @param planid
     * @param userid
     * @param pageNumber
     * @param pageSize
     * @return
     */
    String fingByTreeCSRWByUserReturn(String node, String templId, String planid, String userid, Integer pageNumber, int pageSize);

    /**
     * 内控测试--结果导出--查询所有测试结果
     * planid	测试方案id
     */
    List<Object[]> findElementByPlanid(String planid);
}
