package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblAssessTargetMySql;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
public interface TblAssessTargetMySqlMapper extends BaseMapper<TblAssessTargetMySql> {

    @SelectProvider(type = TblAssessTargetMapperSqlMySqlConfig.class, method = "MyMark")
    List<TblAssessTargetMySql> MyMark(BigDecimal staffid, String assid, String assName, PageInfo<TblAssessTargetMySql> pageInfo);

    @SelectProvider(type = TblAssessTargetMapperSqlMySqlConfig.class, method = "MyMarkCount")
    Integer MyMarkCount(BigDecimal staffid, String assid, String assName);
}
