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
import com.huabo.contract.entity.TblLegalExecumgr;
import com.huabo.contract.mappersql.TblLegalExecumgrMapperSqlConfig;

public interface TblLegalExecumgrMapper extends BaseMapper<TblLegalExecumgr>{

	@SelectProvider(type=TblLegalExecumgrMapperSqlConfig.class,method="findListByPage")
	IPage<TblLegalExecumgr> findListByPage(IPage<TblLegalExecumgr> page, BigDecimal litigationid, BigDecimal arbitraid,TblLegalExecumgr tla, String companyIds);

    @Select("SELECT * FROM TBL_LEGAL_EXECUMGR TLA " +
//            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION TLD ON TLL.DISPUTEINFO = TLD.DISPUTEID " +
            "WHERE ID = #{id}")
    TblLegalExecumgr findById(BigDecimal id);

    @InsertProvider(type = TblLegalExecumgrMapperSqlConfig.class,method = "addEntity")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
    void addEntity(TblLegalExecumgr tla);

    @UpdateProvider(type = TblLegalExecumgrMapperSqlConfig.class,method = "updateEntity")
    void updateEntity(TblLegalExecumgr tla);

    @Delete("DELETE FROM TBL_LEGAL_EXECUMGR WHERE ID = #{id}")
    void deleteEntityById(BigDecimal id);
   
    @Select("SELECT * FROM TBL_LEGAL_EXECUMGR  WHERE DISPUTEID = #{disputeid}")
    List<TblLegalExecumgr> findListBydisputeid(BigDecimal disputeid);

}