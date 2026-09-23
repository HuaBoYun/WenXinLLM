package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetOrganizationStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算组织体系Mapper接口
 * 
 * @description 预算组织体系数据访问层
 * @author AI Assistant
 * @date 2025-12-31
 */
@Mapper
public interface BudgetOrganizationStructureMapper extends BaseMapper<BudgetOrganizationStructure> {

    /**
     * 根据体系编码查询
     * 
     * @param structureCode 体系编码
     * @param companyId 公司ID
     * @return 组织体系
     */
    BudgetOrganizationStructure selectByCode(@Param("structureCode") String structureCode, 
                                              @Param("companyId") String companyId);

    /**
     * 查询组织体系树结构
     * 
     * @param structureId 体系ID
     * @param companyId 公司ID
     * @return 树形结构数据
     */
    List<Map<String, Object>> selectTreeByStructureId(@Param("structureId") String structureId,
                                                       @Param("companyId") String companyId);

    /**
     * 分页查询组织体系列表
     * 
     * @param params 查询参数
     * @return 组织体系列表
     */
    List<BudgetOrganizationStructure> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 统计组织体系数量
     * 
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);

    /**
     * 批量删除组织体系
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("companyId") String companyId);
}

