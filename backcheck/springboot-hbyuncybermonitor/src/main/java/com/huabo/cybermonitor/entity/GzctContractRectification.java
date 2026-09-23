package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同整改记录实体
 * 对应表: GZCT_CONTRACT_RECTIFICATION
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_CONTRACT_RECTIFICATION")
public class GzctContractRectification {

    /**
     * 整改记录ID
     */
    @TableId(value = "RECTIFICATION_ID", type = IdType.ASSIGN_UUID)
    private String rectificationId;

    /**
     * 关联合同编号
     */
    @TableField("CONTRACT_NO")
    private String contractNo;

    /**
     * 关联合同ID
     */
    @TableField("CONTRACT_ID")
    private String contractId;

    /**
     * 违规类型
     */
    @TableField("VIOLATION_TYPE")
    private String violationType;

    /**
     * 整改措施
     */
    @TableField("MEASURES")
    private String measures;

    /**
     * 责任人
     */
    @TableField("OWNER")
    private String owner;

    /**
     * 完成时限
     */
    @TableField("DEADLINE")
    private LocalDate deadline;

    /**
     * 整改说明/备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 整改状态: PENDING-待整改, PROCESSING-整改中, CLOSED-已关闭
     */
    @TableField("RECTIFY_STATUS")
    private String rectifyStatus;

    /**
     * 下发人
     */
    @TableField("ISSUED_BY")
    private String issuedBy;

    /**
     * 下发时间
     */
    @TableField("ISSUED_TIME")
    private LocalDateTime issuedTime;

    /**
     * 完成时间
     */
    @TableField("COMPLETE_TIME")
    private LocalDateTime completeTime;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
