package com.huabo.contract.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblYyXdfCompany;
import com.huabo.contract.mappersql.TblYyXdfCompanyMapperSqlConifg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-07
 */
public interface TblYyXdfCompanyMapper extends BaseMapper<TblYyXdfCompany> {

	@SelectProvider(method="selectListByPageInfo",type=TblYyXdfCompanyMapperSqlConifg.class)
    @Results({
            @Result(column="REALNAME",property="staff.realname")
    })
	IPage<TblYyXdfCompany> selectListByPageInfo(IPage<TblYyXdfCompany> page, TblYyXdfCompany company) throws Exception;
	
	@SelectProvider(method="findBySqlPage",type=TblYyXdfCompanyMapperSqlConifg.class)
    @Results({
            @Result(column="REALNAME",property="staff.realname")
    })
    List<TblYyXdfCompany> findBySqlPage(IPage<TblYyXdfCompany> page, TblYyXdfCompany company) throws Exception;

	@Select("SELECT count(*) from TBL_YY_COMPANY where teamid= #{teamid}")
    Integer listBySqlPageCount(BigDecimal teamid) throws Exception;


}
