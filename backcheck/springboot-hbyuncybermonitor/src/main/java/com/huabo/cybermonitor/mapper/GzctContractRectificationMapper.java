package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.GzctContractRectification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GzctContractRectificationMapper extends BaseMapper<GzctContractRectification> {

    /**
     * 根据合同编号查询整改记录
     */
    @Select("SELECT * FROM GZCT_CONTRACT_RECTIFICATION WHERE CONTRACT_NO = #{contractNo} ORDER BY CREATE_TIME DESC")
    List<GzctContractRectification> selectByContractNo(@Param("contractNo") String contractNo);

    /**
     * 根据合同ID查询最新整改记录
     */
    @Select("SELECT * FROM GZCT_CONTRACT_RECTIFICATION WHERE CONTRACT_ID = #{contractId} ORDER BY CREATE_TIME DESC")
    List<GzctContractRectification> selectByContractId(@Param("contractId") String contractId);
}
