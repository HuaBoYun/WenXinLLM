package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblUserOrgRelation;


public interface TblUserOrgRelationMapper extends BaseMapper<TblUserOrgRelation> {

	@Delete("DELETE FROM TBL_USER_ORGRELATION WHERE STAFFID = #{staffId}")
	void deleteRelationByStaffId(BigDecimal staffId);

	@SelectProvider(method="selectUserOrgRelationInfoListByStaffId",type=TblUserOrgRelationMapperSqlConfig.class)
	List<TblUserOrgRelation> selectUserOrgRelationInfoListByStaffId(BigDecimal staffid, String orgname) throws Exception;

	@Select("SELECT ORGYMSTRIDS FROM TBL_USER_ORGRELATION WHERE ROWNUM = 1 AND DEPTID = #{orgid}")
	String selectUniquePkYmIdsByOrgIds(BigDecimal orgid) throws Exception;

	@Select("SELECT ORG.ORGNAME FROM TBL_USER_ORGRELATION TUO LEFT JOIN TBL_ORGANIZATION ORG ON TUO.DEPTID = ORG.ORGID WHERE TUO.STAFFID = #{staffid} AND NUMNO != 0")
	List<String> selectDeptNamesByStaffId(BigDecimal staffid) throws Exception;

	@Delete("DELETE FROM TBL_USER_ORGRELATION WHERE STAFFID = #{yjstaff} AND DEPTID IN (${orgIdStrs})")
	void deleteRelationByStaffIdDeptId(@Param("yjstaff")BigDecimal yjstaff,@Param("orgIdStrs") String orgIdStrs) throws Exception;

}
