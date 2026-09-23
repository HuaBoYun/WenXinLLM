package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.BudgetDetail;
import com.huabo.contract.mapper.BudgetDetailMapper;
import com.huabo.contract.service.BudgetDetailService;
import com.huabo.contract.vo.BudgetDetailQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算明细服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Service
public class BudgetDetailServiceImpl extends ServiceImpl<BudgetDetailMapper, BudgetDetail> implements BudgetDetailService {

    @Override
    @Transactional
    public boolean save(BudgetDetail budgetDetail) {
        // 手动设置时间字段，因为自动填充可能不工作
        Date now = new Date();
        if (budgetDetail.getCreateTime() == null) {
            budgetDetail.setCreateTime(now);
        }
        if (budgetDetail.getUpdateTime() == null) {
            budgetDetail.setUpdateTime(now);
        }
        return super.save(budgetDetail);
    }

    @Override
    public IPage<BudgetDetail> getBudgetDetailPage(BudgetDetailQueryParam queryParam) {
        Page<BudgetDetail> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectBudgetDetailPage(page, queryParam);
    }

    @Override
    public List<BudgetDetail> getByBudgetId(Long budgetId) {
        return baseMapper.selectByBudgetId(budgetId);
    }

    @Override
    public List<BudgetDetail> getByCostCategory(Integer costCategory) {
        return baseMapper.selectByCostCategory(costCategory);
    }

    @Override
    public List<BudgetDetail> getByDetailStatus(Integer detailStatus) {
        return baseMapper.selectByDetailStatus(detailStatus);
    }

    @Override
    public List<BudgetDetail> getByApprovalStatus(Integer approvalStatus) {
        return baseMapper.selectByApprovalStatus(approvalStatus);
    }

    @Override
    public List<BudgetDetail> getByRiskLevel(Integer riskLevel) {
        return baseMapper.selectByRiskLevel(riskLevel);
    }

    @Override
    public List<BudgetDetail> getCriticalItems() {
        return baseMapper.selectCriticalItems();
    }

    @Override
    public List<BudgetDetail> getBySupplierId(Long supplierId) {
        return baseMapper.selectBySupplierId(supplierId);
    }

    @Override
    public List<BudgetDetail> getByDepartmentId(Long departmentId) {
        return baseMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<BudgetDetail> getByManagerId(Long managerId) {
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<BudgetDetail> getByApproverId(Long approverId) {
        return baseMapper.selectByApproverId(approverId);
    }

    @Override
    public BigDecimal sumBudgetedAmountByBudgetId(Long budgetId) {
        return baseMapper.sumBudgetedAmountByBudgetId(budgetId);
    }

    @Override
    public BigDecimal sumActualAmountByBudgetId(Long budgetId) {
        return baseMapper.sumActualAmountByBudgetId(budgetId);
    }

    @Override
    public BigDecimal sumBudgetedAmountByCostCategory(Integer costCategory) {
        return baseMapper.sumBudgetedAmountByCostCategory(costCategory);
    }

    @Override
    public BigDecimal sumActualAmountByCostCategory(Integer costCategory) {
        return baseMapper.sumActualAmountByCostCategory(costCategory);
    }

    @Override
    public Integer countByDetailStatus(Integer detailStatus) {
        return baseMapper.countByDetailStatus(detailStatus);
    }

    @Override
    public Integer countByApprovalStatus(Integer approvalStatus) {
        return baseMapper.countByApprovalStatus(approvalStatus);
    }

    @Override
    public List<BudgetDetail> getOverBudget() {
        return baseMapper.selectOverBudget();
    }

    @Override
    public List<BudgetDetail> getHighRisk() {
        return baseMapper.selectHighRisk();
    }

    @Override
    public List<BudgetDetail> getPendingApproval() {
        return baseMapper.selectPendingApproval();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateDetailStatus(List<Long> ids, Integer detailStatus, Long updateBy) {
        return baseMapper.batchUpdateDetailStatus(ids, detailStatus, updateBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateApprovalStatus(List<Long> ids, Integer approvalStatus, Long approverId, String approvalComments) {
        return baseMapper.batchUpdateApprovalStatus(ids, approvalStatus, approverId, approvalComments);
    }

    @Override
    public List<BudgetDetail> getByCostSubcategory(String costSubcategory) {
        return baseMapper.selectByCostSubcategory(costSubcategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveBudgetDetail(Long id, Integer approvalStatus, Long approverId, String approvalComments) {
        BudgetDetail budgetDetail = getById(id);
        if (budgetDetail == null) {
            return false;
        }

        // 由于实体类已简化，这里只更新基本信息
        budgetDetail.setUpdateTime(new Date());

        return updateById(budgetDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adjustBudgetDetail(Long id, BigDecimal newSubtotal, String adjustmentReason, String adjustmentDescription, Long updateBy) {
        BudgetDetail budgetDetail = getById(id);
        if (budgetDetail == null) {
            return false;
        }

        // 更新小计金额
        budgetDetail.setSubtotal(newSubtotal);
        budgetDetail.setUpdateTime(new Date());
        
        return updateById(budgetDetail);
    }

    @Override
    public List<BudgetDetail> getBudgetDetailStatistics() {
        return baseMapper.selectBudgetDetailStatistics();
    }

    @Override
    public List<String> getBudgetAnalysisReport(Long budgetId) {
        List<String> report = new ArrayList<>();

        // 查询预算明细
        List<BudgetDetail> details = getByBudgetId(budgetId);

        if (details.isEmpty()) {
            report.add("该预算暂无明细数据");
            return report;
        }

        // 统计各类别预算
        BigDecimal totalSubtotal = BigDecimal.ZERO;
        Map<Integer, Integer> categoryCount = new HashMap<>();

        for (BudgetDetail detail : details) {
            if (detail.getSubtotal() != null) {
                totalSubtotal = totalSubtotal.add(detail.getSubtotal());
            }

            // 统计各费用类别数量
            Integer category = detail.getCostCategory();
            if (category != null) {
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            }
        }

        // 生成分析报告
        report.add("=== 预算明细分析报告 ===");
        report.add("明细总数：" + details.size() + " 项");
        report.add("预算总额：" + totalSubtotal + " 元");

        // 按费用类别统计
        report.add("--- 费用类别分布 ---");
        for (Map.Entry<Integer, Integer> entry : categoryCount.entrySet()) {
            String categoryName = getCategoryName(entry.getKey());
            report.add(categoryName + "：" + entry.getValue() + " 项");
        }

        return report;
    }

    /**
     * 获取费用类别名称
     */
    private String getCategoryName(Integer category) {
        if (category == null) {
            return "未知";
        }
        switch (category) {
            case 1:
                return "人工费";
            case 2:
                return "材料费";
            case 3:
                return "设备费";
            case 4:
                return "其他费用";
            default:
                return "未知";
        }
    }

    @Override
    public Boolean validateBudgetDetailInfo(BudgetDetail budgetDetail) {
        if (budgetDetail == null) {
            System.out.println("验证失败：budgetDetail为null");
            return false;
        }

        // 验证必填字段
        if (budgetDetail.getBudgetId() == null) {
            System.out.println("验证失败：budgetId为null");
            return false;
        }

        // 验证费用项目（允许空字符串，但不允许null）
        if (budgetDetail.getCostItem() == null || budgetDetail.getCostItem().trim().isEmpty()) {
            System.out.println("验证失败：costItem为空, 值为: " + budgetDetail.getCostItem());
            return false;
        }

        if (budgetDetail.getCostCategory() == null) {
            System.out.println("验证失败：costCategory为null");
            return false;
        }

        // 验证小计金额（允许0，但不允许负数）
        if (budgetDetail.getSubtotal() == null) {
            System.out.println("验证失败：subtotal为null");
            return false;
        }
        if (budgetDetail.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("验证失败：subtotal < 0, 值为: " + budgetDetail.getSubtotal());
            return false;
        }

        // 验证数量和单价（允许0，但不允许负数）
        if (budgetDetail.getQuantity() != null &&
            budgetDetail.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("验证失败：quantity < 0, 值为: " + budgetDetail.getQuantity());
            return false;
        }

        if (budgetDetail.getUnitPrice() != null &&
            budgetDetail.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("验证失败：unitPrice < 0, 值为: " + budgetDetail.getUnitPrice());
            return false;
        }

        System.out.println("验证成功：所有字段都通过验证");
        System.out.println("  - budgetId: " + budgetDetail.getBudgetId());
        System.out.println("  - costItem: " + budgetDetail.getCostItem());
        System.out.println("  - costCategory: " + budgetDetail.getCostCategory());
        System.out.println("  - quantity: " + budgetDetail.getQuantity());
        System.out.println("  - unitPrice: " + budgetDetail.getUnitPrice());
        System.out.println("  - subtotal: " + budgetDetail.getSubtotal());
        return true;
    }


}
