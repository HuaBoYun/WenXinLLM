package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.AssessmentIndicator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 考核指标Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface AssessmentIndicatorMapper extends BaseMapper<AssessmentIndicator> {

    /**
     * 查询启用的指标列表
     *
     * @param isActive 是否启用
     * @param parentId 父指标ID
     * @return 指标列表
     */
    @Select("<script>" +
            "SELECT * FROM assessment_indicator " +
            "WHERE is_active = #{isActive} " +
            "<if test='parentId != null'> AND parent_id = #{parentId} </if>" +
            "<if test='parentId == null'> AND parent_id IS NULL </if>" +
            "ORDER BY sort_order ASC, id ASC" +
            "</script>")
    List<AssessmentIndicator> selectActiveIndicators(@Param("isActive") Integer isActive, 
                                                   @Param("parentId") Long parentId);

    /**
     * 查询指标树形结构
     *
     * @param isActive 是否启用
     * @return 指标列表
     */
    @Select("SELECT * FROM assessment_indicator WHERE is_active = #{isActive} ORDER BY sort_order ASC, id ASC")
    List<AssessmentIndicator> selectIndicatorTree(@Param("isActive") Integer isActive);

    /**
     * 根据指标编码查询指标
     *
     * @param indicatorCode 指标编码
     * @return 指标信息
     */
    @Select("SELECT * FROM assessment_indicator WHERE indicator_code = #{indicatorCode} AND is_active = 1")
    AssessmentIndicator selectByIndicatorCode(@Param("indicatorCode") String indicatorCode);

    /**
     * 查询子指标列表
     *
     * @param parentId 父指标ID
     * @return 子指标列表
     */
    @Select("SELECT * FROM assessment_indicator WHERE parent_id = #{parentId} AND is_active = 1 ORDER BY sort_order ASC")
    List<AssessmentIndicator> selectChildrenByParentId(@Param("parentId") Long parentId);

    /**
     * 检查指标编码是否存在
     *
     * @param indicatorCode 指标编码
     * @param excludeId 排除的ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM assessment_indicator " +
            "WHERE indicator_code = #{indicatorCode} " +
            "<if test='excludeId != null'> AND id != #{excludeId} </if>" +
            "</script>")
    int countByIndicatorCode(@Param("indicatorCode") String indicatorCode, @Param("excludeId") Long excludeId);
}
