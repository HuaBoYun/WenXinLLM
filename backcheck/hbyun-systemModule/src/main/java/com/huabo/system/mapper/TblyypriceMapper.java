package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.Tblyyprice;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;


import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblyypriceMapper extends BaseMapper<Tblyyprice> {


    @SelectProvider(method="selectListByPageInfo",type=TblyypriceMapperSqlConifg.class)
    IPage<Tblyyprice> selectListByPageInfo(IPage<Tblyyprice> page, @Param("find") Find find, @Param("orgid")BigDecimal orgid);
}
