package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalNegotiatedsettlemen;
import com.huabo.contract.mappersql.TblLegalNegotiatedsettlemenMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-17
 */
public interface TblLegalNegotiatedsettlemenMapper extends BaseMapper<TblLegalNegotiatedsettlemen> {

	@SelectProvider(type=TblLegalNegotiatedsettlemenMapperSqlConfig.class,method="findListByPage")
	IPage<TblLegalNegotiatedsettlemen> findListByPage(IPage<TblLegalNegotiatedsettlemen> page,
			TblLegalNegotiatedsettlemen negotia, BigDecimal pid, BigDecimal disputeid) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN tln " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tln.DISPUINFO = tld.DISPUTEID " +
            "LEFT JOIN TBL_CYHW_UNIT tcu on tld.CONTRACTINFO = tcu.CONTRACTID " +
            "LEFT JOIN  TBL_STAFF TCS ON TCS.STAFFID = tcu.CONTRACTSTAFF " +
            "WHERE tln.NEGOTIAID = #{negotiaId}")
	TblLegalNegotiatedsettlemen findByNegotiaId(BigDecimal negotiaId);

	@InsertProvider(type=TblLegalNegotiatedsettlemenMapperSqlConfig.class,method="saveDiputregistration")
	void saveDiputregistration(TblLegalNegotiatedsettlemen negotiated) throws Exception;

	@UpdateProvider(type=TblLegalNegotiatedsettlemenMapperSqlConfig.class,method="updateOldNegotiated")
	void updateOldNegotiated(TblLegalNegotiatedsettlemen negotiated) throws Exception;

	@Delete("DELETE FROM TBL_LEGAL_NEGOTIATEDSETTLEMEN WHERE NEGOTIAID = #{negotiaId}")
	void removeLegalNegotiatedSettlemen(BigDecimal negotiaId) throws Exception;

	@SelectProvider(type=TblLegalNegotiatedsettlemenMapperSqlConfig.class,method="findListForLitiationList")
	IPage<TblLegalNegotiatedsettlemen> findListForLitiationList(IPage<TblLegalNegotiatedsettlemen> page,
			TblLegalNegotiatedsettlemen negotiate) throws Exception;

}
