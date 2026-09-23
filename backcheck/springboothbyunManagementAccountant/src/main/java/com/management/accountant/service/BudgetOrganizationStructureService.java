package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetOrganizationStructure;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算组织体系管理Service接口
 * 
 * @description 预算组织体系管理业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetOrganizationStructureService {

    /**
     * 创建组织体系
     * 
     * @param structure 组织体系对象
     * @return 创建后的组织体系
     */
    BudgetOrganizationStructure create(BudgetOrganizationStructure structure);

    /**
     * 根据ID查询组织体系
     * 
     * @param structureId 组织体系ID
     * @return 组织体系对象
     */
    BudgetOrganizationStructure getById(String structureId);

    /**
     * 更新组织体系
     * 
     * @param structure 组织体系对象
     * @return 更新后的组织体系
     */
    BudgetOrganizationStructure update(BudgetOrganizationStructure structure);

    /**
     * 删除组织体系
     * 
     * @param structureId 组织体系ID
     */
    void delete(String structureId);

    /**
     * 分页查询组织体系列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetOrganizationStructure> getPage(Map<String, Object> params);

    /**
     * 获取组织体系树结构
     * 
     * @param structureId 组织体系ID
     * @return 树结构数据
     */
    Map<String, Object> getTree(String structureId);

    /**
     * 批量删除组织体系
     * 
     * @param ids ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 启用组织体系
     * 
     * @param structureId 组织体系ID
     */
    void enable(String structureId);

    /**
     * 禁用组织体系
     *
     * @param structureId 组织体系ID
     */
    void disable(String structureId);

    /**
     * 批量更新启用状态
     *
     * @param ids ID列表
     * @param isEnabled 启用状态 1-启用 0-禁用
     */
    void batchUpdateEnabled(List<String> ids, int isEnabled);

    /**
     * 复制组织体系
     *
     * @param structureId 源组织体系ID
     * @param newName 新体系名称
     * @param newCode 新体系编码
     * @return 复制后的组织体系
     */
    BudgetOrganizationStructure copy(String structureId, String newName, String newCode);

    /**
     * 检查编码是否存在
     * 
     * @param code 体系编码
     * @return true-存在，false-不存在
     */
    boolean checkCodeExists(String code);

    /**
     * 获取统计数据
     * 
     * @return 统计数据
     */
    Map<String, Object> getStats();

    /**
     * 查询所有未删除的组织体系（用于导出）
     * 
     * @param params 查询参数（可选筛选条件）
     * @return 组织体系列表
     */
    List<BudgetOrganizationStructure> getAll(Map<String, Object> params);
}

