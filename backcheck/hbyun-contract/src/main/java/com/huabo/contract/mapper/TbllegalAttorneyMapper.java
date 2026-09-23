package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TbllegalAttorney;
import com.huabo.contract.mappersql.TbllegalAttorneyMapperSqlConfig;

public interface TbllegalAttorneyMapper extends BaseMapper<TbllegalAttorney>{

    
    @Select("SELECT * FROM TBL_LEGAL_ATTORNEY TLA " +
//            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEINFO = TLD.DISPUTEID " +
            "WHERE ID = #{id}")
    TbllegalAttorney findById(BigDecimal id);

    @InsertProvider(type = TbllegalAttorneyMapperSqlConfig.class,method = "addEntity")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    void addEntity(TbllegalAttorney tla);

    @UpdateProvider(type = TbllegalAttorneyMapperSqlConfig.class,method = "updateEntity")
    void updateEntity(TbllegalAttorney tla);

    @Delete("DELETE FROM TBL_LEGAL_ATTORNEY WHERE ID = #{id}")
    void deleteEntityById(BigDecimal id);
    
    @Select("SELECT * FROM TBL_LEGAL_ATTORNEY TLA " +
//          "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEINFO = TLD.DISPUTEID " +
          "WHERE disputeid = #{disputeid}")
    List<TbllegalAttorney> findByDisputeid(BigDecimal disputeid);

	@Select("SELECT * FROM TBL_LEGAL_ATTORNEY TLA " +
			"WHERE ARBITRATIONID = #{arbitrationid}")
	List<TbllegalAttorney> findByArbitrationid(BigDecimal arbitrationid);

	@Select("SELECT * FROM TBL_LEGAL_ATTORNEY TLA " +
			"WHERE LAWSUITID = #{lawsuitid}")
	List<TbllegalAttorney> findByLawsuitid(BigDecimal lawsuitid);

	@Select("SELECT * FROM TBL_LEGAL_ATTORNEY TLA " +
			"WHERE NEGOTIATIONID = #{negotiationid}")
	List<TbllegalAttorney> findByNegotiationid(BigDecimal negotiationid);
}