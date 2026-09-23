package com.huabo.contract.mapper;

import com.hbfk.entity.TblAttachment;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractPlannode;
import com.huabo.contract.mappersql.TblContractPlannodeMapperSqlConfig;

import net.sf.json.JSONObject;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-14
 */
public interface TblContractPlannodeMapper extends BaseMapper<TblContractPlannode> {

	@SelectProvider(type=TblContractPlannodeMapperSqlConfig.class,method="findPlanNodeListForCollection")
	IPage<TblContractPlannode> findPlanNodeListForCollection(IPage<TblContractPlannode> page, TblContractPlannode node) throws Exception;

	@SelectProvider(type=TblContractPlannodeMapperSqlConfig.class,method="findPlanNodeListForPayment")
	IPage<TblContractPlannode> findPlanNodeListForPayment(IPage<TblContractPlannode> page, TblContractPlannode node) throws Exception;

	@Select("SELECT * FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId} AND (PLANNODESTATUS != 2 OR PLANNODESTATUS IS NULL)")
    List<TblContractPlannode> getListPlannodeForBianGeng(String contractId);

	@InsertProvider(type = TblContractPlannodeMapperSqlConfig.class,method = "saveContractPlannode")
    @Options(useGeneratedKeys=true, keyProperty="nodeid", keyColumn="NODEID")
	void saveContractPlannode(TblContractPlannode node);

	@SelectProvider(type = TblContractPlannodeMapperSqlConfig.class,method = "findeSumMoneyByContractId")
	BigDecimal findeSumMoneyByContractId(BigDecimal contractId, BigDecimal nodeid);

	@UpdateProvider(type = TblContractPlannodeMapperSqlConfig.class,method = "updateContractPlannode")
	void updateContractPlannode(TblContractPlannode node);

	@Select("SELECT TCP.NODEID,TCP.NODECONTENT,TCP.PROJECTID,TCP.PLANSTARTDATE,TCP.PLANENDDATE,TCP.NODEPOST,TCP.BUDGETIDS,"
    		+ " TCP.NODEPLANPAYDATE,TCP.DISPATCHSTAFF,TS.REALNAME,TCP.DISPATCHDEPT,ORG.ORGNAME,TCP.nodemoney,TCP.goodsname, "
    		+ " TCP.goodscount,TCP.performanceCategory FROM TBL_CONTRACT_PLANNODE TCP  "
    		+ " LEFT JOIN TBL_STAFF TS ON TCP.DISPATCHSTAFF = TS.STAFFID  "
    		+ " LEFT JOIN TBL_ORGANIZATION ORG ON TCP.DISPATCHDEPT = ORG.ORGID  "
    		+ " WHERE TCP.NODEID = #{nodeId}")
    TblContractPlannode selectByPlanId(BigDecimal nodeId);

	@Delete("DELETE FROM TBL_CONTRACT_PLANNODE WHERE NODEID = #{nodeId}")
    void removeContractPlannode(BigDecimal nodeId);

	@Select("SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId} AND PERFORMANCECATEGORY != '1'")
	Integer findeHwCountByContractid(BigDecimal contractId) throws Exception;

	@Select("SELECT COUNT(NODEID) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId}")
    Integer countPlanByContractId(BigDecimal contractId);

	@Select("SELECT NVL(SUM(NODEPOST),0) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId}")
	Integer sumPlanPostPoint(BigDecimal contractId);

	@Select("SELECT TCP.NODEID,TCP.NODECONTENT,TCP.PROJECTID,TCP.PLANSTARTDATE,TCP.PLANENDDATE,TCP.NODEPOST,TCP.NODEPLANPAYDATE,TCP.DISPATCHSTAFF as jbstaffid,TS.REALNAME,TCP.DISPATCHDEPT as jbunitid,ORG.ORGNAME,TCP.NODEMONEY,PLANNODESTATUS,TCP.goodsname,TCP.goodscount,TCP.performanceCategory FROM TBL_CONTRACT_PLANNODE TCP LEFT JOIN TBL_STAFF TS ON TCP.DISPATCHSTAFF = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON TCP.DISPATCHDEPT = ORG.ORGID WHERE TCP.PROJECTID = #{contractid}  ORDER BY TCP.NODEID ")
    List<TblContractPlannode> findPlanNodeListByContractId(BigDecimal contractid);

	@SelectProvider(type = TblContractPlannodeMapperSqlConfig.class,method = "findContractNodeListByPageInfo")
	List<TblContractPlannode> findContractNodeListByPageInfo(TblContractPlannode node);

	@Select("SELECT TCP.NODEID,TCP.NODECONTENT,TCP.PROJECTID ,TCP.PLANSTARTDATE ,TCP.PLANENDDATE ,TCP.NODEPOST ,TCS.SPNODEID AS TCPSPNODEID, "
    		+ "TCS.STARTDATE AS TCSSTARTDATE,TCS.ENDDATE AS TCSENDDATE,TCS.NODEMEMO AS TCSNODEMEMO,TCS.NODECONTENT AS TCSNODECONTENT, "
    		+ "TCS.NODEPOST AS TCSNODEPOST,TCP.NODEMONEY ,TCS.NODEFINISHDATE AS TCSNODEFINISHDATE,TCU.DCTYPE AS TCUDCTYPE, "
    		+ "TCP.NODEPLANPAYDATE,TCS.ISWY,TCP.PERFORMANCECATEGORY,TCP.GOODSNAME,TCP.GOODSCOUNT "
    		+ "FROM TBL_CONTRACT_PLANNODE TCP "
    		+ "LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.PROJECTID = TCU.CONTRACTID "
    		+ "LEFT JOIN TBL_CONTRACT_SPNODE TCS ON TCP.NODEID = TCS.NODEID "
    		+ "WHERE TCP.NODEID = #{planId}")
    TblContractPlannode findWriteContractPlanNode(BigDecimal nodeId);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,UPLOADER from TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CONTRACTNODE_ATT WHERE ATTTYPE = 1 AND NODEID = #{planId})")
    List<TblAttachment> findeWriteContractPlanFileInfo(BigDecimal planId);

	@Update("UPDATE TBL_CONTRACT_PLANNODE SET PLANNODESTATUS = #{planStatus},feedback= #{feedback} WHERE NODEID = #{nodeId}")
    void modifyPlanNodeStatus(BigDecimal nodeId, Integer planStatus, String feedback);

	@Select("SELECT TCP.NODEID,TCP.NODECONTENT,TCP.PROJECTID,TCP.PLANSTARTDATE,TCP.PLANENDDATE,TCP.NODEPOST,TCP.NODEPLANPAYDATE,TCP.DISPATCHSTAFF,TS.REALNAME,TCP.DISPATCHDEPT,ORG.ORGNAME,TCP.nodemoney FROM TBL_CONTRACT_PLANNODE TCP LEFT JOIN TBL_STAFF TS ON TCP.DISPATCHSTAFF = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON TCP.DISPATCHDEPT = ORG.ORGID WHERE TCP.NODEID = #{nodeId}")
	TblContractPlannode findBynodeId(BigDecimal nodeId);

	@Update("UPDATE TBL_CONTRACT_PLANNODE SET PLANNODESTATUS = #{plStatus} WHERE NODEID = #{nodeid}")
	void updatePlanNodeStatus(BigDecimal nodeid, Integer plStatus);

	@Select("SELECT (SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId})-(SELECT COUNT(0) FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId} AND PLANNODESTATUS = 2) AS WWCCOUNT FROM DUAL")
	Integer selectNoDoneCount(BigDecimal contractId);

}
