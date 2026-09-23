package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.BatchCalculation;

import java.util.List;
import java.util.Map;

/**
 * 批量计算Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BatchCalculationService {

    /**
     * 查询批量计算列表
     */
    List<BatchCalculation> selectList(Map<String, Object> params);

    /**
     * 分页查询批量计算列表
     */
    Page<BatchCalculation> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询批量计算
     */
    BatchCalculation selectById(String calculationId);

    /**
     * 新增批量计算
     */
    boolean insert(BatchCalculation calculation);

    /**
     * 修改批量计算
     */
    boolean update(BatchCalculation calculation);

    /**
     * 删除批量计算
     */
    boolean deleteById(String calculationId);

    /**
     * 执行批量计算
     */
    boolean executeCalculation(String calculationId);

    /**
     * 获取计算结果
     */
    Map<String, Object> getCalculationResult(String calculationId);
}

