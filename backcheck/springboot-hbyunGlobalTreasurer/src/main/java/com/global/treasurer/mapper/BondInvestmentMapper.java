package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BondInvestmentQueryDTO;
import com.global.treasurer.entity.TblBondInvestment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 债券投资Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Mapper
public interface BondInvestmentMapper extends BaseMapper<TblBondInvestment> {

    /**
     * 根据查询条件查询债券投资列表
     */
    List<TblBondInvestment> selectByQueryDTO(BondInvestmentQueryDTO queryDTO);

    /**
     * 根据投资编号查询
     */
    TblBondInvestment selectByInvestmentNo(@Param("investmentNo") String investmentNo);
}
