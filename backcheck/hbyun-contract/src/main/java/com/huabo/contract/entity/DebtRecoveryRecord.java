package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权回收登记记录实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("debt_recovery_record")
@Schema(name="DebtRecoveryRecord对象", description="债权回收登记记录")
public class DebtRecoveryRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "债权ID")
    @TableField("debt_id")
    private Long debtId;

    @Schema(name = "回收日期")
    @TableField("recovery_date")
    private Date recoveryDate;

    @Schema(name = "回收金额")
    @TableField("recovery_amount")
    private BigDecimal recoveryAmount;

    @Schema(name = "回收方式(1:银行转账,2:现金支付,3:支票支付,4:承兑汇票,5:其他方式)")
    @TableField("recovery_method")
    private Integer recoveryMethod;

    @Schema(name = "回收人员ID")
    @TableField("recovery_person_id")
    private Long recoveryPersonId;

    @Schema(name = "回收人员姓名")
    @TableField("recovery_person_name")
    private String recoveryPersonName;

    @Schema(name = "凭证号码")
    @TableField("voucher_number")
    private String voucherNumber;

    @Schema(name = "银行信息")
    @TableField("bank_info")
    private String bankInfo;

    @Schema(name = "付款人信息")
    @TableField("payer_info")
    private String payerInfo;

    @Schema(name = "回收说明")
    @TableField("recovery_description")
    private String recoveryDescription;

    @Schema(name = "相关附件")
    @TableField("attachments")
    private String attachments;

    @Schema(name = "备注")
    @TableField("remarks")
    private String remarks;

    @Schema(name = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(name = "创建人ID")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    @Schema(name = "更新人ID")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @Schema(name = "删除标记(0:未删除,1:已删除)")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
