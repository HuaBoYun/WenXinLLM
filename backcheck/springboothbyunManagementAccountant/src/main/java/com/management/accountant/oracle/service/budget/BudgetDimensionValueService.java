package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetDimensionValue;

import java.util.List;
import java.util.Map;

/**
 * 预算维度值Service接口
 * 
 * @description 预算维度值业务逻辑层
 * @author AI Agent
 * @date 2026-01-30
 */
public interface BudgetDimensionValueService extends IService<BudgetDimensionValue> {

    /**
     * 根据维度ID查询维度值列表
     * 
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 维度值列表
     */
    List<BudgetDimensionValue> getByDimensionId(String dimensionId, String companyId);

    /**
     * 根据维度值编码查询
     * 
     * @param valueCode 维度值编码
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 维度值信息
     */
    BudgetDimensionValue getByCode(String valueCode, String dimensionId, String companyId);

    /**
     * 查询维度值树结构
     * 
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 树形结构数据
     */
    List<Map<String, Object>> getValueTree(String dimensionId, String companyId);

    /**
     * 查询子维度值列表
     * 
     * @param parentId 父维度值ID
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 子维度值列表
     */
    List<BudgetDimensionValue> getChildrenByParentId(String parentId, String dimensionId, String companyId);

    /**
     * 批量创建维度值
     * 
     * @param dimensionId 维度ID
     * @param values 维度值列表
     * @param companyId 公司ID
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 创建数量
     */
    int batchCreateValues(String dimensionId, List<Map<String, Object>> values, 
                          String companyId, String userId, String userName);

    /**
     * 批量删除维度值
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 删除数量
     */
    int batchDeleteValues(List<String> ids, String companyId);

    /**
     * 查询叶子节点列表
     * 
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 叶子节点列表
     */
    List<BudgetDimensionValue> getLeafNodes(String dimensionId, String companyId);
}

