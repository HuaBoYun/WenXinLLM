package com.huabo.compliance.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblAssesslevel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesslevelMapper extends BaseMapper<TblAssesslevel> {


    @Select("select * from TBL_ASSESSLEVEL t where t.levelupper >= #{fen} and TBLCOMANY= #{orgid} order by t.levelupper desc")
    List<TblAssesslevel> getRegion(@Param("fen")Double fen,@Param("orgid")BigDecimal orgid);
   
    
    @SelectProvider(type=TblAssesslevelMapperSqlConfig.class,method="selectPageInfo")
	List<TblAssesslevel> selectPageInfo(PageInfo<TblAssesslevel> pageInfo) throws Exception;

    
    @SelectProvider(type=TblAssesslevelMapperSqlConfig.class,method="selectPageCount")
   	Integer selectPageCount(PageInfo<TblAssesslevel> pageInfo) throws Exception;

}
