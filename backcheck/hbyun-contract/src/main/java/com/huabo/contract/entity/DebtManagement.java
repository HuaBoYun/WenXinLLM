package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权管理表
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("debt_management")
public class DebtManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 项目名称（冗余字段，列表展示用）
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 业务侧债权ID（前端列表 row.debtId 字段对应）
     */
    @TableField("debt_id")
    private String debtId;

    /**
     * 债权编号
     */
    @TableField("debt_no")
    private String debtNo;

    /**
     * 债务人名称
     */
    @TableField("debtor_name")
    private String debtorName;

    /**
     * 债务人类型(1:企业,2:个人,3:政府机构)
     */
    @TableField("debtor_type")
    private Integer debtorType;

    /**
     * 债权金额
     */
    @TableField("debt_amount")
    private BigDecimal debtAmount;

    /**
     * 债权类型(1:工程款,2:材料款,3:设备款,4:其他)
     */
    @TableField("debt_type")
    private Integer debtType;

    /**
     * 债权来源
     */
    @TableField("debt_source")
    private String debtSource;

    /**
     * 合同编号
     */
    @TableField("contract_no")
    private String contractNo;

    /**
     * 债权形成日期
     */
    @TableField("debt_date")
    private Date debtDate;

    /**
     * 到期日期
     */
    @TableField("due_date")
    private Date dueDate;

    /**
     * 逾期天数
     */
    @TableField("overdue_days")
    private Integer overdueDays;

    /**
     * 已收回金额
     */
    @TableField("collected_amount")
    private BigDecimal collectedAmount;

    /**
     * 剩余金额
     */
    @TableField("remaining_amount")
    private BigDecimal remainingAmount;

    /**
     * 债权状态(1:正常,2:逾期,3:呆账,4:已收回,5:已核销)
     */
    @TableField("debt_status")
    private Integer debtStatus;

    /**
     * 收款难度(1:容易,2:一般,3:困难,4:极难)
     */
    @TableField("collection_difficulty")
    private Integer collectionDifficulty;

    /**
     * 催收措施
     */
    @TableField("collection_measures")
    private String collectionMeasures;

    /**
     * 负责人ID
     */
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    /**
     * 法律状态(1:正常,2:仲裁中,3:诉讼中,4:执行中)
     */
    @TableField("legal_status")
    private Integer legalStatus;

    /**
     * 律师信息
     */
    @TableField("lawyer_info")
    private String lawyerInfo;

    /**
     * 法院案件号
     */
    @TableField("court_case_no")
    private String courtCaseNo;

    /**
     * 诉讼状态
     */
    @TableField("litigation_status")
    private Integer litigationStatus;

    /**
     * 和解协议
     */
    @TableField("settlement_agreement")
    private String settlementAgreement;

    /**
     * 核销原因
     */
    @TableField("write_off_reason")
    private String writeOffReason;

    /**
     * 核销日期
     */
    @TableField("write_off_date")
    private Date writeOffDate;

    /**
     * 核销审批人ID
     */
    @TableField("write_off_approver_id")
    private Long writeOffApproverId;

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
     * 删除标志(0:未删除,1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}