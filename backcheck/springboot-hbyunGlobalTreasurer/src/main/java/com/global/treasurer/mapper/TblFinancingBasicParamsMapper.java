package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingBasicParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 融资基础参数Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@Mapper
public interface TblFinancingBasicParamsMapper extends BaseMapper<TblFinancingBasicParams> {

    /**
     * 根据条件查询融资基础参数列表
     */
    List<TblFinancingBasicParams> selectByCondition(@Param("paramName") String paramName,
                                                     @Param("paramType") String paramType,
                                                     @Param("isEnabled") String isEnabled);
}

