package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractLend;
import com.huabo.contract.mappersql.TblContractLendMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-21
 */
public interface TblContractLendMapper extends BaseMapper<TblContractLend> {

    @UpdateProvider(type=TblContractLendMapperSqlConfig.class,method="updateTblContractLead")
    void updateTblContractLead(TblContractLend lend);

    @InsertProvider(type=TblContractLendMapperSqlConfig.class,method="saveTblContractLead")
    @Options(useGeneratedKeys=true, keyProperty="lendid", keyColumn="LENDID")
    void saveTblContractLead(TblContractLend lend);

    @SelectProvider(type=TblContractLendMapperSqlConfig.class,method= "findByContractId")
    IPage<TblContractLend> findByContractId(IPage<TblContractLend> page, TblContractLend tblContractLead);

    @Select("SELECT TCL.LENDID,TCL.LENDDATE,TCL.RETURNDATE,TCL.LENDSTATUS,TCL.MEMO,TCL.USERID,TCL.CONTRACTID,TCU.CONTRACTNO,TCU.CONTRACTNAME,TS.REALNAME AS REALNAME FROM TBL_CONTRACT_LEND TCL LEFT JOIN TBL_STAFF TS ON TCL.USERID = TS.STAFFID LEFT JOIN TBL_CYHW_UNIT TCU ON TCL.CONTRACTID = TCU.CONTRACTID WHERE TCL.LENDID = #{lendid}")
    @Results({
    	@Result(column="LENDID",property="lendid"),
    	@Result(column="LENDDATE",property="lenddate"),
    	@Result(column="RETURNDATE",property="returndate"),
    	@Result(column="LENDSTATUS",property="lendstatus"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="USERID",property="userid"),
    	@Result(column="CONTRACTID",property="contractid"),
    	@Result(column="CONTRACTNO",property="contractno"),
    	@Result(column="CONTRACTNAME",property="contractname"),
    	@Result(column="REALNAME",property="realname"),
    })
    TblContractLend findById(String lendid);

    @Select("select * from TBL_CONTRACT_LEND WHERE LENDID = (SELECT MAX(LENDID) FROM TBL_CONTRACT_LEND WHERE LENDSTATUS = 6 AND USERID = #{userid} AND CONTRACTID = #{contractId} )")
	TblContractLend selectLendRecord(String userid, String contractId) throws Exception;

    @Update("UPDATE TBL_CONTRACT_LEND SET LENDSTATUS = #{stateSp} WHERE LENDID = #{lendid}")
	void updateLendStatus(BigDecimal lendid, Integer stateSp)throws Exception;

	
}
