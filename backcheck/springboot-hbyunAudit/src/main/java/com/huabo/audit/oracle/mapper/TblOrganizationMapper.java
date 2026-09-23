package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.util.PageInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Mapper
public interface TblOrganizationMapper extends BaseMapper<TblOrganization> {

	@Select("SELECT * from TBL_ORGANIZATION  t where t.fatherorgid = #{nodeId} order by t.orderid asc")
	List<TblOrganization> selectChildrenOrgList(BigDecimal nodeId) throws Exception;
	
	@Select("SELECT * from TBL_ORGANIZATION  t where t.orgid = #{nodeId} ")
	 TblOrganization  selectOrgById(BigDecimal nodeId) throws Exception;

	@Select("select * from TBL_ORGANIZATION org where orgtype = 100 and (STATUS != 1 or STATUS IS NULL) start with fatherorgid = -1 connect by prior orgid = fatherorgid   ORDER BY orderid ASC")
    List<TblOrganization> selectzong();
	
	@Select("SELECT * from TBL_ORGANIZATION where  ORGNAME='行业' and ORGTYPE=100 ORDER BY orderid ASC")
	List<TblOrganization> selectGetHY();
    @SelectProvider(method="selectCountByPageInfo",type=TblOrganizationMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblOrganization> pageInfo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_ORGANIZATION TNA  WHERE TNA.ORGID = #{orgid}")
    @Results({
    	@Result(column="ORGID",property="orgid"),
    	
    })
   	TblOrganization selectById(@Param("orgid") Integer orgid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblOrganizationMapperSqlConfig.class)
    @Results({
    	@Result(column="ORGID",property="orgid"),
    	
    })
    List<TblOrganization> selectListByPageInfo(PageInfo<TblOrganization> pageInfo) throws Exception;
    
    
    @Select("SELECT * FROM TBL_ORGANIZATION  t where t.FATHERORGID = #{nodeId} and STATUS = 0 order by t.ORDERID asc")
	List<TblOrganization> getNode(BigDecimal nodeId);
    
    @Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId} and STATUS=0 and ORGTYPE < 100 ORDER BY orderid ASC")
	List<TblOrganization> findBysql(BigDecimal nodeId);
    
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid} ORDER BY orderid ASC")
    Set<TblOrganization> findByfatherorgId(BigDecimal orgid);
    
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	TblOrganization findById(BigDecimal orgid);

    /**
	 * 传入部门Id 查找出当前部门所隶属的公司
	 //* @param deptid
	 * @return
	 * @throws Exception
	 */
	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 AND ROWNUM = 1 START WITH o.ORGID = #{orgid} CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc")
	@Results({
		@Result(column="ORGID",property="orgid"),
		@Result(column="ORGNAME",property="orgname"),
		@Result(column="FATHERORGID",property="fatherorgid"),
		@Result(column="ORGNUMBER",property="orgnumber"),
		@Result(column="ORGMENO",property="orgmeno"),
		@Result(column="MEMO",property="memo"),
		@Result(column="ICODE",property="icode"),
		@Result(column="ORGTYPE",property="orgtype"),
		@Result(column="STATUS",property="status"),
		@Result(column="ISZY",property="iszy"),
	})
	TblOrganization selectCompanyInfoByDeptId(@Param("orgid") BigDecimal orgid);

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${orgId})  AND STATUS = 0 AND ORGTYPE < 100 ")
	List<String> selectFatherIdStrsByChidrenOrgId(@Param("orgId")String orgId) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${orgId})  AND STATUS = 0 AND ORGTYPE < 100 AND ORGTYPE > 0")
	List<String> selectChildrenIdListByFatherOrgId(@Param("orgId")String orgId) throws Exception;

    
	@Select("  select orgname from TBL_ORGANIZATION where 1=1   and orgid IN (#{ids}) ")//count是什么条件
	List<TblOrganization> getOrgNameByOrgids(@Param("ids")String ids);

    @Select("SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    public Integer selectUniqueColumn(BigDecimal orgid);
    
    @Select("SELECT ORGNUMBER FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    public String selectOrgNumber(BigDecimal orgid);
}
