package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblRiskReview;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblRiskReviewOracleMapper extends BaseMapper<TblRiskReview> {
	
	
	List<TblRiskReview> getQueryList(@Param("queryParam")TblRiskReview queryParam,@Param("authorityType")Integer authorityType,@Param("sql")String  sql);
	
	TblRiskReview getDetalById(@Param("id")String id);
	
	@Select("select o.orgid,o.orgname  from (select staffunit from TBL_RISK_REVIEW where staffunit is not null and staffunit !='' group by staffunit) t left join tbl_organization o on o.orgid=t.staffunit")
	List<Organization> getReviewCompanyList();
	
	@Select("select count(1) from TBL_RISK_REVIEW where state=6 and staffunit=#{orgid} and year(createtime)=#{year}")
	Integer getReviewByCompanyYear(@Param("orgid")BigDecimal orgid,@Param("year")String year);
	
	@Select("select count(1) from TBL_RISK_REVIEW where state=6 and staffunit=#{orgid} ")
	Integer getReviewByCompany(@Param("orgid")BigDecimal orgid);
}
