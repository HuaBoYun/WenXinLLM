package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblAssessTarget;
import com.huabo.system.mappersql.TblAssessTargetMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblAssessTargetMapper extends BaseMapper<TblAssessTarget> {

    @SelectProvider(type=TblAssessTargetMapperSqlConfig.class,method="MyMark")
    IPage<TblAssessTarget> MyMark(BigDecimal staffid, String assid, String assName, IPage<TblAssessTarget> page);
    
}
