package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.util.Tree;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblOrganizationMapper extends BaseMapper<TblOrganization> {

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{nodeId} AND STATUS = 0")
	List<Tree> getNodes(BigDecimal nodeId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid} AND STATUS = 0")
	int selectCountByFahterOrgid(BigDecimal orgid) throws Exception;

	@Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId} and STATUS=0 and ORGTYPE < 100 ORDER BY orderid ASC")
	List<TblOrganization> findBysql(BigDecimal nodeId) throws Exception;

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{nodeId} AND STATUS = 0")
	Set<TblOrganization> selectSetByFatherOrgId(BigDecimal orgid) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${orgId})  AND STATUS = 0 AND ORGTYPE < 100 AND ORGTYPE > 0")
	List<String> selectChildrenIdListByFatherOrgId(@Param("orgId")String orgId) throws Exception;

	@Select("SELECT WRITTENBYDEPT FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	String selectWrittenDeptByOrgId(BigDecimal orgid) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${orgId})  AND STATUS = 0 AND ORGTYPE = 0")
	List<String> selectDeptIdListByFatherOrgId(@Param("orgId") String orgId);

	@Select("select * from TBL_ORGANIZATION  WHERE ORGTYPE<100 and ORGTYPE != 0 and FATHERORGID= #{orgid} and  STATUS = 0 ORDER BY ORDERID ASC")
    List<TblOrganization> findByOrgid(BigDecimal orgid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	TblOrganization findById(BigDecimal orgid);

	@Select("SELECT * FROM TBL_ORGANIZATION  t where t.FATHERORGID = #{nodeId} and STATUS = 0 order by t.ORDERID asc")
	List<TblOrganization> getNode(BigDecimal nodeId);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid}")
    Set<TblOrganization> findByfatherorgId(BigDecimal orgid);

	@Select("SELECT ORG.* from TBL_ORGANIZATION org where ORGNAME=#{orgname} AND rownum=1")
	TblOrganization findByname(String  orgname);

	@Select("SELECT * from TBL_ORGANIZATION  t where t.fatherorgid = #{nodeId} order by t.orderid asc")
	List<TblOrganization> selectChildrenOrgList(BigDecimal nodeId) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME = #{orgName}")
	BigDecimal selectOrgIdByOrgName(String orgName) throws Exception;

	@Select("select ORGNAME FROM TBL_ORGANIZATION WHERE ORGID =#{orgId}")
	String getOrgName(@Param("orgId") Long orgId);

	@Select("select ORGNAME FROM TBL_ORGANIZATION WHERE ORGID in (${orgIds})")
	List<String> getOrgNames(@Param("orgIds") String orgIds);
}


