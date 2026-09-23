package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.Find;
import com.huabo.monitor.mysql.entity.TblyypriceMySql;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblyypriceMySqlMapper extends BaseMapper<TblyypriceMySql> {


    @SelectProvider(method = "selectListByPageInfo", type = TblyypriceMapperSqlMySqlConifg.class)
    List<TblyypriceMySql> selectListByPageInfo(PageInfo<TblyypriceMySql> pageInfo, @Param("find") Find find, @Param("orgid") BigDecimal orgid);

    @SelectProvider(method = "selectCountByPageInfo", type = TblyypriceMapperSqlMySqlConifg.class)
    Integer selectCountByPageInfo(PageInfo<TblyypriceMySql> pageInfo, @Param("find") Find find, @Param("orgid") BigDecimal orgid);
}
