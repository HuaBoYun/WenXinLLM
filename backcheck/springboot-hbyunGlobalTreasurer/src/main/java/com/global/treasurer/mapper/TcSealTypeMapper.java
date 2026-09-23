package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcSealType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 印鉴类型Mapper接口
 *
 * @author 华博云开发团队
 * @since 2024-12-24
 */
public interface TcSealTypeMapper extends BaseMapper<TcSealType> {

    /**
     * 根据条件查询印鉴类型列表
     *
     * @param params 查询参数
     * @return 印鉴类型列表
     */
    List<TcSealType> selectListByConditions(@Param("params") Map<String, Object> params);
}
