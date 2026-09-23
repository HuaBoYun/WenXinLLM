package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.InternalLoanApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 内部借款申请Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Mapper
public interface InternalLoanApplicationMapper extends BaseMapper<InternalLoanApplication> {

    /**
     * 分页查询内部借款申请列表
     * @param params 查询参数
     * @return 内部借款申请列表
     */
    List<InternalLoanApplication> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询内部借款申请详情
     * @param id 主键ID
     * @return 内部借款申请详情
     */
    InternalLoanApplication selectDetailById(@Param("id") Long id);

    /**
     * 统计内部借款申请数量
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);
}

