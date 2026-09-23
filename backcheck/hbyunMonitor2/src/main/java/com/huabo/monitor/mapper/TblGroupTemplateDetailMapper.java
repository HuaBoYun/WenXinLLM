package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import com.huabo.monitor.vo.result.TestTrackingStatisticsResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblAssesscategory;
import com.huabo.monitor.entity.TblGroupTemplateDetail;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTestplanVo;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblTesttempleVo;
import com.huabo.monitor.entity.Tree;

 
public interface TblGroupTemplateDetailMapper extends   BaseMapper<TblGroupTemplateDetail> {
 
	
	@Select("select distinct g.*,t.typecode,t.typename,s.realname from TBL_GROUP_TEMPLATEDETAIL g left join TBL_TESTTEMPL_TYPE t on   g.typeid=t.typeid  left join tbl_staff s on s.staffid=g.staffid where  groupid=#{id}  order by id")
	 List<TblGroupTemplateDetail> getListByGroupid(@Param("id")BigDecimal id);
	
	@Select("select distinct g.*,t.typecode,t.typename,s.realname from TBL_GROUP_TEMPLATEDETAIL g left join TBL_TESTTEMPL_TYPE t on   g.typeid=t.typeid left join tbl_staff s on s.staffid=g.staffid where  groupid=#{id}  order by id")
	 List<TblGroupTemplateDetail> getListByStaffid(@Param("id")BigDecimal id,@Param("staffid")BigDecimal staffid);

	@Delete("delete from TBL_GROUP_TEMPLATEDETAIL where groupid=#{id}")
	void delGroupTemplateDetail(@Param("id")BigDecimal id);
	
	
}
