package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalFrozenaccount;
import com.huabo.contract.mappersql.TblLegalFrozenaccountMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-23
 */
public interface TblLegalFrozenaccountMapper extends BaseMapper<TblLegalFrozenaccount> {


    @SelectProvider(type = TblLegalFrozenaccountMapperSqlConfig.class,method = "findListByPageInfo")
    IPage<TblLegalFrozenaccount> findListByPageInfo(IPage<TblLegalFrozenaccount> page,TblLegalFrozenaccount frozenAccount, BigDecimal disputeid);

    @Select("select * from TBL_LEGAL_FROZENACCOUNT tlf LEFT JOIN TBL_STAFF ts on tlf.CREATESTAFF = ts.STAFFID WHERE tlf.INFORID = #{inforId}")
    TblLegalFrozenaccount findByInforid(BigDecimal inforId);

    @InsertProvider(type = TblLegalFrozenaccountMapperSqlConfig.class,method = "saveFrozenAccount")
    void saveFrozenAccount(TblLegalFrozenaccount frozen);

    @UpdateProvider(type = TblLegalFrozenaccountMapperSqlConfig.class,method = "updateModifyFrozenAccount")
    void updateModifyFrozenAccount(TblLegalFrozenaccount oldFrozen);

    @Delete("DELETE FROM TBL_LEGAL_FROZENACCOUNT WHERE INFORID = #{inforId}")
    void removeFrozenAccount(BigDecimal inforId);

    @Select("select * from TBL_LEGAL_FROZENACCOUNT tlf " +
            "LEFT JOIN TBL_STAFF ts on tlf.CREATESTAFF = ts.STAFFID " +
            "LEFT JOIN TBL_LEGAL_PROCEEDINGSRECORD tlp on tlf.PROCEEDINFO = tlp.PROCEEDID " +
            "LEFT JOIN TBL_LEGAL_LITIGATIONSETTLEMENT tll ON tlp.LITIGATIONINFO = tll.LITIGATIONID " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tll.DISPUTEINFO = tld.DISPUTEID " +
            "where tlf.INFORID = #{inforId}")
    TblLegalFrozenaccount findInforid(BigDecimal inforId);
}
