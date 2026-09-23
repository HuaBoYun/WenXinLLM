package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblCreditLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 授信额度Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface CreditLimitMapper extends BaseMapper<TblCreditLimit> {

    /**
     * 分页查询授信额度列表
     *
     * @param params 查询参数
     * @return 授信额度列表
     */
    List<TblCreditLimit> selectLimitList(Map<String, Object> params);

    /**
     * 根据ID查询授信额度详情
     *
     * @param limitId 额度ID
     * @return 授信额度
     */
    TblCreditLimit selectLimitById(@Param("limitId") Long limitId);

    /**
     * 根据合同ID查询额度列表
     *
     * @param contractId 合同ID
     * @return 额度列表
     */
    List<TblCreditLimit> selectByContractId(@Param("contractId") Long contractId);

    /**
     * 更新额度状态
     *
     * @param limitId 额度ID
     * @param status 状态
     * @return 影响行数
     */
    int updateLimitStatus(@Param("limitId") Long limitId, @Param("status") String status);

    /**
     * 更新已用额度
     *
     * @param limitId 额度ID
     * @param usedLimit 已用额度
     * @return 影响行数
     */
    int updateUsedLimit(@Param("limitId") Long limitId, @Param("usedLimit") BigDecimal usedLimit);

    /**
     * 冻结额度
     *
     * @param limitId 额度ID
     * @param frozenAmount 冻结金额
     * @return 影响行数
     */
    int freezeLimit(@Param("limitId") Long limitId, @Param("frozenAmount") BigDecimal frozenAmount);

    /**
     * 解冻额度
     *
     * @param limitId 额度ID
     * @param unfreezeAmount 解冻金额
     * @return 影响行数
     */
    int unfreezeLimit(@Param("limitId") Long limitId, @Param("unfreezeAmount") BigDecimal unfreezeAmount);

    /**
     * 批量删除授信额度（逻辑删除）
     *
     * @param limitIds 额度ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("limitIds") List<Long> limitIds);

    /**
     * 查询授信额度汇总
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> selectLimitSummary(@Param("companyId") Long companyId);
}

