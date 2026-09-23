package com.huabo.finance.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BaseCommonMapper {

	@Select("SELECT REALNAME FROM TBL_STAFF WHERE STAFFID = #{id}")
	String selectUserNameById(@Param("id")String id) throws Exception;

	@Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID = #{id}")
	String selectOrgNameById(@Param("id")String id) throws Exception;

}
