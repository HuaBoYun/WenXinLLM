package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BillIdentificationQueryDTO;
import com.global.treasurer.entity.TblBillIdentification;
import com.global.treasurer.vo.BillIdentificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据标识Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@Mapper
public interface BillIdentificationMapper extends BaseMapper<TblBillIdentification> {

    /**
     * 查询票据标识列表
     *
     * @param queryDTO 查询条件
     * @return 票据标识列表
     */
    List<BillIdentificationVO> selectBillIdentificationList(@Param("query") BillIdentificationQueryDTO queryDTO);

    /**
     * 根据ID查询票据标识详情
     *
     * @param identificationId 标识ID
     * @return 票据标识详情
     */
    BillIdentificationVO selectBillIdentificationById(@Param("identificationId") Long identificationId);

    /**
     * 根据标识编码查询标识
     *
     * @param identificationCode 标识编码
     * @return 标识信息
     */
    TblBillIdentification selectByIdentificationCode(@Param("identificationCode") String identificationCode);

    /**
     * 批量删除票据标识(逻辑删除)
     *
     * @param identificationIds 标识ID数组
     * @return 影响行数
     */
    int deleteBillIdentificationByIds(@Param("identificationIds") Long[] identificationIds);

    /**
     * 查询子级标识列表
     *
     * @param parentId 父级标识ID
     * @return 子级标识列表
     */
    List<BillIdentificationVO> selectChildrenByParentId(@Param("parentId") Long parentId);

    /**
     * 根据类型查询标识列表
     *
     * @param identificationType 标识类型
     * @return 标识列表
     */
    List<BillIdentificationVO> selectByType(@Param("identificationType") String identificationType);

    /**
     * 批量更新启用状态
     *
     * @param identificationIds 标识ID数组
     * @param isEnabled 启用状态
     * @return 影响行数
     */
    int batchUpdateEnabled(@Param("identificationIds") Long[] identificationIds, @Param("isEnabled") Integer isEnabled);

    /**
     * 检查标识编码是否存在
     *
     * @param identificationCode 标识编码
     * @param excludeId 排除的ID（用于更新时排除自身）
     * @return 数量
     */
    int checkCodeExists(@Param("identificationCode") String identificationCode, @Param("excludeId") Long excludeId);
}

