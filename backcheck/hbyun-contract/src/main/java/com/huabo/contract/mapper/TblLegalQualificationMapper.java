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
import com.huabo.contract.entity.TblLegalQualification;
import com.huabo.contract.mappersql.TblLegalQualificationMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
public interface TblLegalQualificationMapper extends BaseMapper<TblLegalQualification> {

	@SelectProvider(type=TblLegalQualificationMapperSqlConfig.class,method="findListByPageInfo")
	IPage<TblLegalQualification> findListByPageInfo(IPage<TblLegalQualification> page,TblLegalQualification qualification, BigDecimal disputeid);

    @Select("select * from TBL_LEGAL_QUALIFICATION tlq " +
            "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tlq.DISPUTEINFO = tld.DISPUTEID " +
            "WHERE QUALID = #{qualId}")
    TblLegalQualification findByQualId(BigDecimal qualId);

    @InsertProvider(type=TblLegalQualificationMapperSqlConfig.class,method="saveQualification")
    @Options(useGeneratedKeys=true, keyProperty="qualid", keyColumn="QUALID")
    void saveQualification(TblLegalQualification qualification);

    @UpdateProvider(type=TblLegalQualificationMapperSqlConfig.class,method="updateModifyQualification")
    void updateModifyQualification(TblLegalQualification old);

    @Delete("DELETE FROM TBL_LEGAL_QUALIFICATION WHERE QUALID = #{qualId}")
    void removeQualification(BigDecimal qualId);

	
}
