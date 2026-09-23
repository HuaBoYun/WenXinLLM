package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetDimensionValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算维度值Mapper接口
 * 
 * @description 预算维度值数据访问层
 * @author AI Agent
 * @date 2026-01-30
 */
@Mapper
public interface BudgetDimensionValueMapper extends BaseMapper<BudgetDimensionValue> {

    /**
     * 根据维度ID查询维度值列表
     * 
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 维度值列表
     */
    List<BudgetDimensionValue> selectByDimensionId(@Param("dimensionId") String dimensionId,
                                                     @Param("companyId") String companyId);

    /**
     * 根据维度值编码查询
     * 
     * @param valueCode 维度值编码
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 维度值信息
     */
    BudgetDimensionValue selectByCode(@Param("valueCode") String valueCode,
                                       @Param("dimensionId") String dimensionId,
                                       @Param("companyId") String companyId);

    /**
     * 查询维度值树结构
     * 
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 树形结构数据
     */
    List<Map<String, Object>> selectValueTree(@Param("dimensionId") String dimensionId,
                                                @Param("companyId") String companyId);

    /**
     * 查询子维度值列表
     * 
     * @param parentId 父维度值ID
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 子维度值列表
     */
    List<BudgetDimensionValue> selectChildrenByParentId(@Param("parentId") String parentId,
                                                         @Param("dimensionId") String dimensionId,
                                                         @Param("companyId") String companyId);

    /**
     * 批量插入维度值
     * 
     * @param valueList 维度值列表
     * @return 影响行数
     */
    int batchInsert(@Param("valueList") List<BudgetDimensionValue> valueList);

    /**
     * 批量删除维度值
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("companyId") String companyId);

    /**
     * 更新子节点的路径信息
     * 
     * @param oldPath 旧路径
     * @param newPath 新路径
     * @param companyId 公司ID
     * @return 影响行数
     */
    int updateChildrenPath(@Param("oldPath") String oldPath,
                           @Param("newPath") String newPath,
                           @Param("companyId") String companyId);

    /**
     * 查询叶子节点列表
     * 
     * @param dimensionId 维度ID
     * @param companyId 公司ID
     * @return 叶子节点列表
     */
    List<BudgetDimensionValue> selectLeafNodes(@Param("dimensionId") String dimensionId,
                                                @Param("companyId") String companyId);
}

