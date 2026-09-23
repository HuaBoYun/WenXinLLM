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
import com.huabo.contract.entity.TblLegalAssetporotect;
import com.huabo.contract.mappersql.TblLegalAssetporotectMapperSqlConfig;

public interface TblLegalAssetporotectMapper extends BaseMapper<TblLegalAssetporotect>{

	@SelectProvider(type=TblLegalAssetporotectMapperSqlConfig.class,method="findListByPage")
	IPage<TblLegalAssetporotect> findListByPage(IPage<TblLegalAssetporotect> page, BigDecimal litigationid, BigDecimal arbitraid);
    
    @Select("SELECT TLA.*,TLD.DISPUTEITEM FROM TBL_LEGAL_ASSETPOROTECT TLA " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLA.DISPUTEID = TLD.DISPUTEID " +
            "WHERE ID = #{id}")
    TblLegalAssetporotect findById(BigDecimal id);

    @InsertProvider(type = TblLegalAssetporotectMapperSqlConfig.class,method = "addEntity")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    void addEntity(TblLegalAssetporotect tla);

    @UpdateProvider(type = TblLegalAssetporotectMapperSqlConfig.class,method = "updateEntity")
    void updateEntity(TblLegalAssetporotect tla);

    @Delete("DELETE FROM TBL_LEGAL_ASSETPOROTECT WHERE ID = #{id}")
    void deleteEntityById(BigDecimal id);

}
