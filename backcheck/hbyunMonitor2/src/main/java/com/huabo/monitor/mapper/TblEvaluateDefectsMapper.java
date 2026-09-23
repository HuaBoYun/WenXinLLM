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
import com.huabo.monitor.entity.TblDefectAtt;
import com.huabo.monitor.entity.TblEvaluateDefects;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.mapper.Provider.TblAssessProvider;

@Mapper
public interface TblEvaluateDefectsMapper extends BaseMapper<TblEvaluateDefects> {

	
	
	List<TblEvaluateDefects> getHomepage_List(@Param("param")TblEvaluateDefects param,@Param("authorityType")Integer authorityType);
	
	@Select("select e.*,s.REALNAME as createstaffname from TBL_EVALUATEDEFECTS e left join tbl_staff s on s.STAFFID=e.CREATESTAFFID where id=#{id} ")
	TblEvaluateDefects getDetatilsById(@Param("id")BigDecimal id);
	
	@Insert("insert into TBL_DEFECT_ATT(attid,id) values(#{attid},#{id})")
	void insertAtt(@Param("attid")BigDecimal attid,@Param("id")BigDecimal id);
	
	
	@Select("select * from TBL_ATTACHMENT where attid in (select attid from TBL_DEFECT_ATT where id=#{id})")
	List<TblAttachment> getAttList(@Param("id")BigDecimal id);
	
	@Delete("delete from TBL_DEFECT_ATT  where id=#{id}")
	void removeAttById(@Param("id")BigDecimal id); 
	
	@Delete("delete from TBL_DEFECT_ATT  where attid=#{attid}")
	void removeAttByAttid(@Param("attid")BigDecimal attid); 
}
