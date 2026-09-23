package com.financial.sharing.oracle.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 成本分摊实体类
 *
 * @author Financial Sharing System
 * @since 2024-12-06
 */
@Data
@Accessors(chain = true)
public class CostAllocationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分摊ID - 主键
     */
    private Long allocationId;

    /**
     * 分摊单号 - 唯一标识
     */
    private String allocationNo;

    /**
     * 分摊期间 - 格式YYYY-MM
     */
    private String allocationPeriod;

    /**
     * 源成本中心ID
     */
    private String sourceCenterId;

    /**
     * 分摊方法
     * QUANTITY_BASED - 数量基础
     * AMOUNT_BASED - 金额基础
     * RATIO_BASED - 比例基础
     * HOUR_BASED - 工时基础
     */
    private String allocationMethod;

    /**
     * 分摊总额
     */
    private BigDecimal totalAmount;

    /**
     * 分摊状态
     * 1 - 待分摊
     * 2 - 分摊中
     * 3 - 已分摊
     * 4 - 已审核
     */
    private Integer allocationStatus;

    /**
     * 分摊日期
     */
    private LocalDate allocationDate;

    /**
     * 是否生成凭证
     * 0 - 否
     * 1 - 是
     */
    private Integer isGenerateVoucher;

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 账簿ID
     */
    private String bookId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 版本号 - 乐观锁
     */
    private Integer version;

    /**
     * 是否删除 - 软删除
     * 0 - 未删除
     * 1 - 已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long updater;

    // ==================== 业务方法 ====================

    /**
     * 获取分摊状态描述
     */
    public String getAllocationStatusName() {
        if (allocationStatus == null) {
            return "未知状态";
        }
        switch (allocationStatus) {
            case 1:
                return "待分摊";
            case 2:
                return "分摊中";
            case 3:
                return "已分摊";
            case 4:
                return "已审核";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取分摊方法描述
     */
    public String getAllocationMethodName() {
        if (allocationMethod == null) {
            return "未知方法";
        }
        switch (allocationMethod) {
            case "QUANTITY_BASED":
                return "数量基础";
            case "AMOUNT_BASED":
                return "金额基础";
            case "RATIO_BASED":
                return "比例基础";
            case "HOUR_BASED":
                return "工时基础";
            default:
                return "未知方法";
        }
    }

    /**
     * 判断是否已删除
     */
    public boolean isDeletedFlag() {
        return Integer.valueOf(1).equals(isDeleted);
    }

    /**
     * 判断是否已生成凭证
     */
    public boolean isVoucherGenerated() {
        return Integer.valueOf(1).equals(isGenerateVoucher);
    }

    /**
     * 判断是否为待分摊状态
     */
    public boolean isPendingStatus() {
        return Integer.valueOf(1).equals(allocationStatus);
    }

    /**
     * 判断是否为分摊中状态
     */
    public boolean isProcessingStatus() {
        return Integer.valueOf(2).equals(allocationStatus);
    }

    /**
     * 判断是否为已分摊状态
     */
    public boolean isAllocatedStatus() {
        return Integer.valueOf(3).equals(allocationStatus);
    }

    /**
     * 判断是否为已审核状态
     */
    public boolean isAuditedStatus() {
        return Integer.valueOf(4).equals(allocationStatus);
    }

    /**
     * 判断是否可以编辑
     * 只有待分摊和分摊中状态可以编辑
     */
    public boolean isEditable() {
        return isPendingStatus() || isProcessingStatus();
    }

    /**
     * 判断是否可以审核
     * 只有已分摊状态可以审核
     */
    public boolean isAuditable() {
        return isAllocatedStatus();
    }

    /**
     * 判断是否可以取消
     * 待分摊、分摊中、已分摊状态可以取消
     */
    public boolean isCancellable() {
        return isPendingStatus() || isProcessingStatus() || isAllocatedStatus();
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 创建新的成本分摊实体
     */
    public static CostAllocationEntity create(String allocationNo, String allocationPeriod,
                                             String sourceCenterId, String allocationMethod,
                                             BigDecimal totalAmount, String bookId, String tenantId) {
        CostAllocationEntity entity = new CostAllocationEntity();
        entity.setAllocationNo(allocationNo);
        entity.setAllocationPeriod(allocationPeriod);
        entity.setSourceCenterId(sourceCenterId);
        entity.setAllocationMethod(allocationMethod);
        entity.setTotalAmount(totalAmount);
        entity.setAllocationStatus(1); // 默认待分摊状态
        entity.setAllocationDate(LocalDate.now());
        entity.setIsGenerateVoucher(0); // 默认不生成凭证
        entity.setBookId(bookId);
        entity.setTenantId(tenantId);
        entity.setVersion(1);
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }

    /**
     * 创建用于查询的实体
     */
    public static CostAllocationEntity forQuery(Long allocationId, String allocationNo,
                                               String allocationPeriod, String sourceCenterId,
                                               Integer allocationStatus, String bookId, String tenantId) {
        CostAllocationEntity entity = new CostAllocationEntity();
        entity.setAllocationId(allocationId);
        entity.setAllocationNo(allocationNo);
        entity.setAllocationPeriod(allocationPeriod);
        entity.setSourceCenterId(sourceCenterId);
        entity.setAllocationStatus(allocationStatus);
        entity.setBookId(bookId);
        entity.setTenantId(tenantId);
        entity.setIsDeleted(0); // 查询未删除的记录
        return entity;
    }

    // ==================== 分摊方法常量 ====================

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

    // 手动添加setter方法，确保编译通过
    public CostAllocationEntity setAllocationNo(String allocationNo) {
        this.allocationNo = allocationNo;
        return this;
    }

    public CostAllocationEntity setAllocationPeriod(String allocationPeriod) {
        this.allocationPeriod = allocationPeriod;
        return this;
    }

    public CostAllocationEntity setSourceCenterId(String sourceCenterId) {
        this.sourceCenterId = sourceCenterId;
        return this;
    }

    public CostAllocationEntity setAllocationMethod(String allocationMethod) {
        this.allocationMethod = allocationMethod;
        return this;
    }

    public CostAllocationEntity setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public CostAllocationEntity setAllocationStatus(int allocationStatus) {
        this.allocationStatus = allocationStatus;
        return this;
    }

    public CostAllocationEntity setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
        return this;
    }

    public CostAllocationEntity setIsGenerateVoucher(int isGenerateVoucher) {
        this.isGenerateVoucher = isGenerateVoucher;
        return this;
    }

    public CostAllocationEntity setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public CostAllocationEntity setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public CostAllocationEntity setVersion(int version) {
        this.version = version;
        return this;
    }

    public CostAllocationEntity setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }

    public CostAllocationEntity setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public CostAllocationEntity setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
        return this;
    }

    public CostAllocationEntity setAllocationStatus(Integer allocationStatus) {
        this.allocationStatus = allocationStatus != null ? allocationStatus.intValue() : 0;
        return this;
    }

    /**
     * 是否标志常量
     */
    public static class Flag {
        public static final Integer NO = 0;
        public static final Integer YES = 1;
    }

    // 添加缺失的setter方法
    public CostAllocationEntity setVoucherId(Long voucherId) {
        this.voucherId = voucherId;
        return this;
    }

    public CostAllocationEntity setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public CostAllocationEntity setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public CostAllocationEntity setCreator(Long creator) {
        this.creator = creator;
        return this;
    }

    public CostAllocationEntity setUpdater(Long updater) {
        this.updater = updater;
        return this;
    }

    public CostAllocationEntity setIsGenerateVoucher(Integer isGenerateVoucher) {
        this.isGenerateVoucher = isGenerateVoucher != null ? isGenerateVoucher.intValue() : 0;
        return this;
    }
}