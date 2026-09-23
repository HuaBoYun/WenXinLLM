package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.FinancingRiskMonitoring;

import java.util.List;
import java.util.Map;

/**
 * 融资风险监控Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface FinancingRiskMonitoringService extends IService<FinancingRiskMonitoring> {

    /**
     * 分页查询融资风险监控列表
     * @param params 查询参数
     * @return 融资风险监控列表
     */
    List<FinancingRiskMonitoring> selectPageList(Map<String, Object> params);

    /**
     * 根据ID查询融资风险监控详情
     * @param id 主键ID
     * @return 融资风险监控详情
     */
    FinancingRiskMonitoring selectDetailById(Long id);

    /**
     * 保存融资风险监控
     * @param entity 融资风险监控实体
     * @return 是否成功
     */
    boolean saveMonitoring(FinancingRiskMonitoring entity);

    /**
     * 更新融资风险监控
     * @param entity 融资风险监控实体
     * @return 是否成功
     */
    boolean updateMonitoring(FinancingRiskMonitoring entity);

    /**
     * 删除融资风险监控
     * @param id 主键ID
     * @return 是否成功
     */
    boolean deleteMonitoring(Long id);
}

