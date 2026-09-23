package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblLegalLitigationsettlement;
import com.huabo.contract.mappersql.TblLegalLitigationsettlementMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-18
 */
public interface TblLegalLitigationsettlementMapper extends BaseMapper<TblLegalLitigationsettlement> {

    @SelectProvider(type = TblLegalLitigationsettlementMapperSqlConfig.class,method = "findListByPageInfo")
    IPage<TblLegalLitigationsettlement> findListByPageInfo(IPage<TblLegalLitigationsettlement> page, TblLegalLitigationsettlement litigation,BigDecimal disputeid) throws Exception;

    @Select("SELECT TLL.*,TLD.*,TLA.COURTFIRST FROM TBL_LEGAL_LITIGATIONSETTLEMENT TLL " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEINFO = TLD.DISPUTEID " +
            "LEFT JOIN TBL_LEGAL_ARBITRATSETTLEMENT TLA ON TLA.ARBITRAID = TLL.ARBITRAID " +
            "WHERE LITIGATIONID = #{litigationId}")
    @Results({
    	@Result(column="COURTFIRST",property="courtfirst"),
    })
    @Options(useGeneratedKeys=true, keyProperty="litigationId", keyColumn="LITIGATIONID")
    TblLegalLitigationsettlement findById(BigDecimal litigationId) throws Exception;

    @InsertProvider(type = TblLegalLitigationsettlementMapperSqlConfig.class,method = "saveLitigationSettlement")
    @Options(useGeneratedKeys=true, keyProperty="litigationid", keyColumn="LITIGATIONID")
    void saveLitigationSettlement(TblLegalLitigationsettlement litigation) throws Exception;

    @UpdateProvider(type = TblLegalLitigationsettlementMapperSqlConfig.class,method = "updateModifyLitigationSettlement")
    void updateModifyLitigationSettlement(TblLegalLitigationsettlement oldLitigation) throws Exception;

    @Delete("DELETE FROM TBL_LEGAL_LITIGATIONSETTLEMENT WHERE LITIGATIONID = #{litigationid}")
    void deleteLitigationId(BigDecimal litigationid) throws Exception;

    @Select("SELECT * FROM TBL_LEGAL_LITIGATIONSETTLEMENT TLL " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEINFO = TLD.DISPUTEID " +
            "WHERE LITIGATIONID = #{litigationid}")
    TblLegalLitigationsettlement findByLitigationid(BigDecimal litigationid) throws Exception;

//    @Insert("INSERT INTO  TBL_LEGAL_LSETTLEMENT_ATT(LITIGATIONID,ATTID) VALUES (#{litigationid},#{id})")
//    void insertAttRelation(Integer litigationid, String id);

//    @Update()
//    void updateModifyLitigationSettlement(Integer litigationid);
}
