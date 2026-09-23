package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetIndicator;
import com.management.accountant.util.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预算指标管理Service接口
 * 
 * @description 预算指标管理业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetIndicatorService {

    /**
     * 创建指标
     * 
     * @param indicator 指标对象
     * @return 创建后的指标
     */
    BudgetIndicator create(BudgetIndicator indicator);

    /**
     * 根据ID查询指标
     * 
     * @param indicatorId 指标ID
     * @return 指标对象
     */
    BudgetIndicator getById(String indicatorId);

    /**
     * 更新指标
     * 
     * @param indicator 指标对象
     * @return 更新后的指标
     */
    BudgetIndicator update(BudgetIndicator indicator);

    /**
     * 删除指标
     * 
     * @param indicatorId 指标ID
     */
    void delete(String indicatorId);

    /**
     * 分页查询指标列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetIndicator> getPage(Map<String, Object> params);

    /**
     * 获取指标树结构
     * 
     * @return 树结构数据
     */
    List<Map<String, Object>> getTree();

    /**
     * 批量删除指标
     * 
     * @param ids ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 启用指标
     * 
     * @param indicatorId 指标ID
     */
    void enable(String indicatorId);

    /**
     * 禁用指标
     * 
     * @param indicatorId 指标ID
     */
    void disable(String indicatorId);

    /**
     * 验证公式
     * 
     * @param formula 公式
     * @return 验证结果
     */
    Map<String, Object> validateFormula(String formula);

    /**
     * 检查编码是否存在
     *
     * @param code 指标编码
     * @return true-存在，false-不存在
     */
    boolean checkCodeExists(String code);

    /**
     * 批量启用指标
     *
     * @param ids ID列表
     */
    void batchEnable(List<String> ids);

    /**
     * 批量禁用指标
     *
     * @param ids ID列表
     */
    void batchDisable(List<String> ids);

    /**
     * 导入指标
     *
     * @param file 文件
     * @return 导入结果
     */
    Map<String, Object> importIndicators(MultipartFile file);

    /**
     * 导出指标
     *
     * @param response 响应对象
     */
    void exportIndicators(HttpServletResponse response);
}

