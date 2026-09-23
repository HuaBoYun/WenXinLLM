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
import com.huabo.contract.entity.TblLegalCloseinformation;
import com.huabo.contract.mappersql.TblLegalCloseinformationMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
public interface TblLegalCloseinformationMapper extends BaseMapper<TblLegalCloseinformation> {

    @SelectProvider(type=TblLegalCloseinformationMapperSqlConfig.class,method="findListByPageInfo")
    IPage<TblLegalCloseinformation> findListByPageInfo(IPage<TblLegalCloseinformation> page, TblLegalCloseinformation closeInfo,BigDecimal disputeid);

    @Select("SELECT * FROM TBL_LEGAL_CLOSEINFORMATION tlc " +
            "LEFT JOIN TBL_STAFF ts on tlc.CREATESTAFF = ts.STAFFID " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tlc.DISPUTINFO = tld.DISPUTEID " +
            "WHERE tlc.CLOSEID = #{closeId}")
    TblLegalCloseinformation findByCloseId(BigDecimal closeId);

    @InsertProvider(type=TblLegalCloseinformationMapperSqlConfig.class,method="addDisputeSettlement")
    @Options(useGeneratedKeys=true, keyProperty="closeid", keyColumn="CLOSEID")
    void addDisputeSettlement(TblLegalCloseinformation closeInfo);

    @UpdateProvider(type=TblLegalCloseinformationMapperSqlConfig.class,method="updateModifyDisputeSettlementModify")
    void updateModifyDisputeSettlementModify(TblLegalCloseinformation oldCloseInfo);

    @Delete("DELETE FROM TBL_LEGAL_CLOSEINFORMATION WHERE CLOSEID = #{closeid}")
    void removeDisputeSettlementRemove(BigDecimal closeid);
}
