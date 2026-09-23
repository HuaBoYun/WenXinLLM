package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetDimension;
import com.management.accountant.oracle.entity.budget.BudgetDimensionAttribute;
import com.management.accountant.oracle.entity.budget.BudgetDimensionRelation;

import java.util.List;
import java.util.Map;

/**
 * 预算维度配置管理Service接口
 * 
 * @description 预算维度配置管理业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetDimensionService {

    /**
     * 创建维度
     * 
     * @param dimension 维度对象
     * @return 创建后的维度
     */
    BudgetDimension create(BudgetDimension dimension);

    /**
     * 根据ID查询维度
     * 
     * @param dimensionId 维度ID
     * @return 维度对象
     */
    BudgetDimension getById(String dimensionId);

    /**
     * 更新维度
     * 
     * @param dimension 维度对象
     * @return 更新后的维度
     */
    BudgetDimension update(BudgetDimension dimension);

    /**
     * 删除维度
     * 
     * @param dimensionId 维度ID
     */
    void delete(String dimensionId);

    /**
     * 分页查询维度列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getPage(Map<String, Object> params);

    /**
     * 获取维度树结构
     *
     * @return 树结构数据
     */
    List<Map<String, Object>> getTree();

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getStats();

    /**
     * 批量删除维度
     * 
     * @param ids ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 获取子维度列表
     * 
     * @param parentId 父维度ID
     * @return 子维度列表
     */
    List<BudgetDimension> getChildren(String parentId);

    /**
     * 获取维度值列表
     * 
     * @param dimensionId 维度ID
     * @return 维度值列表
     */
    List<Map<String, Object>> getValues(String dimensionId);

    /**
     * 批量创建维度值
     * 
     * @param dimensionId 维度ID
     * @param values 维度值列表
     */
    void batchCreateValues(String dimensionId, List<Map<String, Object>> values);

    /**
     * 检查编码是否存在
     *
     * @param code 维度编码
     * @return true-存在，false-不存在
     */
    boolean checkCodeExists(String code);

    /**
     * 获取维度属性
     */
    List<BudgetDimensionAttribute> getAttributes(String dimensionId);

    /**
     * 创建维度属性
     */
    BudgetDimensionAttribute createAttribute(BudgetDimensionAttribute attribute);

    /**
     * 更新维度属性
     */
    BudgetDimensionAttribute updateAttribute(String attributeId, BudgetDimensionAttribute attribute);

    /**
     * 删除维度属性
     */
    void deleteAttribute(String attributeId);

    /**
     * 获取维度关联关系
     */
    List<BudgetDimensionRelation> getRelations(String dimensionId);

    /**
     * 创建维度关联关系
     */
    BudgetDimensionRelation createRelation(BudgetDimensionRelation relation);

    /**
     * 更新维度关联关系
     */
    BudgetDimensionRelation updateRelation(String relationId, BudgetDimensionRelation relation);

    /**
     * 删除维度关联关系
     */
    void deleteRelation(String relationId);

    /**
     * 获取父级维度列表
     *
     * @return 父级维度列表
     */
    List<BudgetDimension> getParents();

    /**
     * 更新维度状态
     *
     * @param dimensionId 维度ID
     * @param isActive 是否启用
     */
    void updateStatus(String dimensionId, Boolean isActive);

    /**
     * 批量验证维度
     *
     * @param ids 维度ID列表
     * @return 验证结果
     */
    Map<String, Object> batchValidate(List<String> ids);

    /**
     * 获取维度成员列表（子维度）
     *
     * @param dimensionId 维度ID
     * @return 成员列表
     */
    List<Map<String, Object>> getMembers(String dimensionId);

    /**
     * 保存维度成员（子维度批量新增/更新/删除）
     *
     * @param dimensionId 维度ID
     * @param members     成员列表
     */
    void saveMembers(String dimensionId, List<Map<String, Object>> members);

    /**
     * 导入维度成员
     *
     * @param dimensionId 维度ID
     * @param file Excel文件
     * @return 导入结果
     */
    Map<String, Object> importMembers(String dimensionId, org.springframework.web.multipart.MultipartFile file);

    /**
     * 删除维度成员
     *
     * @param memberId 成员ID（子维度ID）
     */
    void deleteMember(String memberId);

    /**
     * 导出维度数据
     *
     * @param params 查询参数
     * @param response HTTP响应
     */
    void exportDimension(Map<String, Object> params, javax.servlet.http.HttpServletResponse response);

    /**
     * 导出单个维度数据
     *
     * @param dimensionId 维度ID
     * @param response HTTP响应
     */
    void exportSingleDimension(String dimensionId, javax.servlet.http.HttpServletResponse response);

    /**
     * 导入维度数据
     *
     * @param file Excel文件
     * @return 导入结果
     */
    Map<String, Object> importDimensions(org.springframework.web.multipart.MultipartFile file);

    /**
     * 下载维度导入模板
     *
     * @param response HTTP响应
     */
    void downloadDimensionTemplate(javax.servlet.http.HttpServletResponse response);
}

