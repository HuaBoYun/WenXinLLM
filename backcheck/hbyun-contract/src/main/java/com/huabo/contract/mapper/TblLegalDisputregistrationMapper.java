package com.huabo.contract.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblDispuinfo;
import com.huabo.contract.entity.TblLegalDisputregistration;
import com.huabo.contract.mappersql.TblLegalDisputregistrationMapperSqlConfig;

import net.sf.json.JSONObject;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-16
 */
public interface TblLegalDisputregistrationMapper extends BaseMapper<TblLegalDisputregistration> {

	@InsertProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="saveAttacheMent")
    void saveAttacheMent(Integer type, BigDecimal bid, BigDecimal aid) throws Exception;

	@SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="findAttacheMentByBid")
	List<TblAttachment> findAttacheMentByBid(Integer type, BigDecimal bid) throws Exception;

	@DeleteProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="deleteAttacheMentByBid")
	void deleteAttacheMentByBid(Integer type, BigDecimal aid) throws Exception;

	@SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="selectCountByDisputreNo")
	String selectCountByDisputreNo(String disputeno);

	@Select("SELECT tst.REALNAME AS zxstaffname,tld.*, ts.REALNAME AS realname,tcu.*,org.* " +
            "FROM TBL_LEGAL_DISPUTREGISTRATION tld " +
            "LEFT JOIN TBL_STAFF tst on tld.DISPUTEUNDERTAKER = tst.STAFFID " +
            "LEFT JOIN TBL_CYHW_UNIT tcu ON tld.CONTRACTINFO = tcu.CONTRACTID " +
            "LEFT JOIN TBL_STAFF ts ON tld.CREATESTAFF = ts.STAFFID " +
            "INNER JOIN TBL_ORGANIZATION org ON ts.ORGID = org.ORGID " +
            " WHERE tld.DISPUTEID = #{disputeId}")
	TblLegalDisputregistration findBydisputeId(BigDecimal disputeId) throws Exception;

	@SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="findListByPageInfo")
	IPage<TblLegalDisputregistration> findListByPageInfo(String companyId, IPage<TblLegalDisputregistration> page,
			TblLegalDisputregistration dispute, BigDecimal pid) throws Exception;

	@SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="findListLSByPageInfo")
	IPage<TblLegalDisputregistration> findListLSByPageInfo(String companyId, IPage<TblLegalDisputregistration> page,
			TblLegalDisputregistration dispute, String companyIdStrs) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTEID = #{disputeId}")
	TblLegalDisputregistration findByDisputeId(BigDecimal disputeId) throws Exception;

	/**
	 * 异常
	 * @param attid
	 * @throws Exception
	 */
	@Delete("DELETE FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTEID = #{attid}")
	void deleteRelation(@Param("attid") String attid) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION tld LEFT JOIN TBL_CYHW_UNIT tcu on tld.CONTRACTINFO = tcu.CONTRACTID WHERE tld.DISPUTEID = #{disputeId}")
	TblLegalDisputregistration findDisputeId(@Param("disputeId")BigDecimal disputeId) throws Exception;

	@DeleteProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="deleteAttacheMents")
	void deleteAttacheMents(Integer type, BigDecimal bid) throws Exception;

	@Delete("DELETE FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTEID = #{disputeId}")
	void removecaseInformation(BigDecimal disputeId) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION tld " +
            "LEFT JOIN TBL_CYHW_UNIT tcu on tld.CONTRACTINFO = tcu.CONTRACTID " +
            "WHERE tld.DISPUTEID = #{disputeId}")
	TblLegalDisputregistration selectDisputeId(@Param("disputeId")BigDecimal disputeId) throws Exception;

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="findDisputeIdsForDispute")
	List<TblDispuinfo> findDisputeIdsForDispute(String colName, String tableName, String orgColName, String whSql, BigDecimal oid)throws Exception;

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="findListByPageInfoDispute")
	IPage<TblLegalDisputregistration> findListByPageInfoDispute(IPage<TblLegalDisputregistration> page,
			TblLegalDisputregistration dispute, BigDecimal pid, String disputeIds) throws Exception;

	 @Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION tld " +
	            "LEFT JOIN TBL_CYHW_UNIT tcu on tld.CONTRACTINFO = tcu.CONTRACTID " +
	            "WHERE tld.DISPUTEID = #{disputeId} AND tld.LINKORG = #{orgid}")
	TblLegalDisputregistration findByOrgid(BigDecimal disputeId, BigDecimal orgid);

	@Update("UPDATE TBL_LEGAL_DISPUTREGISTRATION SET ISCLASSICCASE = 2 WHERE disputeid = #{disputeId}")
	void delClassicCase(BigDecimal disputeId) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION tld WHERE tld.DISPUTEID = (SELECT DISPUTEID FROM TBL_LEGAL_LITIGATIONSETTLEMENT WHERE LITIGATIONID = #{litigationid})")
	TblLegalDisputregistration findByIdByLitigation(BigDecimal litigationid);
	    
	@Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION tld WHERE tld.DISPUTEID = (SELECT DISPUTEID FROM TBL_LEGAL_ARBITRATSETTLEMENT WHERE ARBITRAID = #{arbitraid})")
	TblLegalDisputregistration findByIdByArbitra(BigDecimal arbitraid);

	 @Update("UPDATE TBL_LEGAL_DISPUTREGISTRATION SET ISCLASSICCASE = 1 WHERE disputeid = #{disputeid}")
	void addClassicCase(BigDecimal disputeid);

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="findClassicCaseListByPageInfo")
	IPage<TblLegalDisputregistration> findClassicCaseListByPageInfo(String companyId,IPage<TblLegalDisputregistration> page, TblLegalDisputregistration dispute, BigDecimal orgid) throws Exception;

	 @Select("SELECT * FROM TBL_LEGAL_DISPUTREGISTRATION WHERE DISPUTEID = #{orgid}")
	String findDisputeIdsForDisputeClose(BigDecimal orgid);

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="selectJfslList")
	 List<JSONObject>  selectJfslList(Integer year,BigDecimal orgid) throws Exception;

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="selectMoneyValue")
	List<JSONObject> selectMoneyValue(Integer year, BigDecimal orgid) throws Exception;

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="selectSsTypeList")
	List<JSONObject> selectSsTypeList(Integer year, BigDecimal orgid) throws Exception;
	 
	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="selectSlztList")
	List<JSONObject> selectSlztList(Integer year, BigDecimal orgid) throws Exception;

	 @SelectProvider(type=TblLegalDisputregistrationMapperSqlConfig.class,method="selectDisputeMoneyList")
	List<JSONObject> selectDisputeMoneyList(Integer year, BigDecimal orgid) throws Exception;


}
