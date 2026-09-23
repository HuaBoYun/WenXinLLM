package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblOrgBankaccount;
import com.huabo.contract.mappersql.TblOrgBankaccountMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-14
 */
public interface TblOrgBankaccountMapper extends BaseMapper<TblOrgBankaccount> {

	@SelectProvider(type=TblOrgBankaccountMapperSqlConfig.class,method="findPlanNodeListForCollection")
	IPage<TblOrgBankaccount> findPlanNodeListForCollection(IPage<TblOrgBankaccount> page, TblOrgBankaccount bank) throws Exception;

	@Select("SELECT * FROM TBL_ORG_BANKACCOUNT WHERE BANKID = #{bankId}")
	TblOrgBankaccount findByBanId(BigDecimal bankId) throws Exception;

	@SelectProvider(type = TblOrgBankaccountMapperSqlConfig.class,method = "findListByPageInfoPid")
	IPage<TblOrgBankaccount> findListByPageInfoPid(IPage<TblOrgBankaccount> page, BigDecimal pid,TblOrgBankaccount bank) throws Exception;

	@Select("SELECT * FROM TBL_ORG_BANKACCOUNT WHERE BANKID = #{bankid}")
	TblOrgBankaccount findByBankId(BigDecimal bankid) throws Exception;

	@Delete("DELETE FROM TBL_ORG_BANKACCOUNT WHERE BANKID = #{bankId}")
	String removeOrgBankInfo(@Param("bankId")BigDecimal bankId) throws Exception;

	@Select("SELECT * FROM TBL_ORG_BANKACCOUNT WHERE BANKACCNUM = #{num} and orgid=#{orgid}")
    TblOrgBankaccount findByBankNum(String num,BigDecimal orgid);

	@InsertProvider(type = TblOrgBankaccountMapperSqlConfig.class,method = "savebankInfo")
    @Options(useGeneratedKeys=true, keyProperty="bankid", keyColumn="BANKID")
    void savebankInfo(TblOrgBankaccount bank);

}
