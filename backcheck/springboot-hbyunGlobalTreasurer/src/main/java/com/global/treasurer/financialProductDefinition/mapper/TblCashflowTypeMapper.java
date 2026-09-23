package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.financialProductDefinition.entity.TblCashflowType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 现金流类型Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblCashflowTypeMapper extends BaseMapper<TblCashflowType> {

    /**
     * 根据父级ID查询子级列表
     *
     * @param parentId 父级ID
     * @param orgId 组织ID
     * @return 子级列表
     */
    List<TblCashflowType> selectByParentId(@Param("parentId") Long parentId, @Param("orgId") Long orgId);

    /**
     * 查询所有启用的现金流类型
     *
     * @param orgId 组织ID
     * @return 启用的现金流类型列表
     */
    List<TblCashflowType> selectEnabledList(@Param("orgId") Long orgId);

    /**
     * 检查编码是否唯一
     *
     * @param cashflowTypeCode 编码
     * @param excludeId 排除的ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM TBL_CASHFLOW_TYPE " +
            "WHERE CASHFLOW_TYPE_CODE = #{cashflowTypeCode} " +
            "<if test='excludeId != null'>" +
            "AND CASHFLOW_TYPE_ID != #{excludeId} " +
            "</if>" +
            "</script>")
    int checkCodeUnique(@Param("cashflowTypeCode") String cashflowTypeCode, @Param("excludeId") Long excludeId);

    /**
     * 批量更新排序
     *
     * @param list 排序列表
     * @return 影响行数
     */
    int batchUpdateSort(@Param("list") List<TblCashflowType> list);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param isEnabled 状态
     * @param updateBy 更新人
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("updateBy") String updateBy);

    /**
     * 查询使用该类型的记录数
     *
     * @param cashflowTypeId 现金流类型ID
     * @return 使用数量
     */
    @Select("SELECT 0 FROM DUAL")
    int countUsage(@Param("cashflowTypeId") Long cashflowTypeId);

    /**
     * 获取统计信息
     *
     * @param orgId 组织ID
     * @return 统计信息
     */
    List<TblCashflowType> getStatistics(@Param("orgId") Long orgId);
}

