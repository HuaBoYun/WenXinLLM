package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetDimensionMember;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算维度成员Mapper接口
 * 
 * @description 预算维度成员数据访问层，支持维度成员的层级结构管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetDimensionMemberMapper extends BaseMapper<BudgetDimensionMember> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据成员编码查询维度成员
     * @param memberCode 成员编码
     * @param tenantId 租户ID
     * @return 维度成员信息
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE MEMBER_CODE = #{memberCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetDimensionMember selectByMemberCode(@Param("memberCode") String memberCode, @Param("tenantId") String tenantId);

    /**
     * 根据维度ID查询维度成员列表
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 维度成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectByDimensionId(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 根据父成员ID查询子成员列表
     * @param parentMemberId 父成员ID
     * @param tenantId 租户ID
     * @return 子成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE PARENT_MEMBER_ID = #{parentMemberId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectByParentMemberId(@Param("parentMemberId") String parentMemberId, @Param("tenantId") String tenantId);

    /**
     * 根据成员层级查询维度成员列表
     * @param dimensionId 维度ID
     * @param memberLevel 成员层级
     * @param tenantId 租户ID
     * @return 维度成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND MEMBER_LEVEL = #{memberLevel} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectByMemberLevel(@Param("dimensionId") String dimensionId, @Param("memberLevel") Integer memberLevel, @Param("tenantId") String tenantId);

    /**
     * 根据成员类型查询维度成员列表
     * @param dimensionId 维度ID
     * @param memberType 成员类型
     * @param tenantId 租户ID
     * @return 维度成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND MEMBER_TYPE = #{memberType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectByMemberType(@Param("dimensionId") String dimensionId, @Param("memberType") String memberType, @Param("tenantId") String tenantId);

    /**
     * 查询叶子节点成员列表
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 叶子节点成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND IS_LEAF = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectLeafMembers(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 查询根节点成员列表
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 根节点成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND (PARENT_MEMBER_ID IS NULL OR PARENT_MEMBER_ID = '') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectRootMembers(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询维度成员
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetDimensionMember> selectDimensionMemberPage(Page<BudgetDimensionMember> page, @Param("params") Map<String, Object> params);

    /**
     * 根据成员路径查询维度成员
     * @param dimensionId 维度ID
     * @param memberPath 成员路径
     * @param tenantId 租户ID
     * @return 维度成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND MEMBER_PATH LIKE CONCAT(#{memberPath}, '%') AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY MEMBER_PATH ASC")
    List<BudgetDimensionMember> selectByMemberPath(@Param("dimensionId") String dimensionId, @Param("memberPath") String memberPath, @Param("tenantId") String tenantId);

    /**
     * 查询成员的所有祖先节点
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 祖先节点列表
     */
    List<BudgetDimensionMember> selectAncestorMembers(@Param("memberId") String memberId, @Param("tenantId") String tenantId);

    /**
     * 查询成员的所有后代节点
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 后代节点列表
     */
    List<BudgetDimensionMember> selectDescendantMembers(@Param("memberId") String memberId, @Param("tenantId") String tenantId);

    /**
     * 查询成员的兄弟节点
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 兄弟节点列表
     */
    List<BudgetDimensionMember> selectSiblingMembers(@Param("memberId") String memberId, @Param("tenantId") String tenantId);

    /**
     * 查询启用的维度成员
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 启用的维度成员列表
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY SORT_ORDER ASC")
    List<BudgetDimensionMember> selectEnabledMembers(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 根据外部编码查询维度成员
     * @param externalCode 外部编码
     * @param tenantId 租户ID
     * @return 维度成员信息
     */
    @Select("SELECT * FROM BUDGET_DIMENSION_MEMBER WHERE EXTERNAL_CODE = #{externalCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetDimensionMember selectByExternalCode(@Param("externalCode") String externalCode, @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计维度成员数量
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 成员数量
     */
    @Select("SELECT COUNT(*) FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countByDimensionId(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 统计叶子节点数量
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 叶子节点数量
     */
    @Select("SELECT COUNT(*) FROM BUDGET_DIMENSION_MEMBER WHERE DIMENSION_ID = #{dimensionId} AND IS_LEAF = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countLeafMembers(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 统计各层级成员数量
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 各层级成员数量统计
     */
    List<Map<String, Object>> countMembersByLevel(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 统计各类型成员数量
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 各类型成员数量统计
     */
    List<Map<String, Object>> countMembersByType(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 获取维度成员统计信息
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectMemberStatistics(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 批量更新成员状态
     * @param memberIds 成员ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateMemberStatus(@Param("memberIds") List<String> memberIds, @Param("status") String status, 
                               @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 批量更新成员启用状态
     * @param memberIds 成员ID列表
     * @param isEnabled 是否启用
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateMemberEnabled(@Param("memberIds") List<String> memberIds, @Param("isEnabled") Boolean isEnabled, 
                                @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新成员路径
     * @param memberId 成员ID
     * @param memberPath 成员路径
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_DIMENSION_MEMBER SET MEMBER_PATH = #{memberPath}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{memberId}")
    int updateMemberPath(@Param("memberId") String memberId, @Param("memberPath") String memberPath, 
                        @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新成员层级
     * @param memberId 成员ID
     * @param memberLevel 成员层级
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_DIMENSION_MEMBER SET MEMBER_LEVEL = #{memberLevel}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{memberId}")
    int updateMemberLevel(@Param("memberId") String memberId, @Param("memberLevel") Integer memberLevel, 
                         @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 更新叶子节点标志
     * @param memberId 成员ID
     * @param isLeaf 是否叶子节点
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_DIMENSION_MEMBER SET IS_LEAF = #{isLeaf}, UPDATE_BY = #{updateBy}, UPDATE_TIME = #{updateTime} WHERE ID = #{memberId}")
    int updateMemberLeafFlag(@Param("memberId") String memberId, @Param("isLeaf") Boolean isLeaf, 
                            @Param("updateBy") String updateBy, @Param("updateTime") java.time.LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期的维度成员
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupExpiredMembers(@Param("tenantId") String tenantId);

    /**
     * 清理无效的维度成员（维度不存在）
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupInvalidMembers(@Param("tenantId") String tenantId);

    /**
     * 重建成员路径
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 重建数量
     */
    int rebuildMemberPaths(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    /**
     * 重建成员层级
     * @param dimensionId 维度ID
     * @param tenantId 租户ID
     * @return 重建数量
     */
    int rebuildMemberLevels(@Param("dimensionId") String dimensionId, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查成员编码是否存在
     * @param memberCode 成员编码
     * @param dimensionId 维度ID
     * @param excludeId 排除的成员ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_DIMENSION_MEMBER WHERE MEMBER_CODE = #{memberCode} AND DIMENSION_ID = #{dimensionId} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkMemberCodeExists(@Param("memberCode") String memberCode, @Param("dimensionId") String dimensionId, 
                             @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查是否存在循环引用
     * @param memberId 成员ID
     * @param parentMemberId 父成员ID
     * @param tenantId 租户ID
     * @return 是否存在循环引用
     */
    boolean checkCircularReference(@Param("memberId") String memberId, @Param("parentMemberId") String parentMemberId, @Param("tenantId") String tenantId);

    /**
     * 检查成员是否有子节点
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 是否有子节点
     */
    @Select("SELECT COUNT(*) FROM BUDGET_DIMENSION_MEMBER WHERE PARENT_MEMBER_ID = #{memberId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkHasChildren(@Param("memberId") String memberId, @Param("tenantId") String tenantId);
}
