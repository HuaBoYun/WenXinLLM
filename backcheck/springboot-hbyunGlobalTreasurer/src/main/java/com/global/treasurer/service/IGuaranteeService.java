package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeDTO;
import com.global.treasurer.dto.GuaranteeQueryDTO;
import com.global.treasurer.entity.TblGuarantee;
import com.global.treasurer.vo.GuaranteeVO;

import java.util.Map;

/**
 * 保函Service接口
 *
 * 数据库表 TBL_GUARANTEE 主键 GUARANTEE_ID 为 VARCHAR2(10) 类型
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface IGuaranteeService {

    /**
     * 分页查询保函列表
     */
    PageInfo<GuaranteeVO> selectGuaranteeList(GuaranteeQueryDTO queryDTO);

    /**
     * 根据ID查询保函详情
     * @param guaranteeId 保函ID (String类型，对应数据库VARCHAR2)
     */
    GuaranteeVO selectGuaranteeById(String guaranteeId);

    /**
     * 新增保函申请
     */
    TblGuarantee insertGuarantee(GuaranteeDTO dto);

    /**
     * 修改保函申请
     */
    TblGuarantee updateGuarantee(GuaranteeDTO dto);

    /**
     * 批量删除保函
     * @param guaranteeIds 保函ID数组 (String类型)
     */
    boolean deleteGuaranteeByIds(String[] guaranteeIds);

    /**
     * 提交保函申请
     */
    boolean submitGuaranteeApplication(String guaranteeId);

    /**
     * 保函申请审批
     */
    boolean approveGuaranteeApplication(String guaranteeId, Map<String, Object> approvalData);

    /**
     * 开立保函
     */
    boolean issueGuarantee(String guaranteeId);

    /**
     * 索赔保函
     */
    boolean claimGuarantee(String guaranteeId, Map<String, Object> claimData);

    /**
     * 解除保函
     */
    boolean releaseGuarantee(String guaranteeId, String releaseReason);

    /**
     * 取消保函
     */
    boolean cancelGuarantee(String guaranteeId, String cancelReason);
}

