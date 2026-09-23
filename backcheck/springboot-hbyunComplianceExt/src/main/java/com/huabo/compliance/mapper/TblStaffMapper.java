package com.huabo.compliance.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.mockito.internal.matchers.Find;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblStaff;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface TblStaffMapper extends BaseMapper<TblStaff> {


    @Select("select * from TBL_STAFF t where orgid = #{orgid}  AND (t.status is NULL or t.status != 0)")
    <p extends IPage<TblStaff>>  p findStaffByOrgid(p page, @Param("orgid") BigDecimal orgid);
    
    @Select("select * from TBL_STAFF where ORGID = #{orgid}")
    String selectFindOrgid(TblOrganization orgid);
    
    @Delete("DELETE FROM TBL_STAFF WHERE ORGID = #{orgid}")
	void deleteOrgid(TblOrganization orgid);
    
    @Select("select S.*,O.ORGNAME from TBL_STAFF S LEFT JOIN TBL_ORGANIZATION O ON S.ORGID = O.ORGID WHERE S.STAFFID = #{id}")
    TblStaff findById(BigDecimal id);
    
    @SelectProvider(method = "findAllPageInfoByacctid",type = TblStaffMapperSqlConifg.class)
    List<TblStaff> findAllPageInfoByacctid(PageInfo<TblStaff> pageInfo, String bookid);
    
    @SelectProvider(method = "findAllPageInfoByacctidcCount",type = TblStaffMapperSqlConifg.class)
	Integer findAllPageInfoByacctidcCount(String bookid);
    
    @SelectProvider(method = "findByAll",type = TblStaffMapperSqlConifg.class)
    List<TblStaff> findByAll(String pid, PageInfo<TblStaff> pageInfo);

	@Select(" select COUNT(*) from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID = #{pid} AND (TS.STATUS is NULL or TS.STATUS != 0)")
	Integer findByAllCount(String pid);
	
	@SelectProvider(method = "findByAllORGID",type = TblStaffMapperSqlConifg.class)
	List<TblStaff> findByAllORGID(BigDecimal orgid, PageInfo<TblStaff> pageInfo);

	@Select("SELECT COUNT(*) FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID " +
			"WHERE TOR.FATHERORGID = #{orgid} AND (TS.STATUS IS NULL OR TS.STATUS != 0)")
	Integer findByAllORGIDCount(BigDecimal orgid);
	
	@Update("UPDATE TBL_STAFF SET PASSWORD =#{password},PASSWORD = #{possword1} WHERE STAFFID = #{pid}")
	//@UpdateProvider(type=TblStaffMapperSqlConifg.class,method="updateUser")
	void updateUser(TblStaff user);
	
	@Select("SELECT s.STAFFID,s.REALNAME,o.ORGNAME FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (%s)) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = %s connect by prior ORGID = FATHERORGID)")
    List<TblStaff> selectDan();

	@Select("SELECT count(*) FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (%s)) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = %s connect by prior ORGID = FATHERORGID)")
	List<TblStaff> selectCountt();
	
	@SelectProvider(method = "selectListByPageInfoOrgid",type =TblStaffMapperSqlConifg.class )
	List<TblStaff> selectListByPageInfoOrgid(PageInfo<TblStaff> pageInfo, BigDecimal pid,Find find);
	
	@Select("select COUNT(*) from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID =#{pid}")
	Integer selectListByPageInfoCount(BigDecimal pid);
	
	@SelectProvider(method = "selectListByPageInfoFind",type = TblStaffMapperSqlConifg.class)
	List<TblStaff> selectListByPageInfoFind(PageInfo<TblStaff> pageInfo, BigDecimal orgid, Find find);
	
	@Select("select COUNT(*) from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TOR.FATHERORGID =  #{orgid} AND TOR.ORGTYPE = 0 ")
	Integer selectListByPageInfoCountOrgid(BigDecimal orgid);
	
	@InsertProvider(method = "insertUser",type = TblStaffMapperSqlConifg.class)
	@Options(useGeneratedKeys=true, keyProperty="staffid", keyColumn="STAFFID")
	void insertUser(TblStaff user);
	
	@Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{userid}")
	TblStaff selectByUserId(String userid);
}
