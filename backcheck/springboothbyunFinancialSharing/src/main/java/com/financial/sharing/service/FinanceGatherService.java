package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 财务数据收集服务接口
 */
public interface FinanceGatherService {

    /**
     * 获取财务数据收集列表
     */
    List<Map<String, Object>> getGatherList(Map<String, Object> params);

    /**
     * 获取财务数据收集详情
     */
    Map<String, Object> getGatherDetail(String gatherId);

    /**
     * 创建财务数据收集
     */
    int createGather(Map<String, Object> gather);

    /**
     * 更新财务数据收集
     */
    int updateGather(Map<String, Object> gather);

    /**
     * 删除财务数据收集
     */
    int deleteGather(String gatherId);

    /**
     * 提交财务数据收集
     */
    int submitGather(String gatherId);

    /**
     * 审核财务数据收集
     */
    int approveGather(Map<String, Object> params);

    /**
     * 获取财务数据收集统计
     */
    Map<String, Object> getGatherStatistics(Map<String, Object> params);

    // ==================== Controller 需要的方法 ====================

    /**
     * 执行抽取
     */
    Map<String, Object> executeChouqu(Map<String, Object> params);

    /**
     * 停止采集
     */
    boolean stopChouqu(Map<String, Object> params);

    /**
     * 获取采集方案结构树
     */
    PageResult<Map<String, Object>> getFaTree(Map<String, Object> params);

    /**
     * 停止方案采集
     */
    boolean stopFaGather(Map<String, Object> params);

    /**
     * 开始方案采集
     */
    Map<String, Object> startFaGather(Map<String, Object> params);

    /**
     * 获取采集子方案采集状态
     */
    Map<String, Object> getFaGatherStatus(Map<String, Object> params);

    /**
     * 停止采集子方案
     */
    boolean stopFaGatherSub(Map<String, Object> params);

    /**
     * 业务数据采集记录
     */
    PageResult<Map<String, Object>> CaiJiLog(Map<String, Object> params);

    /**
     * 业务数据采集记录当前的采集结果
     */
    Map<String, Object> CaiJiLogResult(Map<String, Object> params);
}