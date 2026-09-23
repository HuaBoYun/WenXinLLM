package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblLegalArbitratsettlement;
import com.huabo.contract.mappersql.TblLegalArbitratsettlementMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-21
 */
public interface TblLegalArbitratsettlementMapper extends BaseMapper<TblLegalArbitratsettlement> {

	@Select("SELECT TCU.* FROM TBL_LEGAL_DISPUTREGISTRATION TLD  LEFT JOIN TBL_CYHW_UNIT TCU ON TLD.CONTRACTINFO = TCU.CONTRACTID WHERE TLD.DISPUTEID = #{disputeId}")
	TblCyhwUnit findContractByDisputeId(BigDecimal disputeId) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_ARBITRATSETTLEMENT TLA " +
            " LEFT JOIN TBL_LEGAL_NEGOTIATEDSETTLEMEN TLN ON TLA.NEGOTIATEINFO = TLN.NEGOTIAID " +
            " LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLN.DISPUINFO = TLD.DISPUTEID " +
            " WHERE ARBITRAID = #{arbitraId}")
    @Options(useGeneratedKeys=true, keyProperty="arbitraid", keyColumn="ARBITRAID")
	TblLegalArbitratsettlement findByArbitraId(BigDecimal arbitraId) throws Exception;

	@SelectProvider(type=TblLegalArbitratsettlementMapperSqlConfig.class,method="findListByPageInfo")
	IPage<TblLegalArbitratsettlement> findListByPageInfo(IPage<TblLegalArbitratsettlement> page,TblLegalArbitratsettlement negotia, BigDecimal disputeid) throws Exception;

	@InsertProvider(type = TblLegalArbitratsettlementMapperSqlConfig.class, method = "saveDiputergistration")
    @Options(useGeneratedKeys=true, keyProperty="arbitraid", keyColumn="ARBITRAID")
	void saveDiputergistration(TblLegalArbitratsettlement arbitrat) throws Exception;

	@UpdateProvider(type=TblLegalArbitratsettlementMapperSqlConfig.class,method="modifyNegotiatedSettlement")
	void modifyNegotiatedSettlement(TblLegalArbitratsettlement arbitrat) throws Exception;

	@Delete("DELETE FROM TBL_LEGAL_ARBITRATSETTLEMENT WHERE ARBITRAID = #{arbitraid}")
	void removeLegalNegotiatedSettlemen(BigDecimal arbitraid) throws Exception;

}
