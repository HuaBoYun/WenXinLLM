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
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTestplanVo;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblTesttempleVo;
import com.huabo.monitor.entity.Tree;

 
public interface TblGroupTestplanMapper extends   BaseMapper<TblGroupTestplan> {

List<TblGroupTestplan> findAllNew(@Param("queryParam")TblGroupTestplan queryParam,@Param("sql")String  sql);
	
List<TblGroupTestplan> findAllnoSjNew(@Param("queryParam")TblGroupTestplan queryParam,@Param("sql")String  sql);


@Select("select * from TBL_GROUP_TESTPLAN where id=#{id}")
@Results(id="TblGroupTestplan",value={
        @Result(property = "id",column = "id",id = true),
        @Result(property = "testtemid",column = "testtemid"),
        @Result(property = "orgid",column = "orgid"),
        @Result(property = "creatid",column = "creatid"),
        @Result(property = "staffid",column = "staffid"),
        @Result(property = "testtemple",column = "testtemid",one=@One(select="com.huabo.monitor.mapper.TblTesttempleMapper.selectById")),

})
TblGroupTestplan getOneTblGroupTestplan(BigDecimal id);

@Delete("DELETE FROM TBL_GROUP_TESTPLAN where ID=#{id}")
void deleteTestPlan(@Param("id")BigDecimal id);

@Delete("DELETE FROM TBL_GROUPTESTPLAN_ATT where attid=#{attid}")
void deleteAtt(BigDecimal attid);


}
