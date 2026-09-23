package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务组织Mapper接口
 */
public interface FinanceOrgMapper {
    
    /**
     * 查询组织列表
     */
    List<Map<String, Object>> selectOrgList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询组织详情
     */
    Map<String, Object> selectOrgById(@Param("orgId") String orgId);
    
    /**
     * 插入组织
     */
    int insertOrg(@Param("org") Map<String, Object> org);
    
    /**
     * 更新组织
     */
    int updateOrg(@Param("org") Map<String, Object> org);
    
    /**
     * 删除组织
     */
    int deleteOrg(@Param("orgId") String orgId);
    
    /**
     * 查询组织树
     */
    List<Map<String, Object>> selectOrgTree(@Param("params") Map<String, Object> params);

    // ==================== 财务组织管理相关方法 ====================

    /**
     * 查询财务组织列表
     */
    List<Map<String, Object>> selectCwzzList(@Param("params") Map<String, Object> params);

    /**
     * 统计财务组织总数
     */
    int countCwzzList(@Param("params") Map<String, Object> params);

    /**
     * 关联公司
     */
    int relateCompany(@Param("data") Map<String, Object> data);

    /**
     * 查询财务组织树列表
     */
    List<Map<String, Object>> selectCwzzTreeList(@Param("params") Map<String, Object> params);

    /**
     * 统计财务组织树总数
     */
    int countCwzzTreeList(@Param("params") Map<String, Object> params);
}