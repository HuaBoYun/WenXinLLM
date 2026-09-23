package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.GuaranteeQueryDTO;
import com.global.treasurer.entity.TblGuarantee;
import com.global.treasurer.vo.GuaranteeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保函Mapper接口
 *
 * 数据库表 TBL_GUARANTEE 主键 GUARANTEE_ID 为 VARCHAR2(10) 类型
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface GuaranteeMapper extends BaseMapper<TblGuarantee> {

    /**
     * 查询保函列表
     *
     * @param queryDTO 查询条件
     * @return 保函列表
     */
    List<GuaranteeVO> selectGuaranteeList(GuaranteeQueryDTO queryDTO);

    /**
     * 根据ID查询保函详情
     *
     * @param guaranteeId 保函ID (VARCHAR2类型)
     * @return 保函详情
     */
    GuaranteeVO selectGuaranteeById(@Param("guaranteeId") String guaranteeId);

    /**
     * 批量删除保函
     *
     * @param guaranteeIds 保函ID数组 (VARCHAR2类型)
     * @return 影响行数
     */
    int deleteGuaranteeByIds(@Param("guaranteeIds") String[] guaranteeIds);
}

