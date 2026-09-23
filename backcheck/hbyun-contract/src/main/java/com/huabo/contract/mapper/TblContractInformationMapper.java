package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblContractInformation;
import com.huabo.contract.mappersql.TblContractInformationMapperSqlConifg;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-17
 */
public interface TblContractInformationMapper extends BaseMapper<TblContractInformation> {

    @InsertProvider(type=TblContractInformationMapperSqlConifg.class,method="saveContractInfoMation")
    @Options(useGeneratedKeys=true, keyProperty="infoid", keyColumn="INFOID")
    void saveContractInfoMation(TblContractInformation information);

    @UpdateProvider(type=TblContractInformationMapperSqlConifg.class,method="updateContractInfoMation")
    void updateContractInfoMation(TblContractInformation information);

    @Delete("DELETE FROM TBL_CONTRACT_INFORMATION WHERE INFOID = #{infoId}")
    void removeContractInfoMation(BigDecimal infoId);

    @Select("SELECT * FROM TBL_CONTRACT_INFORMATION WHERE PROJECTID = #{contractId}")
    List<TblContractInformation> getInfomationList(String contractId);

    @Select("SELECT * FROM TBL_CONTRACT_INFORMATION WHERE PROJECTID = #{contractid} ")
    List<TblContractInformation> findInformationListById(BigDecimal contractid);
}
