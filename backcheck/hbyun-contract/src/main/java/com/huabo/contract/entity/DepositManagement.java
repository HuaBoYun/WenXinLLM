package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 保证金管理表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("deposit_management")
public class DepositManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 招投标项目ID
     */
    @TableField("bidding_project_id")
    private Long biddingProjectId;

    /**
     * 保证金编号
     */
    @TableField("deposit_no")
    private String depositNo;

    /**
     * 保证金类型(1:投标保证金,2:履约保证金,3:质量保证金,4:其他)
     */
    @TableField("deposit_type")
    private Integer depositType;

    /**
     * 保证金金额
     */
    @TableField("deposit_amount")
    private BigDecimal depositAmount;

    /**
     * 缴纳方式(1:现金,2:银行保函,3:保险保函,4:其他)
     */
    @TableField("payment_method")
    private Integer paymentMethod;

    /**
     * 缴纳时间
     */
    @TableField("payment_time")
    private Date paymentTime;

    /**
     * 到期时间
     */
    @TableField("expiry_time")
    private Date expiryTime;

    /**
     * 保证金状态(1:已缴纳,2:已退还,3:已没收,4:已转履约,5:逾期)
     */
    @TableField("deposit_status")
    private Integer depositStatus;

    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 银行账号
     */
    @TableField("bank_account")
    private String bankAccount;

    /**
     * 保函编号
     */
    @TableField("guarantee_no")
    private String guaranteeNo;

    /**
     * 保函文件路径
     */
    @TableField("guarantee_file_path")
    private String guaranteeFilePath;

    /**
     * 退还时间
     */
    @TableField("refund_time")
    private Date refundTime;

    /**
     * 退还金额
     */
    @TableField("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退还原因
     */
    @TableField("refund_reason")
    private String refundReason;

    /**
     * 没收原因
     */
    @TableField("confiscation_reason")
    private String confiscationReason;

    /**
     * 负责人ID
     */
    @TableField("manager_id")
    private Long managerId;

    /**
     * 负责人姓名
     */
    @TableField("manager_name")
    private String managerName;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 获取保证金类型名称
     */
    public String getDepositTypeName() {
        if (depositType == null) {
            return "";
        }
        switch (depositType) {
            case 1:
                return "投标保证金";
            case 2:
                return "履约保证金";
            case 3:
                return "质量保证金";
            case 4:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取缴纳方式名称
     */
    public String getPaymentMethodName() {
        if (paymentMethod == null) {
            return "";
        }
        switch (paymentMethod) {
            case 1:
                return "现金";
            case 2:
                return "银行保函";
            case 3:
                return "保险保函";
            case 4:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取保证金状态名称
     */
    public String getDepositStatusName() {
        if (depositStatus == null) {
            return "";
        }
        switch (depositStatus) {
            case 1:
                return "已缴纳";
            case 2:
                return "已退还";
            case 3:
                return "已没收";
            case 4:
                return "已转履约";
            case 5:
                return "逾期";
            default:
                return "未知";
        }
    }

    /**
     * 判断是否即将到期（7天内）
     */
    public boolean isExpiringSoon() {
        if (expiryTime == null) {
            return false;
        }
        long diffTime = expiryTime.getTime() - System.currentTimeMillis();
        long diffDays = diffTime / (24 * 60 * 60 * 1000);
        return diffDays <= 7 && diffDays >= 0;
    }

    /**
     * 判断是否已逾期
     */
    public boolean isOverdue() {
        if (expiryTime == null) {
            return false;
        }
        return expiryTime.getTime() < System.currentTimeMillis();
    }
}
