package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.GzctContractWarning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 合同风险预警 Mapper
 */
@Mapper
public interface GzctContractWarningMapper extends BaseMapper<GzctContractWarning> {

    /**
     * 根据预警编号查询
     */
    @Select("SELECT * FROM GZCT_CONTRACT_WARNING WHERE WARN_NO = #{warnNo} AND (DEL_FLAG IS NULL OR DEL_FLAG = '0')")
    GzctContractWarning selectByWarnNo(@Param("warnNo") String warnNo);

    /**
     * 根据合同ID查询所有预警
     */
    @Select("SELECT * FROM GZCT_CONTRACT_WARNING WHERE CONTRACT_ID = #{contractId} AND (DEL_FLAG IS NULL OR DEL_FLAG = '0') ORDER BY CREATE_TIME DESC")
    List<GzctContractWarning> selectByContractId(@Param("contractId") String contractId);
}
