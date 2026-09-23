package com.financial.sharing.groupControl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.groupControl.entity.TblDimensionMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 维度成员Mapper接口
 * 
 * @author system
 * @since 2026-01-30
 */
public interface DimensionMemberMapper extends BaseMapper<TblDimensionMember> {

    /**
     * 根据维度ID查询根节点列表
     * 
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 根节点列表
     */
    @Select("SELECT * FROM TBL_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} " +
            "AND TENANT_ID = #{tenantId} AND (PARENT_MEMBER_ID IS NULL OR PARENT_MEMBER_ID = '') " +
            "AND STATUS = 'ACTIVE' ORDER BY SORT_NO ASC")
    List<TblDimensionMember> selectRootMembers(@Param("dimensionId") String dimensionId, 
                                                @Param("tenantId") String tenantId);

    /**
     * 根据父成员ID查询子成员列表
     * 
     * @param parentMemberId 父成员ID
     * @param tenantId 租户ID
     * @return 子成员列表
     */
    @Select("SELECT * FROM TBL_DIMENSION_MEMBER WHERE PARENT_MEMBER_ID = #{parentMemberId} " +
            "AND TENANT_ID = #{tenantId} AND STATUS = 'ACTIVE' ORDER BY SORT_NO ASC")
    List<TblDimensionMember> selectChildMembers(@Param("parentMemberId") String parentMemberId, 
                                                 @Param("tenantId") String tenantId);

    /**
     * 根据成员编码查询成员
     * 
     * @param dimensionId 维度ID
     * @param memberCode 成员编码
     * @param tenantId 租户ID
     * @return 成员信息
     */
    @Select("SELECT * FROM TBL_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} " +
            "AND MEMBER_CODE = #{memberCode} AND TENANT_ID = #{tenantId}")
    TblDimensionMember selectByCode(@Param("dimensionId") String dimensionId,
                                     @Param("memberCode") String memberCode,
                                     @Param("tenantId") String tenantId);

    /**
     * 查询子成员数量
     * 
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 子成员数量
     */
    @Select("SELECT COUNT(*) FROM TBL_DIMENSION_MEMBER WHERE PARENT_MEMBER_ID = #{memberId} " +
            "AND TENANT_ID = #{tenantId}")
    int selectChildCount(@Param("memberId") String memberId, @Param("tenantId") String tenantId);
}

