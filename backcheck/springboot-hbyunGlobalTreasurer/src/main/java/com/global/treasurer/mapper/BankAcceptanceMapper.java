package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BankAcceptanceQueryDTO;
import com.global.treasurer.entity.TblBankAcceptance;
import com.global.treasurer.vo.BankAcceptanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 银行承兑汇票Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface BankAcceptanceMapper extends BaseMapper<TblBankAcceptance> {

    /**
     * 查询银行承兑汇票列表
     *
     * @param queryDTO 查询条件
     * @return 银行承兑汇票列表
     */
    List<BankAcceptanceVO> selectBankAcceptanceList(BankAcceptanceQueryDTO queryDTO);

    /**
     * 根据ID查询银行承兑汇票详情
     *
     * @param acceptanceId 承兑汇票ID
     * @return 银行承兑汇票详情
     */
    BankAcceptanceVO selectBankAcceptanceById(@Param("acceptanceId") Long acceptanceId);

    /**
     * 批量删除银行承兑汇票
     *
     * @param acceptanceIds 承兑汇票ID数组
     * @return 影响行数
     */
    int deleteBankAcceptanceByIds(@Param("acceptanceIds") Long[] acceptanceIds);
}

