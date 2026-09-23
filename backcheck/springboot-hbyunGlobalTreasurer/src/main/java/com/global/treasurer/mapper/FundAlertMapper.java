package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.FundAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金预警Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Mapper
public interface FundAlertMapper extends BaseMapper<FundAlert> {

    /**
     * 分页查询资金预警列表
     * @param params 查询参数
     * @return 资金预警列表
     */
    List<FundAlert> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询资金预警详情
     * @param id 主键ID
     * @return 资金预警详情
     */
    FundAlert selectDetailById(@Param("id") Long id);

    /**
     * 统计资金预警数量
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);
}

