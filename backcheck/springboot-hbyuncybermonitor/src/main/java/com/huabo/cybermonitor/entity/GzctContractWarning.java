package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 合同风险预警实体
 * 持久化预警的处置状态、关闭状态、负责人等信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_CONTRACT_WARNING")
public class GzctContractWarning extends Model<GzctContractWarning> {

    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID)
    private String warningId;

    /** 预警编号（唯一标识，基于合同ID+预警类型生成） */
    @TableField("WARN_NO")
    private String warnNo;

    /** 关联合同ID */
    @TableField("CONTRACT_ID")
    private String contractId;

    /** 关联合同编号 */
    @TableField("CONTRACT_NO")
    private String contractNo;

    /** 合同名称 */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /** 企业名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 预警类型描述 */
    @TableField("WARN_TYPE")
    private String warnType;

    /** 风险级别: HIGH/MEDIUM/LOW */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /** 风险评分 0-100 */
    @TableField("RISK_SCORE")
    private Integer riskScore;

    /** 处置状态: PENDING/PROCESSING/CLOSED */
    @TableField("STATUS")
    private String status;

    /** 负责人 */
    @TableField("OWNER")
    private String owner;

    /** 处置措施 */
    @TableField("MEASURES")
    private String measures;

    /** 计划完成日期 */
    @TableField("DEADLINE")
    private String deadline;

    /** 处置说明 */
    @TableField("REMARK")
    private String remark;

    /** 预警触发时间 */
    @TableField("WARN_TIME")
    private LocalDateTime warnTime;

    /** 处置时间 */
    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    /** 关闭时间 */
    @TableField("CLOSE_TIME")
    private LocalDateTime closeTime;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("DEL_FLAG")
    private String delFlag;
}
