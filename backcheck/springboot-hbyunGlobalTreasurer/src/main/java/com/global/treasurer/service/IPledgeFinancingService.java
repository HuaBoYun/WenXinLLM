package com.global.treasurer.service;

import com.global.treasurer.dto.BillPledgeFinancingDTO;
import com.global.treasurer.entity.TblPledgeFinancing;
import com.global.treasurer.vo.PledgeFinancingRecordVO;

import java.util.List;

/**
 * 质押融资Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface IPledgeFinancingService {

    /**
     * 查询融资记录列表
     *
     * @param poolId 票据池ID
     * @return 融资记录列表
     */
    List<PledgeFinancingRecordVO> selectFinancingRecordsByPoolId(Long poolId);

    /**
     * 创建质押融资
     *
     * @param dto 质押融资DTO
     * @return 融资实体
     */
    TblPledgeFinancing insertPledgeFinancing(BillPledgeFinancingDTO dto);

    /**
     * 更新质押融资
     *
     * @param dto 质押融资DTO
     * @return 融资实体
     */
    TblPledgeFinancing updatePledgeFinancing(BillPledgeFinancingDTO dto);

    /**
     * 删除质押融资
     *
     * @param financingIds 融资ID数组
     * @return 是否成功
     */
    boolean deletePledgeFinancing(Long[] financingIds);

    /**
     * 执行质押融资
     *
     * @param dto 质押融资DTO
     * @return 是否成功
     */
    boolean executePledgeFinancing(BillPledgeFinancingDTO dto);

    /**
     * 处理还款
     *
     * @param financingId 融资ID
     * @return 是否成功
     */
    boolean processRepayment(Long financingId);
}

