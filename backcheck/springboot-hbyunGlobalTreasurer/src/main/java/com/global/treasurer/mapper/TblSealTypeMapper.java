package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSealType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 印鉴类型Mapper接口
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
@Mapper
public interface TblSealTypeMapper extends BaseMapper<TblSealType> {

    /**
     * 根据条件查询印鉴类型列表（支持PageHelper分页）
     *
     * @param name 类型名称（模糊查询）
     * @param sealLevel 印鉴级别
     * @param status 状态
     * @return 印鉴类型列表
     */
    List<TblSealType> selectByCondition(@Param("name") String name,
                                         @Param("sealLevel") String sealLevel,
                                         @Param("status") Integer status);

    /**
     * 查询启用的印鉴类型列表
     *
     * @return 印鉴类型列表
     */
    List<TblSealType> selectActiveTypes();
}
