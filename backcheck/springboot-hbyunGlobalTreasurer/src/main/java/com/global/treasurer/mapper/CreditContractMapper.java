package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblCreditContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 授信合同Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface CreditContractMapper extends BaseMapper<TblCreditContract> {

    /**
     * 分页查询授信合同列表
     *
     * @param params 查询参数
     * @return 授信合同列表
     */
    List<TblCreditContract> selectContractList(Map<String, Object> params);

    /**
     * 根据ID查询授信合同详情
     *
     * @param contractId 合同ID
     * @return 授信合同
     */
    TblCreditContract selectContractById(@Param("contractId") Long contractId);

    /**
     * 根据合同编号查询
     *
     * @param contractNo 合同编号
     * @return 授信合同
     */
    TblCreditContract selectByContractNo(@Param("contractNo") String contractNo);

    /**
     * 更新合同状态
     *
     * @param contractId 合同ID
     * @param status 状态
     * @return 影响行数
     */
    int updateContractStatus(@Param("contractId") Long contractId, @Param("status") String status);

    /**
     * 更新已用额度
     *
     * @param contractId 合同ID
     * @param usedAmount 已用额度
     * @return 影响行数
     */
    int updateUsedAmount(@Param("contractId") Long contractId, @Param("usedAmount") BigDecimal usedAmount);

    /**
     * 批量删除授信合同（逻辑删除）
     *
     * @param contractIds 合同ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("contractIds") List<Long> contractIds);

    /**
     * 查询即将到期的合同
     *
     * @param days 天数
     * @return 合同列表
     */
    List<TblCreditContract> selectExpiringContracts(@Param("days") Integer days);

    /**
     * 查询授信合同汇总
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> selectContractSummary(@Param("companyId") Long companyId);
}

