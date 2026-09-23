package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.huabo.audit.oracle.entity.TblNbsjStaffSelect;

public interface TblNbsjStaffSelectMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjStaffSelect> {

	@Select("SELECT SELECTID,PROJECTID,STAFFID FROM TBL_NBSJ_STAFFSELECT WHERE STAFFID = #{staffid} AND ROWNUM = 1")
	@Results({
		@Result(column="SELECTID",property="selectId"),
		@Result(column="PROJECTID",property="projectId"),
		@Result(column="STAFFID",property="staffId"),
	})
	TblNbsjStaffSelect selectByUserId(BigDecimal staffid) throws Exception;

	@Delete("DELETE FROM TBL_NBSJ_STAFFSELECT WHERE STAFFID = #{staffid}")
	void deleteNbsjByStaff(BigDecimal staffid) throws Exception;

	@Insert("INSERT INTO TBL_NBSJ_STAFFSELECT(SELECTID, PROJECTID, STAFFID) VALUES (HIBERNATE_SEQUENCE.nextval, #{projectId} , #{staffid} )")
	void insertEntity(BigDecimal staffid, BigDecimal projectId);

	@Select("SELECT PROJECTID FROM TBL_NBSJ_STAFFSELECT WHERE STAFFID = #{staffid}")
	BigDecimal selectProjectIdByStaffId(BigDecimal staffid) throws Exception;
	
	@Delete("DELETE FROM TBL_NBSJ_STAFFSELECT WHERE PROJECTID = #{projectId}")
	void deleteNbsjByProject(BigDecimal projectId) throws Exception;
	
	
}
