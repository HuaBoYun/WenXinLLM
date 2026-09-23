package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetDimension;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算维度Mapper接口
 * 
 * @description 预算维度数据访问层
 * @author AI Assistant
 * @date 2025-12-31
 */
@Mapper
public interface BudgetDimensionMapper extends BaseMapper<BudgetDimension> {

    /**
     * 根据维度编码查询
     * 
     * @param dimensionCode 维度编码
     * @param companyId 公司ID
     * @return 维度信息
     */
    BudgetDimension selectByCode(@Param("dimensionCode") String dimensionCode,
                                  @Param("companyId") String companyId);

    /**
     * 查询维度树结构
     * 
     * @param companyId 公司ID
     * @return 树形结构数据
     */
    List<Map<String, Object>> selectDimensionTree(@Param("companyId") String companyId);

    /**
     * 查询子维度列表
     * 
     * @param parentId 父维度ID
     * @param companyId 公司ID
     * @return 子维度列表
     */
    List<BudgetDimension> selectChildrenByParentId(@Param("parentId") String parentId,
                                                    @Param("companyId") String companyId);

    /**
     * 查询维度属性
     * 
     * @param dimensionId 维度ID
     * @return 属性列表
     */
    List<Map<String, Object>> selectDimensionAttributes(@Param("dimensionId") String dimensionId);

    /**
     * 查询维度关联关系
     * 
     * @param dimensionId 维度ID
     * @return 关联关系列表
     */
    List<Map<String, Object>> selectDimensionRelations(@Param("dimensionId") String dimensionId);

    /**
     * 批量删除维度
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("companyId") String companyId);
}

