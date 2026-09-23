package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * BU数据服务接口
 */
public interface FinanceBuDataService {

    /**
     * 获取BU数据列表
     */
    List<Map<String, Object>> getBuDataList(Map<String, Object> params);

    /**
     * 获取BU数据详情
     */
    Map<String, Object> getBuDataDetail(String buId);

    /**
     * 创建BU数据
     */
    int createBuData(Map<String, Object> buData);

    /**
     * 更新BU数据
     */
    int updateBuData(Map<String, Object> buData);

    /**
     * 删除BU数据
     */
    int deleteBuData(String buId);

    /**
     * 获取BU数据统计
     */
    Map<String, Object> getBuDataStatistics(Map<String, Object> params);

    /**
     * 导入BU数据
     */
    int importBuData(List<Map<String, Object>> buDataList);

    /**
     * 导出BU数据
     */
    List<Map<String, Object>> exportBuData(Map<String, Object> params);

    // ==================== Controller 需要的方法 ====================

    /**
     * 业务数据列表
     */
    PageResult<Map<String, Object>> getYWSJList(Map<String, Object> params);

    /**
     * 业务数据新建
     */
    Map<String, Object> getYWSJAdd(Map<String, Object> data);

    /**
     * 业务数据编辑
     */
    Map<String, Object> getYWSJEdit(Map<String, Object> data);

    /**
     * 业务数据详情
     */
    Map<String, Object> getYWSJDetail(Map<String, Object> params);

    /**
     * 业务数据删除
     */
    boolean getYWSJDelete(Map<String, Object> params);

    /**
     * 业务数据发布
     */
    boolean YWSJFabu(Map<String, Object> params);

    /**
     * 修改业务数据状态
     */
    boolean changeStatus(Map<String, Object> params);

    /**
     * 开始采集业务数据
     */
    Map<String, Object> startCaiJi(Map<String, Object> params);

    /**
     * 停止采集业务数据
     */
    boolean stopCaiJi(Map<String, Object> params);

    /**
     * SQL测试
     */
    Map<String, Object> sqlTest(Map<String, Object> data);
}