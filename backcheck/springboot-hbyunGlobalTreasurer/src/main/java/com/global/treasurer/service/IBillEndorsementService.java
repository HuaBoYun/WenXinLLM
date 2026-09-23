package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillEndorsementDTO;
import com.global.treasurer.dto.BillEndorsementQueryDTO;
import com.global.treasurer.entity.TblBillEndorsement;
import com.global.treasurer.vo.BillEndorsementVO;

import java.util.List;
import java.util.Map;

/**
 * 票据背书Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface IBillEndorsementService {

    /**
     * 分页查询票据背书列表
     */
    PageInfo<BillEndorsementVO> selectBillEndorsementList(BillEndorsementQueryDTO queryDTO);

    /**
     * 根据ID查询票据背书详情
     */
    BillEndorsementVO selectBillEndorsementById(Long endorsementId);

    /**
     * 新增票据背书
     */
    TblBillEndorsement insertBillEndorsement(BillEndorsementDTO dto);

    /**
     * 修改票据背书
     */
    TblBillEndorsement updateBillEndorsement(BillEndorsementDTO dto);

    /**
     * 批量删除票据背书
     */
    boolean deleteBillEndorsementByIds(Long[] endorsementIds);

    /**
     * 审批票据背书
     */
    boolean approveBillEndorsement(Long endorsementId, Map<String, Object> approvalData);

    /**
     * 执行票据背书
     */
    boolean executeBillEndorsement(Long endorsementId);

    /**
     * 撤销票据背书
     */
    boolean cancelBillEndorsement(Long endorsementId, String reason);

    /**
     * 批量审批票据背书
     */
    boolean batchApproveBillEndorsement(List<Long> endorsementIds, Map<String, Object> approvalData);

    /**
     * 获取可用于背书的票据列表
     */
    List<Map<String, Object>> getAvailableBillsForEndorsement(Map<String, Object> params);
}

