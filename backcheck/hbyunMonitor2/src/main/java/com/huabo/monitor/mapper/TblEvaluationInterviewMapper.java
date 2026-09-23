package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.monitor.vo.param.MaxNumberParam;
import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblAssEleCategory;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessPlan;
import com.huabo.monitor.entity.TblAssessPlanVo;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblEvaluateDefects;
import com.huabo.monitor.entity.TblEvaluationInterview;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.mapper.Provider.TblAssessProvider;

@Mapper
public interface TblEvaluationInterviewMapper extends BaseMapper<TblEvaluationInterview> {

	 
	List<TblEvaluationInterview> getHomepage_List(@Param("param")TblEvaluationInterview param,@Param("authorityType")Integer authorityType,@Param("sql")String  sql);
	
	@Select("select e.*,s.REALNAME as staffname ,o.orgname as  deptname from  TBL_EVALUATIONINTERVIEW e left join tbl_staff s on s.STAFFID=e.STAFFID left join tbl_organization o on o.orgid=e.dept where id=#{id} ")
	TblEvaluationInterview getDetatilsById(@Param("id")BigDecimal id);
	
	@Insert("insert into TBL_INTERVIEW_ATT(attid,id) values(#{attid},#{id})")
	void insertAtt(String attid,BigDecimal id);
	
	@Select("select * from TBL_ATTACHMENT where attid in (select attid from TBL_INTERVIEW_ATT where id=#{id})")
	List<TblAttachment> getAttList(@Param("id")BigDecimal id);
	
	
	@Delete("delete from TBL_INTERVIEW_ATT  where id=#{id}")
	void removeAttById(@Param("id")BigDecimal id); 
	
	@Delete("delete from TBL_INTERVIEW_ATT  where attid=#{attid}")
	void removeAttByAttid(@Param("attid")BigDecimal attid); 
	
	
}
