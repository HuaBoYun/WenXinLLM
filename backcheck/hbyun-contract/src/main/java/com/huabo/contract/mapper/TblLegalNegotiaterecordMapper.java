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
import com.huabo.contract.entity.TblLegalNegotiaterecord;
import com.huabo.contract.mappersql.TblLegalNegotiaterecordMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-18
 */
public interface TblLegalNegotiaterecordMapper extends BaseMapper<TblLegalNegotiaterecord> {

	@Select("SELECT * FROM TBL_LEGAL_NEGOTIATERECORD WHERE NEGOTIATEINFO = #{negotiaId}")
	List<TblLegalNegotiaterecord> findListBynegotiaId(BigDecimal negotiaId) throws Exception;

	@Delete("DELETE FROM TBL_LEGAL_NEGOTIATERECORD WHERE RECORDID = #{recordId}")
	void removeNegitiateRecord(BigDecimal recordId);

	@InsertProvider(type=TblLegalNegotiaterecordMapperSqlConfig.class,method="saveNegotiateRecord")
	@Options(useGeneratedKeys=true, keyProperty="recordid", keyColumn="RECORDID")
	void saveNegotiateRecord(TblLegalNegotiaterecord record) throws Exception;

	@Select("SELECT * FROM TBL_LEGAL_NEGOTIATERECORD WHERE RECORDID = #{recordId}")
	TblLegalNegotiaterecord findById(BigDecimal recordId) throws Exception;

	 @SelectProvider(type=TblLegalNegotiaterecordMapperSqlConfig.class,method="findByNegotiaid")
	IPage<TblLegalNegotiaterecord> findByNegotiaid(IPage<TblLegalNegotiaterecord> page, BigDecimal negotiaId) throws Exception;

	 @UpdateProvider(type=TblLegalNegotiaterecordMapperSqlConfig.class,method="updateModifyNegotiateRecord")
	void updateModifyNegotiateRecord(TblLegalNegotiaterecord oldRecord) throws Exception;


}
