package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.ReservedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName DesignPaymentInfoEntity
 * @Description
 * @DATE 2023/9/30
 */

@Data
@TableName("TBL_YQNS_DESIGN_PAYMENT")
@Schema(name="工程结算设计付款情况统计表")
@Accessors(chain = true)
public class DesignPaymentInfoEntity extends ReservedEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="PLAN_ID")
    @Schema(name="计划文号")
    private String planId;

    @TableField(value="CONTRACT_NO")
    @Schema(name="合同编号")
    private String contractNo;

    @TableField(value="ORG_ID")
    @Schema(name="施工单位")
    private String orgId;

	@TableField(exist = false)
	private TblOrganization org;

    @TableField(value="CONTRACT_AMOUNT")
    @Schema(name="合同金额")
    private BigDecimal contractAmount;

    @TableField(value="SETTLEMENT_AMOUNT")
    @Schema(name="结算金额")
    private BigDecimal settlementAmount;

    @TableField(value="MATERIAL_AMOUNT")
    @Schema(name="甲供物资金额")
    private BigDecimal materialAmount;

    @TableField(value="PAID_AMOUNT")
    @Schema(name="已付款金额")
    private BigDecimal paidAmount;

    @TableField(value="BALANCE_PAYMENT")
    @Schema(name="尾款")
    private BigDecimal balancePayment;

    @TableField(value="FIRST_TRIAL_TIME")
    @Schema(name="一审报审结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date firstTrialTime;

    @TableField(value="FIRST_TRIAL_AMOUNT")
    @Schema(name="一审报审金额")
    private BigDecimal firstTrialAmount;

    @TableField(value="SECOND_TRIAL_TIME")
    @Schema(name="二审报审结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date secondTrialTime;

    @TableField(value="SECOND_TRIAL_AMOUNT")
    @Schema(name="二审报审金额")
    private BigDecimal secondTrialAmount;

    @TableField(value="DRAWING_DESIGN_TIME")
    @Schema(name="施工图纸设计时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date drawingDesignTime;

    @TableField(value="CONTRACT_START_TIME")
    @Schema(name="合同开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date contractStartTime;

    @TableField(value="CONTRACT_END_TIME")
    @Schema(name="合同竣工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date contractEndTime;

    @TableField(value="WORK_START_TIME")
    @Schema(name="实际开工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workStartTime;

    @TableField(value="WORK_END_TIME")
    @Schema(name="实际竣工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workEndTime;

    @TableField(value="DELAY_TIMES")
    @Schema(name="延期次数")
    private Integer delayTimes;

    @TableField(value="DELAY_DAYS")
    @Schema(name="延期天数")
    private Integer delayDays;

    @TableField(value="DELAY_REASON")
    @Schema(name="延期原因")
    private String delayReason;

    @TableField(value="ARCHIVE_TIME")
    @Schema(name="竣工资料存档时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date archiveTime;

    @TableField(value="BIDDING_SITUATION")
    @Schema(name="招标情况")
    private String biddingSituation;

    @TableField(value="IS_CONSISTENT_CON_PLAN")
    @Schema(name="合同施工内容与计划内容是否一致 1:一致 0：不一致")
    private String isConsistentConPlan;

    @TableField(value="IS_CONSISTENT_CON_WORK")
    @Schema(name="合同施工内容与实际施工是否一致 1:一致 0：不一致")
    private String isConsistentConWork;

    @TableField(value="REMARK")
    @Schema(name="备注")
    private String remark;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private String attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

}
