package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblLegalCloseSum;
import com.huabo.contract.mappersql.TblLegalCloseSumMapperSqlConfig;

public interface TblLegalCloseSumMapper extends BaseMapper<TblLegalCloseSum>{

    
    @Select("SELECT * FROM TBL_LEGAL_CLOSESUM TLA " +
//            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEINFO = TLD.DISPUTEID " +
            "WHERE ID = #{id}")
    TblLegalCloseSum findById(BigDecimal id);

    @InsertProvider(type = TblLegalCloseSumMapperSqlConfig.class,method = "addEntity")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    void addEntity(TblLegalCloseSum tla);

    @UpdateProvider(type = TblLegalCloseSumMapperSqlConfig.class,method = "updateEntity")
    void updateEntity(TblLegalCloseSum tla);

    @Delete("DELETE FROM TBL_LEGAL_CLOSESUM WHERE ID = #{id}")
    void deleteEntityById(BigDecimal id);

    
    @Select("SELECT * FROM TBL_LEGAL_CLOSESUM TLA " +
          "WHERE litigationid = #{litigationid}")
    TblLegalCloseSum findByLitigationid(BigDecimal litigationid);
    
    @Select("SELECT * FROM TBL_LEGAL_CLOSESUM TLA " +
          "WHERE arbitraid = #{arbitraid}")
    TblLegalCloseSum findByAarbitraid(BigDecimal arbitraid);
}