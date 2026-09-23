package com.huabo.contract.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractCollection;
import com.huabo.contract.mappersql.TblContractCollectionMapperSqlConfig;

import lombok.Data;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-11
 */
public interface TblContractCollectionMapper extends BaseMapper<TblContractCollection> {

	@SelectProvider(type=TblContractCollectionMapperSqlConfig.class,method="selectListByPageInfo")
	IPage<TblContractCollection> selectListByPageInfo(IPage<TblContractCollection> page, String contractname,
			String contractno, BigDecimal pid, TblContractCollection collection) throws Exception;

	@Select("SELECT NODEMONEY FROM TBL_CONTRACT_COLLECTION WHERE COLLECTIONID = #{nodeid}")
	BigDecimal checkMoneyNodeId(@Param("nodeid")BigDecimal nodeid) throws Exception;

	@Select("SELECT TCP.NODEPOST*TCU.CONTRACTMONEY/100 AS SKMONEY FROM TBL_CONTRACT_PLANNODE TCP LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.PROJECTID = TCU.CONTRACTID LEFT JOIN TBL_CONTRACT_COLLECTION TCC ON TCP.NODEID = TCC.NODEID LEFT JOIN TBL_CONTRACT_INVOICESMANAGEMEN TCI ON TCI.INVOICEID = TCC.INVOICEID WHERE TCP.NODEID = #{nodeid} GROUP BY TCP.NODEPOST*TCU.CONTRACTMONEY")
	BigDecimal getSkMoneyNodeId(BigDecimal nodeid) throws Exception;

	
	@Select("SELECT * FROM TBL_CONTRACT_COLLECTION WHERE COLLECTIONID = #{collectionid}")
    TblContractCollection findByContractId(BigDecimal collectionid);

	@Delete("DELETE FROM TBL_CONTRACT_COLLECTION WHERE COLLECTIONID = #{collectionId}")
	void removeContractCollection(BigDecimal collectionId) throws Exception;
	
	@Select("SELECT * FROM TBL_CONTRACT_COLLECTION TCC " +
	        "LEFT JOIN TBL_CYHW_UNIT TCU  ON TCC.CONTRACTID = TCU.CONTRACTID " +
	        "LEFT JOIN TBL_CYHW_PROJECTBUDGET BUD ON TCU.CONTRACTXDFXINFO = BUD.BUDGETID " +
	        "LEFT JOIN TBL_CONTRACT_PLANNODE TCP ON TCC.NODEID = TCP.NODEID " +
	        "LEFT JOIN TBL_COUNTERPART_BANKINFO TCB ON TCC.COUNTERBANK = TCB.BANKID " +
	        "LEFT JOIN TBL_ORG_BANKACCOUNT TOB ON TCC.ORGBANK = TOB.BANKID " +
	        "LEFT JOIN TBL_CONTRACT_INVOICESMANAGEMEN TCI ON TCC.INVOICEID = TCI.INVOICEID " +
	        "WHERE COLLECTIONID  = #{collectionId}")
	TblContractCollection findCollectionInfoByCollectId(BigDecimal collectionId) throws Exception;

	@Select("SELECT TCC.COLLECTIONID,TCC.COLLECTIONSKDATE,TCC.COLLECTIONORGNAME,TCI.INVOICEMONEY,TCI.INVOICEHEADTEXT,TCI.INVOICENO FROM TBL_CONTRACT_COLLECTION TCC LEFT JOIN TBL_CONTRACT_INVOICESMANAGEMEN TCI ON TCC.INVOICEID = TCI.INVOICEID WHERE TCC.CONTRACTID = #{contractid} ORDER BY COLLECTIONID DESC")
	List<TblContractCollection> findcollectionListByContractId(BigDecimal contractid);

	@Select("SELECT  CONTRACTID  FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005')  AND CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND RECORDPARENT IS NOT NULL) AND CONTRACTSTATUS = 7")
	List<String>  getSkContractIds();

	@Select("SELECT * FROM TBL_CONTRACT_COLLECTION WHERE Contractid = #{contractid}  and nodeid=#{nodeid}" )
    TblContractCollection findByContract(String contractid,String nodeid);

	@InsertProvider(method="SaveMengerCollectionEntity",type=TblContractCollectionMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="collectionid", keyColumn="COLLECTIONID")
    void SaveMengerCollectionEntity(TblContractCollection collection);

}
