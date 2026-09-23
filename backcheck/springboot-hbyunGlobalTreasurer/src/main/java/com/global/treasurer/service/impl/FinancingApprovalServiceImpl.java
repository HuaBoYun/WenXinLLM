package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingApprovalDTO;
import com.global.treasurer.dto.FinancingApprovalQueryDTO;
import com.global.treasurer.entity.TblFinancingApproval;
import com.global.treasurer.mapper.FinancingApprovalMapper;
import com.global.treasurer.service.FinancingApprovalService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 融资审批服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class FinancingApprovalServiceImpl implements FinancingApprovalService {

    private static final Logger log = LoggerFactory.getLogger(FinancingApprovalServiceImpl.class);

    @Resource
    private FinancingApprovalMapper financingApprovalMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public PageInfo<TblFinancingApproval> getApprovalList(FinancingApprovalQueryDTO queryDTO) {
        try {
            // 分页参数设置
            PageHelper.startPage(queryDTO.getPageNo(), queryDTO.getPageSize());

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (queryDTO.getApprovalId() != null) {
                params.put("approvalId", queryDTO.getApprovalId());
            }
            if (StringUtils.hasText(queryDTO.getFinancingType())) {
                params.put("financingType", queryDTO.getFinancingType());
            }
            if (StringUtils.hasText(queryDTO.getFinancingNo())) {
                params.put("financingNo", queryDTO.getFinancingNo());
            }
            if (queryDTO.getCompanyId() != null) {
                params.put("companyId", queryDTO.getCompanyId());
            }
            if (queryDTO.getApplicantId() != null) {
                params.put("applicantId", queryDTO.getApplicantId());
            }
            if (StringUtils.hasText(queryDTO.getApprovalStatus())) {
                params.put("approvalStatus", queryDTO.getApprovalStatus());
            }
            if (StringUtils.hasText(queryDTO.getCurrentApprovalNode())) {
                params.put("currentApprovalNode", queryDTO.getCurrentApprovalNode());
            }
            if (queryDTO.getApplicationTimeStart() != null) {
                params.put("applicationTimeStart", queryDTO.getApplicationTimeStart());
            }
            if (queryDTO.getApplicationTimeEnd() != null) {
                params.put("applicationTimeEnd", queryDTO.getApplicationTimeEnd());
            }
            if (StringUtils.hasText(queryDTO.getKeyword())) {
                params.put("keyword", queryDTO.getKeyword());
            }
            params.put("deleted", 0);

            // 查询列表
            List<TblFinancingApproval> list = financingApprovalMapper.selectApprovalList(params);
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询融资审批列表失败", e);
            throw new RuntimeException("查询融资审批列表失败: " + e.getMessage());
        }
    }

    @Override
    public TblFinancingApproval getApprovalById(Long approvalId) {
        try {
            if (approvalId == null) {
                throw new IllegalArgumentException("审批ID不能为空");
            }
            return financingApprovalMapper.selectApprovalById(approvalId);
        } catch (Exception e) {
            log.error("获取融资审批详情失败, approvalId: {}", approvalId, e);
            throw new RuntimeException("获取融资审批详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingApproval submitApproval(FinancingApprovalDTO dto) {
        try {
            // 获取当前用户信息
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null) {
                throw new RuntimeException("获取当前用户信息失败");
            }

            // 检查是否已存在审批记录
            TblFinancingApproval existing = financingApprovalMapper.selectByFinancingId(
                dto.getFinancingId(), dto.getFinancingType());
            if (existing != null && "PENDING".equals(existing.getApprovalStatus())) {
                throw new RuntimeException("该融资申请已在审批流程中,请勿重复提交");
            }

            // 创建审批记录
            TblFinancingApproval approval = new TblFinancingApproval();
            approval.setFinancingType(dto.getFinancingType());
            approval.setFinancingId(dto.getFinancingId());
            approval.setFinancingNo(dto.getFinancingNo());
            approval.setCompanyId(dto.getCompanyId());
            approval.setCompanyName(dto.getCompanyName());
            approval.setFinancingAmount(dto.getFinancingAmount());
            approval.setCurrencyCode(dto.getCurrencyCode());
            approval.setFinancingTerm(dto.getFinancingTerm());
            approval.setTermUnit(dto.getTermUnit());
            approval.setInterestRate(dto.getInterestRate());
            approval.setRepaymentMethod(dto.getRepaymentMethod());
            approval.setGuaranteeMethod(dto.getGuaranteeMethod());
            approval.setFundPurpose(dto.getFundPurpose());
            approval.setApplicantId(currentUser.getStaffid().longValue());
            approval.setApplicantName(currentUser.getRealname());
            approval.setApplicationTime(new Date());
            approval.setCurrentApprovalNode("DEPT_MANAGER"); // 默认从部门经理审批开始
            approval.setApprovalStatus("PENDING");
            approval.setAttachmentPath(dto.getAttachmentPath());
            approval.setRemarks(dto.getRemarks());
            approval.setCreateBy(currentUser.getStaffid().longValue());
            approval.setCreateTime(new Date());
            approval.setUpdateBy(currentUser.getStaffid().longValue());
            approval.setUpdateTime(new Date());
            approval.setDeleted(0);
            approval.setTenantId(currentUser.getCurrentOrg() != null ? currentUser.getCurrentOrg().getOrgid().longValue() : null);

            // 保存到数据库
            financingApprovalMapper.insert(approval);

            log.info("提交融资审批成功, approvalId: {}, financingId: {}, financingType: {}",
                    approval.getApprovalId(), dto.getFinancingId(), dto.getFinancingType());

            return approval;
        } catch (Exception e) {
            log.error("提交融资审批失败", e);
            throw new RuntimeException("提交融资审批失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long approvalId, String comments) {
        try {
            // 获取当前用户信息
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null) {
                throw new RuntimeException("获取当前用户信息失败");
            }

            // 查询审批记录
            TblFinancingApproval approval = financingApprovalMapper.selectApprovalById(approvalId);
            if (approval == null) {
                throw new RuntimeException("审批记录不存在");
            }

            if (!"PENDING".equals(approval.getApprovalStatus())) {
                throw new RuntimeException("该审批已处理,状态为: " + approval.getApprovalStatus());
            }

            // 更新审批状态为已通过
            approval.setApprovalStatus("APPROVED");
            approval.setUpdateBy(currentUser.getStaffid().longValue());
            approval.setUpdateTime(new Date());

            financingApprovalMapper.updateById(approval);

            log.info("审批通过成功, approvalId: {}, comments: {}", approvalId, comments);
        } catch (Exception e) {
            log.error("审批通过失败, approvalId: {}", approvalId, e);
            throw new RuntimeException("审批通过失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long approvalId, String comments) {
        try {
            // 获取当前用户信息
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null) {
                throw new RuntimeException("获取当前用户信息失败");
            }

            // 查询审批记录
            TblFinancingApproval approval = financingApprovalMapper.selectApprovalById(approvalId);
            if (approval == null) {
                throw new RuntimeException("审批记录不存在");
            }

            if (!"PENDING".equals(approval.getApprovalStatus())) {
                throw new RuntimeException("该审批已处理,状态为: " + approval.getApprovalStatus());
            }

            // 更新审批状态为已拒绝
            approval.setApprovalStatus("REJECTED");
            approval.setRemarks(comments);
            approval.setUpdateBy(currentUser.getStaffid().longValue());
            approval.setUpdateTime(new Date());

            financingApprovalMapper.updateById(approval);

            log.info("审批拒绝成功, approvalId: {}, comments: {}", approvalId, comments);
        } catch (Exception e) {
            log.error("审批拒绝失败, approvalId: {}", approvalId, e);
            throw new RuntimeException("审批拒绝失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getApprovalHistory(Long approvalId) {
        try {
            if (approvalId == null) {
                throw new IllegalArgumentException("审批ID不能为空");
            }
            return financingApprovalMapper.selectApprovalHistory(approvalId);
        } catch (Exception e) {
            log.error("查询审批历史失败, approvalId: {}", approvalId, e);
            throw new RuntimeException("查询审批历史失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchApprove(List<Long> approvalIds, Boolean approved, String comments) {
        try {
            // 获取当前用户信息
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null) {
                throw new RuntimeException("获取当前用户信息失败");
            }

            if (approvalIds == null || approvalIds.isEmpty()) {
                throw new IllegalArgumentException("审批ID列表不能为空");
            }

            String status = approved ? "APPROVED" : "REJECTED";
            int count = financingApprovalMapper.batchUpdateStatus(approvalIds, status);

            log.info("批量审批成功, count: {}, status: {}, comments: {}", count, status, comments);
        } catch (Exception e) {
            log.error("批量审批失败, approvalIds: {}", approvalIds, e);
            throw new RuntimeException("批量审批失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelApproval(Long approvalId) {
        try {
            // 获取当前用户信息
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null) {
                throw new RuntimeException("获取当前用户信息失败");
            }

            // 查询审批记录
            TblFinancingApproval approval = financingApprovalMapper.selectApprovalById(approvalId);
            if (approval == null) {
                throw new RuntimeException("审批记录不存在");
            }

            if (!"PENDING".equals(approval.getApprovalStatus())) {
                throw new RuntimeException("只能撤销待审批状态的申请");
            }

            // 检查是否为申请人本人
            if (currentUser.getStaffid().longValue() != approval.getApplicantId()) {
                throw new RuntimeException("只有申请人才能撤销审批申请");
            }

            // 更新审批状态为已撤销
            approval.setApprovalStatus("CANCELLED");
            approval.setUpdateBy(currentUser.getStaffid().longValue());
            approval.setUpdateTime(new Date());

            financingApprovalMapper.updateById(approval);

            log.info("撤销审批成功, approvalId: {}", approvalId);
        } catch (Exception e) {
            log.error("撤销审批失败, approvalId: {}", approvalId, e);
            throw new RuntimeException("撤销审批失败: " + e.getMessage());
        }
    }

    @Override
    public int countPendingApprovals(Long approverId) {
        try {
            if (approverId == null) {
                return 0;
            }
            return financingApprovalMapper.countPendingApprovals(approverId);
        } catch (Exception e) {
            log.error("统计待审批数量失败, approverId: {}", approverId, e);
            return 0;
        }
    }
}
