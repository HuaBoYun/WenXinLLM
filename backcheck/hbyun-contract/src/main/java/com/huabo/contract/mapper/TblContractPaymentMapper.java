package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.mappersql.TblContractPaymentMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-15
 */
public interface TblContractPaymentMapper extends BaseMapper<TblContractPayment> {

	@SelectProvider(type=TblContractPaymentMapperSqlConfig.class,method="selectPaymentManagemenByPageInfo")
	IPage<TblContractPayment> selectPaymentManagemenByPageInfo(IPage<TblContractPayment> page, String orgid,
			TblContractPayment payment, TblCyhwUnit unit) throws Exception;

	@Delete("DELETE FROM TBL_CONTRACT_PAYMENT WHERE PAYMENTID = #{paymentId}")
	void deletePaymentId(BigDecimal paymentId) throws Exception;

	@Select("SELECT TCP.PAYMENTID,TCP.PAYMENTTITLE,TCP.APPLYDATE,TCP.PAYMENTRECORD,TCP.PAYMENTTYPE,TCP.PAYMENTLATEDATE,TCP.PAYMENTMEMO,APS.STAFFID AS APSSTAFFID,APS.REALNAME AS APSREALNAME,APO.ORGID AS APOORGID,APO.ORGNAME AS APOORGNAME,TCPL.NODEID,TCPL.NODECONTENT,TCPL.NODEPOST, TCPL.NODEMONEY nodemoney,TCPB.BUDGETID,TCPB.COUNTERPARTNO,TCPB.BUDGETNAME,TCPB.COUNTERPARTHANK,TCPB.COUNTERPARTHANKACCOUNT,TCPB.PROJECTSTAGEGOAL,TCPB.COUNTERPARTPHONE,TCU.CONTRACTID,TCU.CONTRACTNAME,TCU.CONTRACTNO, TCU.CONTRACTMONEY,TCS.REALNAME," +
            "TCP.PAYMENMONEY,TCP.NOPAYMONEY,TCP.ACCUMULATEDPAYMENTS,TCB.BANKID bankbankid,TCB.BANKACCOUNT,TOB.BANKID bankid,TOB.BANKACCNUM,TCI.INVOICEID,TCI.INVOICENO,TCI.INVOICESPORG,TCI.INVOICEMONEY invoicemoney,TCI.INVOICETYPE,TCI.INVOICEDATE,TCI.INVOICESTATUS,TCP.PAYMENTSTATUS " +
            "FROM TBL_CONTRACT_PAYMENT TCP LEFT JOIN TBL_STAFF APS ON TCP.APPLYSTAFF = APS.STAFFID LEFT JOIN TBL_ORGANIZATION APO ON TCP.APPLYORG = APO.ORGID LEFT JOIN TBL_CONTRACT_PLANNODE TCPL ON TCP.NODEID = TCPL.NODEID LEFT JOIN TBL_CYHW_PROJECTBUDGET TCPB ON TCP.BUDGETID = TCPB.BUDGETID LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.CONTRACTID = TCU.CONTRACTID LEFT JOIN TBL_STAFF TCS ON TCS.STAFFID = TCU.CONTRACTSTAFF LEFT JOIN TBL_COUNTERPART_BANKINFO TCB ON TCB.BANKID = TCP.COUNTERBANK LEFT JOIN TBL_ORG_BANKACCOUNT TOB ON TOB.BANKID = TCP.ORGBANK LEFT JOIN TBL_CONTRACT_INVOICESMANAGEMEN TCI ON TCI.INVOICEID = TCP.INVOICEID WHERE PAYMENTID = #{paymentId}")
	TblContractPayment findPaymentInfoByParmentId(BigDecimal paymentId) throws Exception;

	@Select("SELECT TCP.PAYMENTID,TCI.INVOICEMONEY,TCP.PAYMENTTITLE,TCP.PAYMENTLATEDATE,TCP.PAYMENTTYPE,TCPB.BUDGETNAME,TCI.INVOICENO,TCI.INVOICEHEADTEXT FROM TBL_CONTRACT_PAYMENT TCP LEFT JOIN TBL_CYHW_PROJECTBUDGET TCPB ON TCP.BUDGETID = TCPB.BUDGETID LEFT JOIN TBL_CONTRACT_INVOICESMANAGEMEN TCI ON TCP.INVOICEID = TCI.INVOICEID  WHERE CONTRACTID = #{contractid} ")
    @Results({
            @Result(column="PAYMENTID",property="paymentid"),
            @Result(column="INVOICEMONEY",property="paymenmoney"),
            @Result(column="PAYMENTTITLE",property="paymenttitle"),
            @Result(column="PAYMENTLATEDATE",property="paymentlatedate"),
            @Result(column="PAYMENTTYPE",property="paymenttype"),
            @Result(column="BUDGETNAME",property="budgetname"),
            @Result(column="INVOICENO",property="invoiceno"),
            @Result(column="INVOICEHEADTEXT",property="invoiceheadtext"),
    })
    List<TblContractPayment> findpaymentListByContractId(BigDecimal contractid);

	@Select("SELECT * FROM TBL_CONTRACT_PAYMENT WHERE contractid = #{contractid} and nodeid=#{nodeid}")
    TblContractPayment findByParment(String contractid,String nodeid);

	@InsertProvider(type=TblContractPaymentMapperSqlConfig.class,method="savePaymenInfo")
    @Options(useGeneratedKeys=true, keyProperty="paymentid", keyColumn="PAYMENTID")
    void savePaymenInfo(TblContractPayment payment);

	@Select("SELECT sum(Paymenmoney)  FROM TBL_CONTRACT_PAYMENT WHERE contractid = #{contractid}  ")
    BigDecimal getAllPaymenmoney(BigDecimal contractid);

}
