package com.huabo.system.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblHomePageModel;
import com.huabo.system.mappersql.TblHomePageModelMapperSqlConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblHomePageModelMapper extends BaseMapper<TblHomePageModel> {

    @SelectProvider(type=TblHomePageModelMapperSqlConfig.class,method="homePageModels")
    IPage<TblHomePageModel> homePageModels(IPage<TblHomePageModel> page, BigDecimal staffid);
    
}
