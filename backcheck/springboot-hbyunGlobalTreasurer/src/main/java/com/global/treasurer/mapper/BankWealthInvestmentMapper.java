package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BankWealthInvestmentQueryDTO;
import com.global.treasurer.entity.TblBankWealthInvestment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 银行理财投资Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Mapper
public interface BankWealthInvestmentMapper extends BaseMapper<TblBankWealthInvestment> {

    /**
     * 根据查询条件查询银行理财投资列表
     */
    List<TblBankWealthInvestment> selectByQueryDTO(BankWealthInvestmentQueryDTO queryDTO);

    /**
     * 根据投资编号查询
     */
    TblBankWealthInvestment selectByInvestmentNo(@Param("investmentNo") String investmentNo);
}
