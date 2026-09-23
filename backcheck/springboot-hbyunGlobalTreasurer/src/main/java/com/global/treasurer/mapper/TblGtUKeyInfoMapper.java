package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGtUKeyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

import java.util.List;
import java.util.Map;

/**
 * 全球司库-U盾信息Mapper接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Mapper
public interface TblGtUKeyInfoMapper extends BaseMapper<TblGtUKeyInfo> {

    /**
     * 分页查询U盾信息列表
     *
     * @param orgId 组织ID
     * @param holderName 账户名称
     * @param ukeyNo U盾编号
     * @param ukeyStatus U盾状态
     * @return U盾信息列表
     */
    List<TblGtUKeyInfo> selectPageList(
            @Param("orgId") BigDecimal orgId,
            @Param("ukeyId") Long ukeyId,
            @Param("holderName") String holderName,
            @Param("ukeyNo") String ukeyNo,
            @Param("ukeyStatus") String ukeyStatus
    );

    /**
     * 按状态统计U盾数量
     */
    List<Map<String, Object>> selectStatusCount(@Param("orgId") BigDecimal orgId);
}
