package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;
import com.huabo.contract.mappersql.TblContractInvoicesmanagemenMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-14
 */
public interface TblContractInvoicesmanagemenMapper extends BaseMapper<TblContractInvoicesmanagemen> {

	@SelectProvider(type=TblContractInvoicesmanagemenMapperSqlConfig.class,method="findInvoiceInfoListForCollection")
	IPage<TblContractInvoicesmanagemen> findInvoiceInfoListForCollection(IPage<TblContractInvoicesmanagemen> page,
			TblContractInvoicesmanagemen invoice) throws Exception;

	@Select("SELECT * FROM TBL_CONTRACT_INVOICESMANAGEMEN TCI LEFT JOIN TBL_CYHW_PROJECTBUDGET TCP ON TCI.BUDGETID = TCP.BUDGETID WHERE INVOICEID = #{invoiceid}")
	TblContractInvoicesmanagemen getInvoiceid(@Param("invoiceid") BigDecimal invoiceid);

	@SelectProvider(type=TblContractInvoicesmanagemenMapperSqlConfig.class,method="findContractInvociesManaeMenPageInfo")
	IPage<TblContractInvoicesmanagemen> findContractInvociesManaeMenPageInfo(IPage<TblContractInvoicesmanagemen> page,
			TblContractInvoicesmanagemen invoice) throws Exception;

	@SelectProvider(type=TblContractInvoicesmanagemenMapperSqlConfig.class,method = "selectListCount")
	Integer selectListCount(TblContractInvoicesmanagemen invoice) throws Exception;

	@Update("UPDATE TBL_CONTRACT_INVOICESMANAGEMEN SET INVOICESTATUS = #{status} WHERE INVOICEID = #{invoiceId}")
	void updateModifyInvoiceStatus(@Param("invoiceId")BigDecimal invoiceId,@Param("status") Integer status) throws Exception;

	@Delete("DELETE FROM TBL_CONTRACT_INVOICESMANAGEMEN WHERE INVOICEID = #{invoiceId}")
	void removeInvoiceInfo(@Param("invoiceId")BigDecimal invoiceId) throws Exception;

	@InsertProvider(type=TblContractInvoicesmanagemenMapperSqlConfig.class,method = "saveMergenEntity")
	@Options(useGeneratedKeys=true, keyProperty="invoiceid", keyColumn="INVOICEID")
	void saveMergenEntity(TblContractInvoicesmanagemen oldInvoice);

    
}
