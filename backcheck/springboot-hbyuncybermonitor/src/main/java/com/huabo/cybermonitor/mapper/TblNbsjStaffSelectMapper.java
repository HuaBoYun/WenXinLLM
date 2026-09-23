package com.huabo.cybermonitor.mapper;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

public interface TblNbsjStaffSelectMapper {


	@Select("SELECT PROJECTID FROM TBL_NBSJ_STAFFSELECT WHERE STAFFID = #{staffid}")
	Integer selectProjectIdByStaffId(BigDecimal staffid) throws Exception;
	
	
}
