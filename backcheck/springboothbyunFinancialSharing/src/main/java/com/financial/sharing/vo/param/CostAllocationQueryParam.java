package com.financial.sharing.vo.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 成本分摊查询参数类
 *
 * @author Financial Sharing System
 * @since 2024-12-06
 */
@Data
@Accessors(chain = true)
public class CostAllocationQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 分页参数 ====================

    /**
     * 页码（从1开始）
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNumber = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 20;

    /**
     * 排序字段
     */
    private String orderBy = "ALLOCATION_DATE DESC, ALLOCATION_ID DESC";

    /**
     * 分页偏移量（计算得出）
     */
    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    // ==================== 基础查询条件 ====================

    /**
     * 分摊期间（格式：YYYY-MM）
     */
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "分摊期间格式不正确，应为YYYY-MM")
    private String allocationPeriod;

    /**
     * 分摊单号
     */
    private String allocationNo;

    /**
     * 源成本中心ID
     */
    private String sourceCostCenterId;

    /**
     * 分摊方法
     * QUANTITY_BASED - 数量基础
     * AMOUNT_BASED - 金额基础
     * RATIO_BASED - 比例基础
     * HOUR_BASED - 工时基础
     */
    private String allocationMethod;

    /**
     * 分摊状态
     * 1 - 待分摊
     * 2 - 分摊中
     * 3 - 已分摊
     * 4 - 已审核
     */
    private Integer allocationStatus;

    /**
     * 分摊日期范围 - 开始日期
     */
    private String startDate;

    /**
     * 分摊日期范围 - 结束日期
     */
    private String endDate;

    /**
     * 是否生成凭证
     * 0 - 否
     * 1 - 是
     */
    private Integer isGenerateVoucher;

    // ==================== 多租户参数 ====================

    /**
     * 账簿ID
     */
    private String bookId;

    /**
     * 租户ID
     */
    private String tenantId;

    // ==================== 高级查询条件 ====================

    /**
     * 目标成本中心ID（查询包含该目标成本中心的分摊记录）
     */
    private String targetCostCenterId;

    /**
     * 分摊金额范围 - 最小值
     */
    private Double minAmount;

    /**
     * 分摊金额范围 - 最大值
     */
    private Double maxAmount;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 创建时间范围 - 开始时间
     */
    private String startCreateTime;

    /**
     * 创建时间范围 - 结束时间
     */
    private String endCreateTime;

    /**
     * 更新时间范围 - 开始时间
     */
    private String startUpdateTime;

    /**
     * 更新时间范围 - 结束时间
     */
    private String endUpdateTime;

    // ==================== 模糊查询条件 ====================

    /**
     * 备注（模糊查询）
     */
    private String remark;

    // ==================== 数据权限控制 ====================

    /**
     * 数据权限范围的成本中心ID列表
     */
    private String[] accessibleCenterIds;

    /**
     * 是否只查询自己创建的记录
     */
    private Boolean onlySelfCreated = false;

    /**
     * 当前用户ID（用于数据权限过滤）
     */
    private Long currentUserId;

    // ==================== 业务辅助字段 ====================

    /**
     * 是否包含明细数据
     */
    private Boolean includeDetails = false;

    /**
     * 是否需要统计信息
     */
    private Boolean needStats = false;

    /**
     * 是否查询概览数据
     */
    private Boolean isOverviewQuery = false;

    /**
     * 统计类型（用于不同的统计场景）
     */
    private String statsType;

    // ==================== 业务方法 ====================

    /**
     * 验证查询参数的有效性
     */
    public boolean isValid() {
        // 确保分页参数有值（使用默认值）
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }

        // 基础参数验证
        if (pageNumber < 1) {
            return false;
        }
        if (pageSize < 1 || pageSize > 1000) {
            return false;
        }

        // 期间格式验证（允许为空）
        if (allocationPeriod != null && !allocationPeriod.trim().isEmpty()
            && !allocationPeriod.matches("^\\d{4}-\\d{2}$")) {
            return false;
        }

        // 状态值验证（允许为空）
        if (allocationStatus != null && (allocationStatus < 1 || allocationStatus > 4)) {
            return false;
        }

        // 金额范围验证
        if (minAmount != null && maxAmount != null && minAmount > maxAmount) {
            return false;
        }

        return true;
    }

    /**
     * 检查是否为空查询（所有查询条件都为空）
     */
    public boolean isEmptyQuery() {
        return (allocationPeriod == null || allocationPeriod.trim().isEmpty()) &&
               (allocationNo == null || allocationNo.trim().isEmpty()) &&
               (sourceCostCenterId == null || sourceCostCenterId.trim().isEmpty()) &&
               allocationMethod == null &&
               allocationStatus == null &&
               (startDate == null || startDate.trim().isEmpty()) &&
               (endDate == null || endDate.trim().isEmpty()) &&
               isGenerateVoucher == null &&
               (targetCostCenterId == null || targetCostCenterId.trim().isEmpty()) &&
               minAmount == null &&
               maxAmount == null &&
               creator == null &&
               updater == null &&
               (remark == null || remark.trim().isEmpty());
    }

    /**
     * 构建查询描述（用于日志记录）
     */
    public String buildQueryDescription() {
        StringBuilder description = new StringBuilder();
        description.append("查询成本分摊数据 - ");

        if (allocationPeriod != null) {
            description.append("期间: ").append(allocationPeriod).append(", ");
        }
        if (sourceCostCenterId != null) {
            description.append("源中心: ").append(sourceCostCenterId).append(", ");
        }
        if (allocationMethod != null) {
            description.append("方法: ").append(allocationMethod).append(", ");
        }
        if (allocationStatus != null) {
            description.append("状态: ").append(allocationStatus).append(", ");
        }
        if (allocationNo != null) {
            description.append("单号: ").append(allocationNo).append(", ");
        }

        description.append("页码: ").append(pageNumber).append(", ");
        description.append("页大小: ").append(pageSize);

        return description.toString();
    }

    /**
     * 克隆查询参数（用于分页统计等场景）
     */
    public CostAllocationQueryParam clone() {
        CostAllocationQueryParam clone = new CostAllocationQueryParam();
        clone.pageNumber = this.pageNumber;
        clone.pageSize = this.pageSize;
        clone.orderBy = this.orderBy;
        clone.allocationPeriod = this.allocationPeriod;
        clone.allocationNo = this.allocationNo;
        clone.sourceCostCenterId = this.sourceCostCenterId;
        clone.allocationMethod = this.allocationMethod;
        clone.allocationStatus = this.allocationStatus;
        clone.startDate = this.startDate;
        clone.endDate = this.endDate;
        clone.isGenerateVoucher = this.isGenerateVoucher;
        clone.bookId = this.bookId;
        clone.tenantId = this.tenantId;
        clone.targetCostCenterId = this.targetCostCenterId;
        clone.minAmount = this.minAmount;
        clone.maxAmount = this.maxAmount;
        clone.creator = this.creator;
        clone.updater = this.updater;
        clone.startCreateTime = this.startCreateTime;
        clone.endCreateTime = this.endCreateTime;
        clone.startUpdateTime = this.startUpdateTime;
        clone.endUpdateTime = this.endUpdateTime;
        clone.remark = this.remark;
        clone.accessibleCenterIds = this.accessibleCenterIds;
        clone.onlySelfCreated = this.onlySelfCreated;
        clone.currentUserId = this.currentUserId;
        clone.includeDetails = this.includeDetails;
        clone.needStats = this.needStats;
        clone.isOverviewQuery = this.isOverviewQuery;
        clone.statsType = this.statsType;
        return clone;
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 创建基础查询参数
     */
    public static CostAllocationQueryBuilder builder() {
        return new CostAllocationQueryBuilder();
    }

    /**
     * 创建分页查询参数
     */
    public static CostAllocationQueryParam forPage(Integer pageNumber, Integer pageSize) {
        return new CostAllocationQueryBuilder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    /**
     * 创建期间查询参数
     */
    public static CostAllocationQueryParam forPeriod(String period, String bookId, String tenantId) {
        return new CostAllocationQueryBuilder()
                .allocationPeriod(period)
                .bookId(bookId)
                .tenantId(tenantId)
                .build();
    }

    /**
     * 查询参数构建器
     */
    public static class CostAllocationQueryBuilder {
        private final CostAllocationQueryParam param = new CostAllocationQueryParam();

        public CostAllocationQueryBuilder pageNumber(Integer pageNumber) {
            param.pageNumber = pageNumber;
            return this;
        }

        public CostAllocationQueryBuilder pageSize(Integer pageSize) {
            param.pageSize = pageSize;
            return this;
        }

        public CostAllocationQueryBuilder allocationPeriod(String allocationPeriod) {
            param.allocationPeriod = allocationPeriod;
            return this;
        }

        public CostAllocationQueryBuilder sourceCostCenterId(String sourceCostCenterId) {
            param.sourceCostCenterId = sourceCostCenterId;
            return this;
        }

        public CostAllocationQueryBuilder allocationMethod(String allocationMethod) {
            param.allocationMethod = allocationMethod;
            return this;
        }

        public CostAllocationQueryBuilder allocationStatus(Integer allocationStatus) {
            param.allocationStatus = allocationStatus;
            return this;
        }

        public CostAllocationQueryBuilder bookId(String bookId) {
            param.bookId = bookId;
            return this;
        }

        public CostAllocationQueryBuilder tenantId(String tenantId) {
            param.tenantId = tenantId;
            return this;
        }

        public CostAllocationQueryBuilder includeDetails(Boolean includeDetails) {
            param.includeDetails = includeDetails;
            return this;
        }

        public CostAllocationQueryParam build() {
            return param;
        }
    }

    // ==================== 常量定义 ====================

    /**
     * 分摊方法常量
     */
    public static class AllocationMethod {
        public static final String QUANTITY_BASED = "QUANTITY_BASED";
        public static final String AMOUNT_BASED = "AMOUNT_BASED";
        public static final String RATIO_BASED = "RATIO_BASED";
        public static final String HOUR_BASED = "HOUR_BASED";
    }

    /**
     * 分摊状态常量
     */
    public static class AllocationStatus {
        public static final Integer PENDING = 1;       // 待分摊
        public static final Integer PROCESSING = 2;    // 分摊中
        public static final Integer ALLOCATED = 3;      // 已分摊
        public static final Integer AUDITED = 4;       // 已审核
    }

    /**
     * 默认分页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    /**
     * 最大分页大小
     */
    public static final Integer MAX_PAGE_SIZE = 1000;
}