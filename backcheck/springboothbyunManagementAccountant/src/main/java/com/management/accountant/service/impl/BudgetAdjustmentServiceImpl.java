package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAdjustment;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.mapper.budget.BudgetAdjustmentMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.service.BudgetAdjustmentService;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预算调整Service实现类
 * 
 * @description 预算调整业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetAdjustmentServiceImpl implements BudgetAdjustmentService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetAdjustmentMapper adjustmentMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取当前登录用户名
     */
    private String getCurrentUserName() {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff != null) {
                String name = staff.getRealname();
                return StringUtils.hasText(name) ? name : staff.getUsername();
            }
        } catch (Exception e) {
            log.debug("获取当前用户失败", e);
        }
        return "admin";
    }

    /**
     * 获取当前登录用户ID
     */
    private String getCurrentUserId() {
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff != null && staff.getStaffid() != null) {
                return staff.getStaffid().toString();
            }
        } catch (Exception e) {
            log.debug("获取当前用户ID失败", e);
        }
        return "0";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetAdjustment create(BudgetAdjustment adjustment) {
        // 1. 参数校验
        if (adjustment == null) {
            throw new ServiceException("调整信息不能为空");
        }
        if (!StringUtils.hasText(adjustment.getAdjustmentType())) {
            throw new ServiceException("调整类型不能为空");
        }
        if (adjustment.getAdjustmentAmount() == null || adjustment.getAdjustmentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("调整金额必须大于0");
        }

        // 2. 生成调整编码
        if (!StringUtils.hasText(adjustment.getAdjustmentCode())) {
            adjustment.setAdjustmentCode(generateAdjustmentCode());
        }

        // 3. 设置默认值
        if (adjustment.getDelFlag() == null) {
            adjustment.setDelFlag(0);
        }
        if (!StringUtils.hasText(adjustment.getAdjustmentStatus())) {
            adjustment.setAdjustmentStatus("DRAFT");
        }
        // 填充组织名称：根据 organizationId 查出 organizationName
        if (StringUtils.hasText(adjustment.getOrganizationId()) && !StringUtils.hasText(adjustment.getOrganizationName())) {
            try {
                QueryWrapper<BudgetOrganization> orgWrapper = new QueryWrapper<>();
                orgWrapper.eq("ORGANIZATION_ID", adjustment.getOrganizationId()).eq("DEL_FLAG", 0);
                BudgetOrganization org = organizationMapper.selectOne(orgWrapper);
                if (org != null && StringUtils.hasText(org.getOrganizationName())) {
                    adjustment.setOrganizationName(org.getOrganizationName());
                }
            } catch (Exception e) {
                log.warn("查询组织名称失败, organizationId={}", adjustment.getOrganizationId(), e);
            }
        }
        // 填充申请人信息
        if (!StringUtils.hasText(adjustment.getApplicantName())) {
            adjustment.setApplicantName(getCurrentUserName());
        }
        if (!StringUtils.hasText(adjustment.getApplicantId())) {
            adjustment.setApplicantId(getCurrentUserId());
        }
        adjustment.setApplyDate(new Date());
        adjustment.setCreateTime(new Date());
        adjustment.setUpdateTime(new Date());

        // 4. 插入数据库
        int result = adjustmentMapper.insert(adjustment);
        if (result <= 0) {
            throw new ServiceException("创建调整申请失败");
        }

        log.info("创建调整申请成功，ID: {}", adjustment.getAdjustmentId());
        return adjustment;
    }

    @Override
    public BudgetAdjustment getById(String adjustmentId) {
        if (!StringUtils.hasText(adjustmentId)) {
            throw new ServiceException("调整ID不能为空");
        }
        
        QueryWrapper<BudgetAdjustment> wrapper = new QueryWrapper<>();
        wrapper.eq("ADJUSTMENT_ID", adjustmentId)
               .eq("DEL_FLAG", 0);
        
        return adjustmentMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetAdjustment adjustment) {
        if (adjustment == null || !StringUtils.hasText(adjustment.getAdjustmentId())) {
            throw new ServiceException("调整ID不能为空");
        }

        BudgetAdjustment existing = getById(adjustment.getAdjustmentId());
        if (existing == null) {
            throw new ServiceException("调整记录不存在");
        }

        // 只有草稿状态才能修改
        if (!"DRAFT".equals(existing.getAdjustmentStatus())) {
            throw new ServiceException("只有草稿状态的调整才能修改");
        }

        adjustment.setUpdateTime(new Date());
        int result = adjustmentMapper.updateById(adjustment);
        if (result <= 0) {
            throw new ServiceException("更新调整申请失败");
        }

        log.info("更新调整申请成功，ID: {}", adjustment.getAdjustmentId());
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetAdjustment> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 调整编码
        if (hasValue(params.get("adjustmentCode"))) {
            wrapper.like("ADJUSTMENT_CODE", params.get("adjustmentCode"));
        }

        // 调整类型
        if (hasValue(params.get("adjustmentType"))) {
            wrapper.eq("ADJUSTMENT_TYPE", params.get("adjustmentType"));
        }

        // 调整状态
        if (hasValue(params.get("adjustmentStatus"))) {
            wrapper.eq("ADJUSTMENT_STATUS", params.get("adjustmentStatus"));
        }

        // 原预算ID
        if (hasValue(params.get("originalBudgetId"))) {
            wrapper.eq("ORIGINAL_BUDGET_ID", params.get("originalBudgetId"));
        }

        // 申请人
        if (hasValue(params.get("applicantId"))) {
            wrapper.eq("APPLICANT_ID", params.get("applicantId"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetAdjustment> page = new Page<>(pageNum, pageSize);
        IPage<BudgetAdjustment> pageResult = adjustmentMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(String adjustmentId) {
        if (!StringUtils.hasText(adjustmentId)) {
            throw new ServiceException("调整ID不能为空");
        }

        BudgetAdjustment adjustment = getById(adjustmentId);
        if (adjustment == null) {
            throw new ServiceException("调整记录不存在");
        }

        if (!"DRAFT".equals(adjustment.getAdjustmentStatus())) {
            throw new ServiceException("只有草稿状态的调整才能提交");
        }

        // 更新状态为待审批（APPLY_DATE 对应申请日期）
        BudgetAdjustment update = new BudgetAdjustment();
        update.setAdjustmentId(adjustmentId);
        update.setAdjustmentStatus("PENDING");
        update.setApplyDate(new Date());
        update.setUpdateTime(new Date());

        int result = adjustmentMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("提交审批失败");
        }

        log.info("提交调整审批成功，ID: {}", adjustmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Map<String, Object> params) {
        String adjustmentId = (String) params.get("adjustmentId");

        // 兼容前端 action/approveResult 两种字段名
        String approveResult = (String) params.get("approveResult");
        if (!StringUtils.hasText(approveResult)) {
            String action = (String) params.get("action");
            if ("approve".equals(action)) {
                approveResult = "APPROVED";
            } else if ("reject".equals(action)) {
                approveResult = "REJECTED";
            }
        }

        // 兼容前端 comment/approveComment 两种字段名
        String approveComment = (String) params.get("approveComment");
        if (!StringUtils.hasText(approveComment)) {
            approveComment = (String) params.get("comment");
        }

        if (!StringUtils.hasText(adjustmentId)) {
            throw new ServiceException("调整ID不能为空");
        }
        if (!StringUtils.hasText(approveResult)) {
            throw new ServiceException("审批结果不能为空");
        }

        BudgetAdjustment adjustment = getById(adjustmentId);
        if (adjustment == null) {
            throw new ServiceException("调整记录不存在");
        }

        // 允许 PENDING 和 SUBMITTED 状态进行审批
        String currentStatus = adjustment.getAdjustmentStatus();
        if (!"PENDING".equals(currentStatus) && !"SUBMITTED".equals(currentStatus)) {
            throw new ServiceException("只有待审批状态的调整才能审批");
        }

        // 更新审批状态
        BudgetAdjustment update = new BudgetAdjustment();
        update.setAdjustmentId(adjustmentId);
        update.setAdjustmentStatus(approveResult);
        update.setApproveComment(approveComment);
        update.setApproveDate(new Date());
        update.setUpdateTime(new Date());
        // 填充审批人信息
        update.setApproverName(getCurrentUserName());
        update.setApproverId(getCurrentUserId());

        int result = adjustmentMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("审批失败");
        }

        log.info("审批调整成功，ID: {}, 结果: {}", adjustmentId, approveResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String adjustmentId) {
        if (!StringUtils.hasText(adjustmentId)) {
            throw new ServiceException("调整ID不能为空");
        }

        BudgetAdjustment adjustment = getById(adjustmentId);
        if (adjustment == null) {
            throw new ServiceException("调整记录不存在");
        }

        if (!"APPROVED".equals(adjustment.getAdjustmentStatus())) {
            throw new ServiceException("只有已审批的调整才能执行");
        }

        // 执行调整逻辑
        // TODO: 根据调整类型执行相应的预算调整操作
        // INCREASE: 增加预算
        // DECREASE: 减少预算
        // TRANSFER: 转移预算

        // 更新状态为已执行
        BudgetAdjustment update = new BudgetAdjustment();
        update.setAdjustmentId(adjustmentId);
        update.setAdjustmentStatus("EXECUTED");
        update.setExecuteTime(new Date());
        update.setUpdateTime(new Date());

        int result = adjustmentMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("执行调整失败");
        }

        log.info("执行调整成功，ID: {}", adjustmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String adjustmentId) {
        if (!StringUtils.hasText(adjustmentId)) {
            throw new ServiceException("调整ID不能为空");
        }
        BudgetAdjustment adjustment = getById(adjustmentId);
        if (adjustment == null) {
            throw new ServiceException("调整记录不存在");
        }
        // 软删除
        BudgetAdjustment update = new BudgetAdjustment();
        update.setAdjustmentId(adjustmentId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());
        int result = adjustmentMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除调整申请失败");
        }
        log.info("删除调整申请成功，ID: {}", adjustmentId);
    }

    /**
     * 生成调整编码
     */
    private String generateAdjustmentCode() {
        return "ADJ" + System.currentTimeMillis();
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }
}

