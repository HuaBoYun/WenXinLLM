package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblHomePageModelMySql;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblHomePageModelMySqlMapper extends BaseMapper<TblHomePageModelMySql> {

    @SelectProvider(type = TblHomePageModelMapperSqlConfig.class, method = "homePageModels")
    List<TblHomePageModelMySql> homePageModels(PageInfo<TblHomePageModelMySql> pageInfo, BigDecimal staffid);

    @SelectProvider(type = TblHomePageModelMapperSqlConfig.class, method = "homePageModelsCount")
    Integer homePageModelsCount(BigDecimal staffid);
}
