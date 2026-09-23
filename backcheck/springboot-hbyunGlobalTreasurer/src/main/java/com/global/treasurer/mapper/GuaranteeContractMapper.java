package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblGuaranteeContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 担保合同Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface GuaranteeContractMapper extends BaseMapper<TblGuaranteeContract> {

    List<TblGuaranteeContract> selectContractList(Map<String, Object> params);

    TblGuaranteeContract selectContractById(@Param("contractId") Long contractId);

    int updateContractStatus(@Param("contractId") Long contractId, @Param("status") String status);

    int batchDeleteByIds(@Param("contractIds") List<Long> contractIds);

    List<TblGuaranteeContract> selectExpiringContracts(@Param("days") Integer days);

    Map<String, Object> selectContractSummary(@Param("companyId") Long companyId);
}

