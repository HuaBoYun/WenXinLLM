package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcSealArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TcSealArchive Mapper接口
 * 支持PageHelper分页
 * 继承BaseMapper但使用自定义方法避免selectOne异常
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@Mapper
public interface TcSealArchiveMapper extends BaseMapper<TcSealArchive> {

    /**
     * 带条件查询列表
     * @param condition SQL条件语句 (WHERE部分,不包含WHERE关键字)
     * @param params 参数数组
     * @return 列表数据
     */
    List<TcSealArchive> selectListWithCondition(@Param("condition") String condition, @Param("params") Object[] params);

    /**
     * 统计记录数 - 使用自定义SQL避免selectOne异常
     * @param condition SQL条件语句
     * @param params 参数数组
     * @return 记录数
     */
    Long selectCountByCondition(@Param("condition") String condition, @Param("params") Object[] params);
}
