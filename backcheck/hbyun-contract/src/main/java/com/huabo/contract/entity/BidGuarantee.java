package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投标保证金表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bid_guarantee")
public class BidGuarantee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 招投标项目ID
     */
    @TableField("bidding_project_id")
    private Long biddingProjectId;

    /**
     * 保证金编号
     */
    @TableField("guarantee_no")
    private String guaranteeNo;

    /**
     * 保证金类型(1:投标保证金,2:履约保证金,3:质量保证金)
     */
    @TableField("guarantee_type")
    private Integer guaranteeType;

    /**
     * 保证金形式(1:现金,2:银行保函,3:保险保函,4:其他)
     */
    @TableField("guarantee_form")
    private Integer guaranteeForm;

    /**
     * 保证金金额
     */
    @TableField("guarantee_amount")
    private BigDecimal guaranteeAmount;

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
    @TableField("guarantee_letter_no")
    private String guaranteeLetterNo;

    /**
     * 保证金状态(1:待缴纳,2:已缴纳,3:已退还,4:已没收,5:已过期)
     */
    @TableField("guarantee_status")
    private Integer guaranteeStatus;

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
     * 没收原因
     */
    @TableField("confiscation_reason")
    private String confiscationReason;

    /**
     * 没收时间
     */
    @TableField("confiscation_time")
    private Date confiscationTime;

    /**
     * 凭证文件路径
     */
    @TableField("voucher_file_path")
    private String voucherFilePath;

    /**
     * 凭证文件名称
     */
    @TableField("voucher_file_name")
    private String voucherFileName;

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
    public String getGuaranteeTypeName() {
        if (guaranteeType == null) {
            return "";
        }
        switch (guaranteeType) {
            case 1:
                return "投标保证金";
            case 2:
                return "履约保证金";
            case 3:
                return "质量保证金";
            default:
                return "未知";
        }
    }

    /**
     * 获取保证金形式名称
     */
    public String getGuaranteeFormName() {
        if (guaranteeForm == null) {
            return "";
        }
        switch (guaranteeForm) {
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
    public String getGuaranteeStatusName() {
        if (guaranteeStatus == null) {
            return "";
        }
        switch (guaranteeStatus) {
            case 1:
                return "待缴纳";
            case 2:
                return "已缴纳";
            case 3:
                return "已退还";
            case 4:
                return "已没收";
            case 5:
                return "已过期";
            default:
                return "未知";
        }
    }

    /**
     * 获取保证金状态颜色
     */
    public String getGuaranteeStatusColor() {
        if (guaranteeStatus == null) {
            return "#909399";
        }
        switch (guaranteeStatus) {
            case 1:
                return "#E6A23C"; // 橙色
            case 2:
                return "#67C23A"; // 绿色
            case 3:
                return "#409EFF"; // 蓝色
            case 4:
                return "#F56C6C"; // 红色
            case 5:
                return "#909399"; // 灰色
            default:
                return "#909399";
        }
    }

    /**
     * 获取保证金类型颜色
     */
    public String getGuaranteeTypeColor() {
        if (guaranteeType == null) {
            return "#909399";
        }
        switch (guaranteeType) {
            case 1:
                return "#409EFF"; // 蓝色
            case 2:
                return "#67C23A"; // 绿色
            case 3:
                return "#E6A23C"; // 橙色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否已缴纳
     */
    public boolean isPaid() {
        return guaranteeStatus != null && guaranteeStatus == 2;
    }

    /**
     * 判断是否已退还
     */
    public boolean isRefunded() {
        return guaranteeStatus != null && guaranteeStatus == 3;
    }

    /**
     * 判断是否已没收
     */
    public boolean isConfiscated() {
        return guaranteeStatus != null && guaranteeStatus == 4;
    }

    /**
     * 判断是否已过期
     */
    public boolean isExpired() {
        if (expiryTime == null) {
            return false;
        }
        return new Date().after(expiryTime) && guaranteeStatus != null && guaranteeStatus == 2;
    }

    /**
     * 判断是否即将过期（7天内）
     */
    public boolean isExpiringSoon() {
        if (expiryTime == null || !isPaid()) {
            return false;
        }
        long diff = expiryTime.getTime() - new Date().getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return days >= 0 && days <= 7;
    }

    /**
     * 获取保证金金额显示文本
     */
    public String getGuaranteeAmountText() {
        if (guaranteeAmount == null) {
            return "未知";
        }
        if (guaranteeAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return guaranteeAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return guaranteeAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取退还金额显示文本
     */
    public String getRefundAmountText() {
        if (refundAmount == null) {
            return "未退还";
        }
        if (refundAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return refundAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return refundAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 计算距离到期时间的天数
     */
    public long getDaysUntilExpiry() {
        if (expiryTime == null) {
            return -1;
        }
        long diff = expiryTime.getTime() - new Date().getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    /**
     * 判断是否为现金保证金
     */
    public boolean isCashGuarantee() {
        return guaranteeForm != null && guaranteeForm == 1;
    }

    /**
     * 判断是否为保函
     */
    public boolean isGuaranteeLetter() {
        return guaranteeForm != null && (guaranteeForm == 2 || guaranteeForm == 3);
    }

    /**
     * 判断是否可以退还
     */
    public boolean canRefund() {
        return isPaid() && !isExpired() && !isConfiscated();
    }

    /**
     * 判断是否需要提醒
     */
    public boolean needReminder() {
        return isExpiringSoon() || (guaranteeStatus != null && guaranteeStatus == 1);
    }

    /**
     * 获取银行账号掩码显示
     */
    public String getBankAccountMask() {
        if (bankAccount == null || bankAccount.length() <= 8) {
            return bankAccount;
        }
        String prefix = bankAccount.substring(0, 4);
        String suffix = bankAccount.substring(bankAccount.length() - 4);
        return prefix + "****" + suffix;
    }
}
