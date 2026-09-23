package com.financial.sharing.mysql.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成本分摊明细实体类
 *
 * @author Financial Sharing System
 * @since 2024-12-06
 */
@Data
@Accessors(chain = true)
public class CostAllocationDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID - 主键
     */
    private Long detailId;

    /**
     * 分摊主表ID - 外键
     */
    private Long allocationId;

    /**
     * 目标成本中心ID
     */
    private String targetCenterId;

    /**
     * 分摊基础值
     * 根据分摊方法不同，代表的含义不同：
     * - QUANTITY_BASED: 数量
     * - AMOUNT_BASED: 金额
     * - RATIO_BASED: 比例值
     * - HOUR_BASED: 工时数
     */
    private BigDecimal allocationBasisValue;

    /**
     * 分摊比例 (0-1之间的小数)
     * 例如: 0.25 表示25%
     */
    private BigDecimal allocationRatio;

    /**
     * 分摊金额
     * 计算公式: totalAmount * allocationRatio
     */
    private BigDecimal allocatedAmount;

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

    // ==================== 扩展字段（非持久化，用于显示） ====================

    /**
     * 目标成本中心名称（从关联查询获取）
     */
    private transient String targetCenterName;

    /**
     * 分摊比例百分比显示（例如: 25.00%）
     */
    private transient String allocationRatioPercent;

    /**
     * 分摊方法（从主表获取）
     */
    private transient String allocationMethod;

    /**
     * 源成本中心ID（从主表获取）
     */
    private transient String sourceCenterId;

    /**
     * 源成本中心名称（从关联查询获取）
     */
    private transient String sourceCenterName;

    // ==================== 业务方法 ====================

    /**
     * 获取分摊比例百分比字符串
     */
    public String getAllocationRatioPercent() {
        if (allocationRatio == null) {
            return "0.00%";
        }
        return allocationRatio.multiply(new BigDecimal("100"))
                .setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
    }

    /**
     * 设置分摊比例并计算百分比字符串
     */
    public void setAllocationRatioWithPercent(BigDecimal allocationRatio) {
        this.allocationRatio = allocationRatio;
        this.allocationRatioPercent = getAllocationRatioPercent();
    }

    /**
     * 判断是否已删除
     */
    public boolean isDeletedFlag() {
        return Integer.valueOf(1).equals(isDeleted);
    }

    /**
     * 验证分摊明细数据的完整性
     */
    public boolean isValid() {
        return allocationId != null &&
               targetCenterId != null && !targetCenterId.trim().isEmpty() &&
               allocationBasisValue != null && allocationBasisValue.compareTo(BigDecimal.ZERO) > 0 &&
               allocationRatio != null && allocationRatio.compareTo(BigDecimal.ZERO) > 0 &&
               allocatedAmount != null && allocatedAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * 验证分摊比例是否合法
     */
    public boolean isRatioValid() {
        return allocationRatio != null &&
               allocationRatio.compareTo(BigDecimal.ZERO) > 0 &&
               allocationRatio.compareTo(BigDecimal.ONE) <= 0;
    }

    /**
     * 验证分摊金额是否合理
     */
    public boolean isAmountReasonable(BigDecimal totalAmount) {
        return allocatedAmount != null &&
               totalAmount != null &&
               allocatedAmount.compareTo(BigDecimal.ZERO) >= 0 &&
               allocatedAmount.compareTo(totalAmount) <= 0;
    }

    // ==================== 静态工厂方法 ====================

    /**
     * 创建新的成本分摊明细实体
     */
    public static CostAllocationDetailEntity create(Long allocationId, String targetCenterId,
                                                   BigDecimal allocationBasisValue, BigDecimal allocationRatio,
                                                   BigDecimal allocatedAmount, String bookId, String tenantId) {
        CostAllocationDetailEntity entity = new CostAllocationDetailEntity();
        entity.setAllocationId(allocationId);
        entity.setTargetCenterId(targetCenterId);
        entity.setAllocationBasisValue(allocationBasisValue);
        entity.setAllocationRatio(allocationRatio);
        entity.setAllocatedAmount(allocatedAmount);
        entity.setBookId(bookId);
        entity.setTenantId(tenantId);
        entity.setVersion(1);
        entity.setIsDeleted(0);
        entity.setCreateTime(LocalDateTime.now());
        // 自动计算百分比显示
        entity.setAllocationRatioPercent(entity.getAllocationRatioPercent());
        return entity;
    }

    /**
     * 根据分摊基础值创建明细实体
     */
    public static CostAllocationDetailEntity createFromBasisValue(Long allocationId, String targetCenterId,
                                                                 BigDecimal basisValue, BigDecimal totalBasis,
                                                                 BigDecimal totalAmount, String bookId, String tenantId) {
        // 计算分摊比例
        BigDecimal allocationRatio = basisValue.divide(totalBasis, 4, BigDecimal.ROUND_HALF_UP);
        // 计算分摊金额
        BigDecimal allocatedAmount = totalAmount.multiply(allocationRatio)
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        return create(allocationId, targetCenterId, basisValue, allocationRatio, allocatedAmount, bookId, tenantId);
    }

    /**
     * 根据分摊比例创建明细实体
     */
    public static CostAllocationDetailEntity createFromRatio(Long allocationId, String targetCenterId,
                                                           BigDecimal allocationRatio, BigDecimal totalAmount,
                                                           String bookId, String tenantId) {
        // 计算分摊金额
        BigDecimal allocatedAmount = totalAmount.multiply(allocationRatio)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        // 将比例转换为基础值（用于显示）
        BigDecimal basisValue = allocationRatio.multiply(new BigDecimal("100"));

        CostAllocationDetailEntity entity = create(allocationId, targetCenterId, basisValue, allocationRatio, allocatedAmount, bookId, tenantId);
        // 如果是比例基础分摊，basisValue就是原始比例值
        entity.setAllocationBasisValue(allocationRatio);
        return entity;
    }

    /**
     * 创建用于查询的实体
     */
    public static CostAllocationDetailEntity forQuery(Long detailId, Long allocationId,
                                                    String targetCenterId, String bookId, String tenantId) {
        CostAllocationDetailEntity entity = new CostAllocationDetailEntity();
        entity.setDetailId(detailId);
        entity.setAllocationId(allocationId);
        entity.setTargetCenterId(targetCenterId);
        entity.setBookId(bookId);
        entity.setTenantId(tenantId);
        entity.setIsDeleted(0); // 查询未删除的记录
        return entity;
    }

    // ==================== 计算辅助方法 ====================

    /**
     * 根据总金额和当前比例重新计算分摊金额
     */
    public void recalculateAllocatedAmount(BigDecimal totalAmount) {
        if (allocationRatio != null && totalAmount != null) {
            this.allocatedAmount = totalAmount.multiply(allocationRatio)
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 根据分摊金额反算比例
     */
    public void calculateRatioFromAmount(BigDecimal totalAmount) {
        if (allocatedAmount != null && totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.allocationRatio = allocatedAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP);
            this.allocationRatioPercent = getAllocationRatioPercent();
        }
    }

    /**
     * 格式化金额显示（万元）
     */
    public String getFormattedAmount() {
        if (allocatedAmount == null) {
            return "0.00万";
        }
        BigDecimal amountInWan = allocatedAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
        return amountInWan + "万";
    }

    // ==================== 常量定义 ====================

    /**
     * 默认精度常量
     */
    public static class Precision {
        public static final int RATIO_SCALE = 4;        // 比例精度
        public static final int AMOUNT_SCALE = 2;       // 金额精度
        public static final int BASIS_SCALE = 4;        // 基础值精度
    }

    // 手动添加setter方法，确保编译通过
    public CostAllocationDetailEntity setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
        return this;
    }

    public CostAllocationDetailEntity setTargetCenterId(String targetCenterId) {
        this.targetCenterId = targetCenterId;
        return this;
    }

    public CostAllocationDetailEntity setAllocationBasisValue(BigDecimal allocationBasisValue) {
        this.allocationBasisValue = allocationBasisValue;
        return this;
    }

    public CostAllocationDetailEntity setAllocationRatio(BigDecimal allocationRatio) {
        this.allocationRatio = allocationRatio;
        return this;
    }

    public CostAllocationDetailEntity setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
        return this;
    }

    public CostAllocationDetailEntity setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    /**
     * 验证常量
     */
    public static class Validation {
        public static final BigDecimal MIN_RATIO = new BigDecimal("0.0001");
        public static final BigDecimal MAX_RATIO = new BigDecimal("1.0000");
        public static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;
    }
}