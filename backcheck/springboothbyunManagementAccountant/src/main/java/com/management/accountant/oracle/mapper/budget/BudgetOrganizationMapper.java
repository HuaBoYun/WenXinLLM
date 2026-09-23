package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算组织体系Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetOrganizationMapper extends BaseMapper<BudgetOrganization> {

    /**
     * 根据组织编码查询组织
     */
    BudgetOrganization selectByOrganizationCode(@Param("organizationCode") String organizationCode);

    /**
     * 根据组织类型查询组织列表
     */
    List<BudgetOrganization> selectByOrganizationType(@Param("organizationType") String organizationType);

    /**
     * 根据上级组织ID查询子组织列表
     */
    List<BudgetOrganization> selectByParentId(@Param("parentId") String parentId);

    /**
     * 查询组织树结构
     */
    List<BudgetOrganization> selectOrganizationTree(@Param("parentId") String parentId);

    /**
     * 查询启用的组织列表
     */
    List<BudgetOrganization> selectEnabledOrganizations();

    /**
     * 批量删除组织
     */
    int batchDeleteOrganizations(@Param("organizationIds") List<String> organizationIds);
}

