package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.FormulaTrace;

import java.util.List;
import java.util.Map;

/**
 * 公式追踪Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface FormulaTraceService {

    /**
     * 查询公式追踪列表
     */
    List<FormulaTrace> selectList(Map<String, Object> params);

    /**
     * 分页查询公式追踪列表
     */
    Page<FormulaTrace> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询公式追踪
     */
    FormulaTrace selectById(String traceId);

    /**
     * 新增公式追踪
     */
    boolean insert(FormulaTrace trace);

    /**
     * 修改公式追踪
     */
    boolean update(FormulaTrace trace);

    /**
     * 删除公式追踪
     */
    boolean deleteById(String traceId);

    /**
     * 创建追踪任务
     */
    boolean createTraceTask(String formulaId, String traceType, Integer traceDepth);

    /**
     * 获取公式依赖关系
     */
    Map<String, Object> getFormulaDependencies(String formulaId);

    /**
     * 获取公式影响分析
     */
    Map<String, Object> getFormulaImpactAnalysis(String formulaId);
}

