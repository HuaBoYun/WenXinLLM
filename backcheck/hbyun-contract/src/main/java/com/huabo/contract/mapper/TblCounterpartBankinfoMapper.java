package com.huabo.contract.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.mappersql.TblCounterpartBankinfoMapperSqlConfig;
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
public interface TblCounterpartBankinfoMapper extends BaseMapper<TblCounterpartBankinfo> {

	@Select("SELECT * FROM TBL_COUNTERPART_BANKINFO WHERE BUDGETID = #{budgetId}")
    List<TblCounterpartBankinfo> selectBankInfoByBugetId(@Param("budgetId") BigDecimal budgetId) throws Exception;

	@Delete("DELETE FROM TBL_COUNTERPART_BANKINFO WHERE BUDGETID = #{newBudgetId}")
    void deleteByBankInfo(@Param("newBudgetId") BigDecimal newBudgetId) throws Exception;

	@SelectProvider(type=TblCounterpartBankinfoMapperSqlConfig.class,method="findAllListByBankInfo")
	IPage<TblCounterpartBankinfo> findAllListByBankInfo(IPage<TblCounterpartBankinfo> page, TblCounterpartBankinfo bank) throws Exception;

	@Select("SELECT COUNT(0) PCOUNT FROM TBL_CONTRACT_PAYMENT WHERE COUNTERBANK = #{bankId}")
    Integer selectCountByUser(String bankId) throws Exception;

	@Select("SELECT COUNT(0) PCOUNT FROM TBL_CONTRACT_COLLECTION WHERE COUNTERBANK = #{bankId}")
	Integer selectpaymentCountByBankId(String bankId) throws Exception;

	@Delete("DELETE FROM TBL_COUNTERPART_BANKINFO WHERE BANKID = #{bankId}")
    void removeBank(String bankId);

	@Update("UPDATE TBL_COUNTERPART_BANKINFO SET BANKSTATUS = #{bankstatus} WHERE BANKID = #{bankId}")
    void modifyBankStatus(@Param("bankId") String bankId,@Param("bankstatus") Integer bankstatus) throws Exception;
	
	@SelectProvider(type=TblCounterpartBankinfoMapperSqlConfig.class,method="findListByPageInfo")
	IPage<TblCounterpartBankinfo> findListByPageInfo(IPage<TblCounterpartBankinfo> page, TblCounterpartBankinfo bank) throws Exception;

	@Select("SELECT * FROM TBL_COUNTERPART_BANKINFO WHERE BUDGETID = #{budgetId} and BANKACCOUNT=#{BANKACCOUNT}")
    TblCounterpartBankinfo  selectBankInfoById(String budgetId,String BANKACCOUNT);
	
	@InsertProvider(type=TblCounterpartBankinfoMapperSqlConfig.class,method="saveBank")
    @Options(useGeneratedKeys=true, keyProperty="bankid", keyColumn="BANKID")
    void saveBank(TblCounterpartBankinfo bank);

	@Select("SELECT * FROM TBL_COUNTERPART_BANKINFO WHERE  BANKACCOUNT = #{bankaccount}")
    List<TblCounterpartBankinfo> findListByBankInfo(String bankaccount);

	@Update("UPDATE TBL_COUNTERPART_BANKINFO SET bankaccount = #{bankaccount},bankaccname = #{bankaccname},bankstatus = #{bankstatus},banknature = #{banknature},bankkhyh = #{bankkhyh} WHERE bankaccount = #{bankaccount}")
    void updateBybmbh(TblCounterpartBankinfo tblCounterpartBankinfo);

	@Select("SELECT * FROM TBL_COUNTERPART_BANKINFO WHERE OUTSIDEID = #{bankaccount}")
	TblCounterpartBankinfo selectBankInfoByAccount(String bankaccount) throws Exception;

}
