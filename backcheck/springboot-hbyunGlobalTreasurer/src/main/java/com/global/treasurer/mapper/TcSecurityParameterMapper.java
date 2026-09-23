package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcSecurityParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 安全参数配置Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Mapper
public interface TcSecurityParameterMapper extends BaseMapper<TcSecurityParameter> {

    /**
     * 分页查询安全参数列表
     * @param params 查询参数
     * @return 安全参数列表
     */
    List<TcSecurityParameter> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询安全参数详情
     * @param id 主键ID
     * @return 安全参数详情
     */
    TcSecurityParameter selectDetailById(@Param("id") Long id);

    /**
     * 根据参数编码查询
     * @param paramCode 参数编码
     * @return 安全参数
     */
    TcSecurityParameter selectByParamCode(@Param("paramCode") String paramCode);

    /**
     * 统计安全参数数量
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);
}

