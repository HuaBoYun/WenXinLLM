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
import com.huabo.contract.entity.TblLegalArbitrationrecord;
import com.huabo.contract.mappersql.TblLegalArbitrationrecordMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
public interface TblLegalArbitrationrecordMapper extends BaseMapper<TblLegalArbitrationrecord> {

    @Select("SELECT * FROM TBL_LEGAL_ARBITRATIONRECORD WHERE ARBITRATIONINFO = #{arbitraId}")
    List<TblLegalArbitrationrecord> findListBynegotiaId(BigDecimal arbitraId) throws Exception;

    @InsertProvider(type = TblLegalArbitrationrecordMapperSqlConfig.class, method = "saveNegotiateRecord")
    @Options(useGeneratedKeys=true, keyProperty="arrecordid", keyColumn="ARRECORDID")
    void saveNegotiateRecord(TblLegalArbitrationrecord record) throws Exception;

    @Select("SELECT * FROM TBL_LEGAL_ARBITRATIONRECORD WHERE ARRECORDID = #{arrecordId}")
    TblLegalArbitrationrecord findByArrecordId(BigDecimal arrecordId) throws Exception;

    @UpdateProvider(type = TblLegalArbitrationrecordMapperSqlConfig.class, method = "modifyNegotiateRecord")
    void modifyNegotiateRecord(TblLegalArbitrationrecord oldRecord) throws Exception;

    @Delete("DELETE FROM TBL_LEGAL_ARBITRATIONRECORD WHERE ARRECORDID = #{arrecordId}")
    void removeNegitiateRecord(BigDecimal arrecordId);

    @SelectProvider(type = TblLegalArbitrationrecordMapperSqlConfig.class, method = "findListByPageInfo")
	IPage<TblLegalArbitrationrecord> findListByPageInfo(IPage<TblLegalArbitrationrecord> page, BigDecimal arrecordid) throws Exception;
}
