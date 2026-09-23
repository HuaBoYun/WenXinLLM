package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Riskevent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Repository
public interface RiskeventMapper extends BaseMapper<Riskevent> {
   
	//总公司
	List<Riskevent> getRiskeventPageInfoHead(@Param("queryParam")Riskevent queryParam,@Param("childNodeIds")List<BigDecimal> childNodeIds,@Param("sql") String sql);
    //子公司
	List<Riskevent> getRiskeventPageInfo(@Param("queryParam")Riskevent queryParam,@Param("childNodeIds")List<BigDecimal> childNodeIds,@Param("sql")String sql);
	
	List<Riskevent> getRiskeventHistoryHead(@Param("queryParam")Riskevent queryParam);
	
	List<Riskevent> getRiskeventHistory(@Param("queryParam")Riskevent queryParam);

    List<Riskevent> getRemindList(@Param("staffid")BigDecimal staffid);
	
    
    List<Map<String, Object>> getRiskLosseventcategory(@Param("company")String company);
	
	@Select("select o.orgname name,a.num  from (select unit ,count(1) as num from TBL_RISKEVENT  group by unit ) a left join tbl_organization o on a.unit=o.orgid ")
	List<Map<String, Object>> getRiskeventCountByCompany();
    
    
	@Select("select r.RISEVEID,r.RISKEVENTCODE,r.RISKEVENTNAME,rc.PRICENUMBER,r.OCCUREDDATE,r.RISKEVENTDESCRIPTION from TBL_RISKEVENT r left join TBL_RISK_CLAIM rc on r.RISEVEID = rc.CLAIMNUMBER " +
            " where r.RISEVEID in (select riseveid from tbl_risk_riskevent where riskid = #{param1} )")
    List<Riskevent> selectRiskeventByRiskId(String riskid);
	
	
	
    
 
	@Delete("DELETE FROM TBL_RISKEVENT_ATT WHERE RISEVEID=#{riskId}")
	void deleteAttchMent(Integer riskId);

	@Insert("INSERT INTO TBL_RISKEVENT_ATT(RISEVEID,ATTID) VALUES(#{riskId},#{id})")
	void saveAttchMent(String id, BigDecimal riskId);


	@Select("SELECT COUNT(1) FROM TBL_RISKEVENT WHERE losseventcategory='1' and UNIT=#{orgid}")
    Integer queryRisksNumberYiBan(BigDecimal orgid);

	@Select("SELECT COUNT(1) FROM TBL_RISKEVENT WHERE losseventcategory='2' and UNIT=#{orgid}")
	Integer queryRisksNumberZhongDa(BigDecimal orgid);
	
	
	Integer queryRisksNumberByType(@Param("orgid")BigDecimal orgid,@Param("type")String type,@Param("year")String year);

//	by 20240319 多数据融合改造
//	@Select("SELECT MAX(TO_NUMBER(SUBSTR(riskeventcode,INSTR(riskeventcode,'-',-1)+1)))  "
//			+ " FROM TBL_RISKEVENT "
//			+ " WHERE riskeventcode LIKE ${riskeventcode} ")
	@Select("SELECT  MAX(TO_NUMBER(SUBSTR(riskeventcode,INSTR(riskeventcode,'-',-1)+1))) "
			+ " FROM TBL_RISKEVENT "
			+ " WHERE riskeventcode LIKE ${riskeventcode} ")
	Integer get_risksj_no(String riskeventcode) throws Exception;
	
	@Select("select  max(version) from  TBL_RISKEVENT where riskeventcode LIKE ${riskeventcode} ")
	Integer getMaxVersion(String riskeventcode) throws Exception;
	
	@Select("select  o.orgid,o.orgname  from (select UNIT from TBL_RISKEVENT where UNIT is not null and UNIT !='' group by UNIT) t left join tbl_organization o on o.orgid=t.UNIT")
	List<Organization> getEventCompanyList();
	

}
