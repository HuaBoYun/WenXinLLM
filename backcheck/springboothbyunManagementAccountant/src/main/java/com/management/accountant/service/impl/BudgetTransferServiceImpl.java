package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetTransfer;
import com.management.accountant.oracle.mapper.budget.BudgetTransferMapper;
import com.management.accountant.service.BudgetTransferService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 预算转移Service实现类
 * 
 * @description 预算转移业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetTransferServiceImpl implements BudgetTransferService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetTransferMapper transferMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTransfer create(BudgetTransfer transfer) {
        if (transfer == null) {
            throw new ServiceException("转移信息不能为空");
        }
        if (transfer.getTransferAmount() == null || transfer.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("转移金额必须大于0");
        }

        if (!StringUtils.hasText(transfer.getTransferCode())) {
            transfer.setTransferCode(generateTransferCode());
        }

        if (transfer.getDelFlag() == null) {
            transfer.setDelFlag(0);
        }
        if (!StringUtils.hasText(transfer.getTransferStatus())) {
            transfer.setTransferStatus("PENDING");
        }
        if (!StringUtils.hasText(transfer.getApprovalStatus())) {
            transfer.setApprovalStatus("PENDING");
        }
        if (transfer.getApplyDate() == null) {
            transfer.setApplyDate(new Date());
        }
        transfer.setCreateTime(new Date());
        transfer.setUpdateTime(new Date());

        int result = transferMapper.insert(transfer);
        if (result <= 0) {
            throw new ServiceException("创建转移申请失败");
        }

        log.info("创建转移申请成功，ID: {}", transfer.getTransferId());
        return transfer;
    }

    @Override
    public BudgetTransfer getById(String transferId) {
        if (!StringUtils.hasText(transferId)) {
            throw new ServiceException("转移ID不能为空");
        }
        
        QueryWrapper<BudgetTransfer> wrapper = new QueryWrapper<>();
        wrapper.eq("TRANSFER_ID", transferId)
               .eq("DEL_FLAG", 0);
        
        return transferMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetTransfer transfer) {
        if (transfer == null || !StringUtils.hasText(transfer.getTransferId())) {
            throw new ServiceException("转移ID不能为空");
        }

        BudgetTransfer existing = getById(transfer.getTransferId());
        if (existing == null) {
            throw new ServiceException("转移记录不存在");
        }

        if ("APPROVED".equals(existing.getTransferStatus()) || "EXECUTING".equals(existing.getTransferStatus()) || "COMPLETED".equals(existing.getTransferStatus())) {
            throw new ServiceException("已审批或已执行的转移申请不能修改");
        }

        transfer.setUpdateTime(new Date());
        int result = transferMapper.updateById(transfer);
        if (result <= 0) {
            throw new ServiceException("更新转移申请失败");
        }

        log.info("更新转移申请成功，ID: {}", transfer.getTransferId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String transferId) {
        if (!StringUtils.hasText(transferId)) {
            throw new ServiceException("转移ID不能为空");
        }

        BudgetTransfer transfer = getById(transferId);
        if (transfer == null) {
            throw new ServiceException("转移记录不存在");
        }

        if ("EXECUTING".equals(transfer.getTransferStatus()) || "COMPLETED".equals(transfer.getTransferStatus())) {
            throw new ServiceException("已执行的转移申请不能删除");
        }

        BudgetTransfer update = new BudgetTransfer();
        update.setTransferId(transferId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = transferMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除转移申请失败");
        }

        log.info("删除转移申请成功，ID: {}", transferId);
    }

    @Override
    public PageResult<BudgetTransfer> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetTransfer> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 前端查询条件
        if (params.get("transferTitle") != null && StringUtils.hasText(params.get("transferTitle").toString())) {
            wrapper.like("TRANSFER_TITLE", params.get("transferTitle"));
        }
        if (params.get("transferType") != null && StringUtils.hasText(params.get("transferType").toString())) {
            wrapper.eq("TRANSFER_TYPE", params.get("transferType"));
        }
        if (params.get("fromOrganization") != null && StringUtils.hasText(params.get("fromOrganization").toString())) {
            wrapper.eq("FROM_ORGANIZATION_ID", params.get("fromOrganization"));
        }
        if (params.get("toOrganization") != null && StringUtils.hasText(params.get("toOrganization").toString())) {
            wrapper.eq("TO_ORGANIZATION_ID", params.get("toOrganization"));
        }
        if (params.get("transferStatus") != null && StringUtils.hasText(params.get("transferStatus").toString())) {
            wrapper.eq("TRANSFER_STATUS", params.get("transferStatus"));
        }
        if (params.get("transferCode") != null && StringUtils.hasText(params.get("transferCode").toString())) {
            wrapper.like("TRANSFER_CODE", params.get("transferCode"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetTransfer> page = new Page<>(pageNum, pageSize);
        IPage<BudgetTransfer> pageResult = transferMapper.selectPage(page, wrapper);

        PageResult<BudgetTransfer> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Map<String, Object> params) {
        String transferId = (String) params.get("transferId");
        String remark = params.get("remark") != null ? params.get("remark").toString() : "";

        if (!StringUtils.hasText(transferId)) {
            throw new ServiceException("转移ID不能为空");
        }

        BudgetTransfer transfer = getById(transferId);
        if (transfer == null) {
            throw new ServiceException("转移记录不存在");
        }

        if (!"PENDING".equals(transfer.getTransferStatus())) {
            throw new ServiceException("只能审批待审批状态的转移申请");
        }

        BudgetTransfer update = new BudgetTransfer();
        update.setTransferId(transferId);
        update.setTransferStatus("APPROVED");
        update.setApprovalStatus("APPROVED");
        update.setApproveRemark(remark);
        update.setApproveDate(new Date());
        update.setApproveTime(new Date());
        update.setUpdateTime(new Date());

        int result = transferMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("审批转移申请失败");
        }

        log.info("审批转移申请成功，ID: {}", transferId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String transferId) {
        if (!StringUtils.hasText(transferId)) {
            throw new ServiceException("转移ID不能为空");
        }

        BudgetTransfer transfer = getById(transferId);
        if (transfer == null) {
            throw new ServiceException("转移记录不存在");
        }

        if (!"APPROVED".equals(transfer.getTransferStatus())) {
            throw new ServiceException("只能执行已审批的转移申请");
        }

        // TODO: 实际的预算转移逻辑
        // 1. 从源预算扣减金额
        // 2. 向目标预算增加金额
        // 3. 记录转移历史

        BudgetTransfer update = new BudgetTransfer();
        update.setTransferId(transferId);
        update.setTransferStatus("COMPLETED");
        update.setTransferDate(new Date());
        update.setExecuteTime(new Date());
        update.setUpdateTime(new Date());

        int result = transferMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("执行转移失败");
        }

        log.info("执行转移成功，ID: {}", transferId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchTransfer(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> transferIds = (List<String>) params.get("transferIds");

        if (transferIds == null || transferIds.isEmpty()) {
            throw new ServiceException("转移ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String transferId : transferIds) {
            try {
                execute(transferId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(transferId);
                log.error("批量转移失败，转移ID: {}", transferId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", transferIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量转移完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<BudgetTransfer> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetTransfer> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 优先按 ids 筛选（导出选中行）
        if (params.get("ids") != null && StringUtils.hasText(params.get("ids").toString())) {
            String idsStr = params.get("ids").toString();
            List<String> idList = Arrays.asList(idsStr.split(","));
            wrapper.in("TRANSFER_ID", idList);
        } else {
            // 应用查询条件
            if (params.get("transferStatus") != null && StringUtils.hasText(params.get("transferStatus").toString())) {
                wrapper.eq("TRANSFER_STATUS", params.get("transferStatus"));
            }
            if (params.get("transferTitle") != null && StringUtils.hasText(params.get("transferTitle").toString())) {
                wrapper.like("TRANSFER_TITLE", params.get("transferTitle"));
            }
            if (params.get("transferType") != null && StringUtils.hasText(params.get("transferType").toString())) {
                wrapper.eq("TRANSFER_TYPE", params.get("transferType"));
            }
            if (params.get("fromOrganization") != null && StringUtils.hasText(params.get("fromOrganization").toString())) {
                wrapper.eq("FROM_ORGANIZATION_ID", params.get("fromOrganization"));
            }
            if (params.get("toOrganization") != null && StringUtils.hasText(params.get("toOrganization").toString())) {
                wrapper.eq("TO_ORGANIZATION_ID", params.get("toOrganization"));
            }
        }

        wrapper.orderByDesc("CREATE_TIME");

        // 如果有分页参数且无ids，限制导出数量
        if (params.get("ids") == null && params.get("pageSize") != null) {
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = Integer.parseInt(params.get("pageSize").toString());
            Page<BudgetTransfer> page = new Page<>(pageNum, pageSize);
            IPage<BudgetTransfer> pageResult = transferMapper.selectPage(page, wrapper);
            return pageResult.getRecords();
        }

        return transferMapper.selectList(wrapper);
    }

    private String generateTransferCode() {
        return "TRF" + System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            QueryWrapper<BudgetTransfer> baseWrapper = new QueryWrapper<>();
            baseWrapper.eq("DEL_FLAG", 0);
            Integer totalCount = transferMapper.selectCount(baseWrapper).intValue();
            statistics.put("totalCount", totalCount);

            QueryWrapper<BudgetTransfer> approvedWrapper = new QueryWrapper<>();
            approvedWrapper.eq("DEL_FLAG", 0).eq("TRANSFER_STATUS", "APPROVED");
            Integer approvedCount = transferMapper.selectCount(approvedWrapper).intValue();
            statistics.put("approvedCount", approvedCount);

            QueryWrapper<BudgetTransfer> completedWrapper = new QueryWrapper<>();
            completedWrapper.eq("DEL_FLAG", 0).eq("TRANSFER_STATUS", "COMPLETED");
            Integer completedCount = transferMapper.selectCount(completedWrapper).intValue();
            statistics.put("completedCount", completedCount);

            QueryWrapper<BudgetTransfer> pendingWrapper = new QueryWrapper<>();
            pendingWrapper.eq("DEL_FLAG", 0).eq("TRANSFER_STATUS", "PENDING");
            Integer pendingCount = transferMapper.selectCount(pendingWrapper).intValue();
            statistics.put("pendingCount", pendingCount);

            statistics.put("processingCount", totalCount - completedCount - pendingCount);

            // 计算已转移总金额
            QueryWrapper<BudgetTransfer> amountWrapper = new QueryWrapper<>();
            amountWrapper.eq("DEL_FLAG", 0).eq("TRANSFER_STATUS", "COMPLETED");
            List<BudgetTransfer> completedList = transferMapper.selectList(amountWrapper);
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (BudgetTransfer t : completedList) {
                if (t.getTransferAmount() != null) {
                    totalAmount = totalAmount.add(t.getTransferAmount());
                }
            }
            statistics.put("transferredAmount", totalAmount);

            // 转移率 = 已完成 / 总数
            double transferRate = totalCount > 0 ? Math.round((completedCount * 100.0 / totalCount) * 10) / 10.0 : 0;
            statistics.put("transferRate", transferRate);
        } catch (Exception e) {
            log.error("获取转移统计信息异常", e);
        }
        return statistics;
    }

    @Override
    public boolean updateById(BudgetTransfer transfer) {
        return transferMapper.updateById(transfer) > 0;
    }

    @Override
    public boolean save(BudgetTransfer transfer) {
        return transferMapper.insert(transfer) > 0;
    }
}

