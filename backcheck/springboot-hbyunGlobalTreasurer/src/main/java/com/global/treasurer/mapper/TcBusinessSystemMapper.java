package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcBusinessSystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务系统注册Mapper接口
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@Mapper
public interface TcBusinessSystemMapper extends BaseMapper<TcBusinessSystem> {

    /**
     * 根据条件查询业务系统列表(支持动态条件)
     *
     * @param systemCode 系统编码(模糊查询)
     * @param systemName 系统名称(模糊查询)
     * @param systemType 系统类型(精确查询)
     * @param connectionStatus 连接状态(精确查询)
     * @param status 状态(精确查询)
     * @return 业务系统列表
     */
    List<TcBusinessSystem> selectByCondition(@Param("systemCode") String systemCode,
                                             @Param("systemName") String systemName,
                                             @Param("systemType") String systemType,
                                             @Param("connectionStatus") String connectionStatus,
                                             @Param("status") String status);
}