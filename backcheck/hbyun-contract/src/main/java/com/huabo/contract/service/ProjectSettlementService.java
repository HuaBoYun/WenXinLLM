package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.vo.ProjectSettlementQueryParam;
import com.huabo.contract.entity.ProjectSettlement;
import com.huabo.contract.mapper.ProjectSettlementMapper.ProjectSettlementStatistics;

import java.util.List;

/**
 * 项目结算服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface ProjectSettlementService extends IService<ProjectSettlement> {

    /**
     * 分页查询项目结算列表
     * 
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<ProjectSettlement> getSettlementPage(ProjectSettlementQueryParam queryParam);

    /**
     * 创建项目结算
     * 
     * @param settlement 结算信息
     * @return 创建结果
     */
    ProjectSettlement createSettlement(ProjectSettlement settlement);

    /**
     * 更新项目结算
     * 
     * @param settlement 结算信息
     * @return 更新结果
     */
    ProjectSettlement updateSettlement(ProjectSettlement settlement);

    /**
     * 删除项目结算
     * 
     * @param id 结算ID
     * @return 删除结果
     */
    boolean deleteSettlement(Long id);

    /**
     * 根据ID查询结算详情
     * 
     * @param id 结算ID
     * @return 结算详情
     */
    ProjectSettlement getSettlementById(Long id);

    /**
     * 结算审核
     * 
     * @param id 结算ID
     * @param reviewComments 审核意见
     * @param reviewerId 审核人ID
     * @return 审核结果
     */
    boolean reviewSettlement(Long id, String reviewComments, Long reviewerId);

    /**
     * 获取项目结算统计信息
     * 
     * @param projectId 项目ID
     * @return 统计信息
     */
    ProjectSettlementStatistics getSettlementStatistics(Long projectId);

    /**
     * 获取待审核的结算记录
     * 
     * @return 待审核结算列表
     */
    List<ProjectSettlement> getPendingReviewSettlements();

    /**
     * 生成结算编号
     * 
     * @return 结算编号
     */
    String generateSettlementNo();

    /**
     * 验证结算编号是否唯一
     * 
     * @param settlementNo 结算编号
     * @return 是否唯一
     */
    boolean isSettlementNoUnique(String settlementNo);

    /**
     * 批量审核结算
     * 
     * @param ids 结算ID列表
     * @param reviewComments 审核意见
     * @param reviewerId 审核人ID
     * @return 审核结果
     */
    boolean batchReviewSettlements(List<Long> ids, String reviewComments, Long reviewerId);
}
