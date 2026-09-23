package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjOuterruleEntity;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;

public interface TblNbsjOuterruleMapper extends BaseMapper<TblNbsjOuterruleEntity>{
	
	  @SelectProvider(method="selectOuterruleListView",type=TblNbsjOuterruleMapperSqlConfig.class)
	     @Results({
	     	@Result(column="OUTRULID",property="outrulid"),
	     	@Result(column="RULENUMBER",property="rulenumber"),
	     	@Result(column="RULENAME",property="rulename"),
	     	@Result(column="PUBLISHORG",property="publishorg"),
	     	@Result(column="EFFECTIVELEVEL",property="effectivelevel"),
	     	@Result(column="TIMELINESS",property="timeliness"),
	     	@Result(column="PUBLISHDATE",property="publishdate"),
	     	@Result(column="TAKEEFFECTTIME",property="takeeffecttime"),
	     })
		List<TblNbsjOuterruleEntity> selectOuterruleListView(PageInfo<TblNbsjOuterruleEntity> pageInfo) throws Exception;

	  @SelectProvider(method="selectOuterruleCountView",type=TblNbsjOuterruleMapperSqlConfig.class)
		Integer selectOuterruleCountView(PageInfo<TblNbsjOuterruleEntity> pageInfo) throws Exception;
      
	  @Select("select * from TBL_NBSJ_OUTERRULE where outrulid =#{id}")
	  TblNbsjOuterruleEntity findById(@Param("id")String id) throws Exception;

	  @Delete("delete from TBL_NBSJ_OUTERRULE where outrulid =#{id}")
	  void deleteById(@Param("id")String id) throws Exception;
	  
	  
		@InsertProvider(type=TblNbsjOuterruleMapperSqlConfig.class,method="insertEntity")
		@Options(useGeneratedKeys=true, keyProperty="outrulid", keyColumn="OUTRULID")
		void insertEntity(TblNbsjOuterruleEntity outer) throws Exception;

		@UpdateProvider(type=TblNbsjOuterruleMapperSqlConfig.class,method="updateEntity")
		void updateEntity(TblNbsjOuterruleEntity outer) throws Exception;

		 @Insert("INSERT INTO TBL_NBSJ_OUTERRULE_ATT(OUTRULID,ATTID) VALUES (#{outid},#{attid})")
			void insertAttInfoForPlan(@Param("outid")BigDecimal outid,@Param("attid") String attid) throws Exception;
		 
		 @Select("SELECT ATTID FROM  TBL_NBSJ_OUTERRULE_ATT WHERE OUTRULID=#{outid} ")
			String selectOuterAtt(@Param("outid")String outid) throws Exception;
		 
		 
		 @Delete("DELETE FROM TBL_NBSJ_OUTERRULE_ATT WHERE attid=#{attid}")
			void deleteFileInfoByAttId(Integer attid);
}
