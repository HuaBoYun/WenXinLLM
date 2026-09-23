package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.RiskCoping;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface RiskCopingMapper extends BaseMapper<RiskCoping> {
	@Insert("insert into TBL_RISK_COPINGATT(ATTID, riskcopingid) values (#{attid}, #{riskcopingid})")
    int insertRCAtt(@Param("riskcopingid") BigDecimal riskcopingid, @Param("attid") BigDecimal attid);

    @Delete("DELETE from TBL_RISK_COPINGATT WHERE riskcopingid= #{riskcopingid} ")
    int deleteRCAtt(@Param("riskcopingid") BigDecimal riskcopingid);

    @Select("SELECT COUNT(0) FROM TBL_RISK_COPING WHERE RISKID = #{riskid}")
	Object selectRiskCopyingCountByRiskId(BigDecimal riskid) throws Exception;
    
    @Delete("DELETE from  TBL_RISK_COPING WHERE riskid= #{riskid} ")
    int deleteRiskCoping(@Param("riskid") String riskid);
    
    @Select("select  o.orgid,o.orgname  from (select UNIT from TBL_RISK_COPING where UNIT is not null  group by UNIT) t left join tbl_organization o on o.orgid=t.UNIT")
	List<Organization> getCopingCompanyList();
    
    @Select("select count(1) from TBL_RISK_COPING where unit=#{orgid} and year(createdate)=#{year}")
    Integer getCopingCountByCompanyYear(@Param("orgid")BigDecimal orgid,@Param("year")String year);

    
    
}
