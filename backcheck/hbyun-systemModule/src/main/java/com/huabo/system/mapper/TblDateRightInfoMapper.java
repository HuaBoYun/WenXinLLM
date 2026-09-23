package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblDateRightInfo;

/**
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblDateRightInfoMapper extends BaseMapper<TblDateRightInfo> {
	

	@Select("SELECT TSD.ROLEID,TSD.ORGID,TSD.DEPTIDSTRS,ORG.ORGNAME,ORG.ORGID FROM TBL_SYSTEM_DATA_RIGHT TSD LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TSD.ORGID WHERE TSD.ROLEID = #{roleId}")
	@Results({
		@Result(column="ROLEID",property="roleId"),
		@Result(column="ORGID",property="orgId"),
		@Result(column="DEPTIDSTRS",property="deptIdStrs"),
		@Result(column="ORGNAME",property="orgname"),
	})
	List<TblDateRightInfo> selectDateRightInfoByRoleId(BigDecimal roleId) throws Exception;
}
