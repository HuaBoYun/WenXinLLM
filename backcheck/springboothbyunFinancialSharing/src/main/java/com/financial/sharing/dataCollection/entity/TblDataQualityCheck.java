package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据质量检查记录实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_DATA_QUALITY_CHECK")
public class TblDataQualityCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检查ID
     */
    @TableId(value = "CHECK_ID", type = IdType.ASSIGN_ID)
    private Long checkId;

    /**
     * 检查编码
     */
    @TableField("CHECK_CODE")
    private String checkCode;

    /**
     * 检查名称
     */
    @TableField("CHECK_NAME")
    private String checkName;

    /**
     * 检查类型：MANUAL(手动)/AUTO(自动)
     */
    @TableField("CHECK_TYPE")
    private String checkType;

    /**
     * 检查对象类型：TASK(归集任务)/TABLE(数据表)/FIELD(字段)
     */
    @TableField("TARGET_TYPE")
    private String targetType;

    /**
     * 检查对象ID
     */
    @TableField("TARGET_ID")
    private Long targetId;

    /**
     * 检查对象名称
     */
    @TableField("TARGET_NAME")
    private String targetName;

    /**
     * 检查状态：PENDING(待检查)/CHECKING(检查中)/COMPLETED(已完成)/FAILED(失败)
     */
    @TableField("CHECK_STATUS")
    private String checkStatus;

    /**
     * 开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 检查时长（毫秒）
     */
    @TableField("CHECK_DURATION")
    private Long checkDuration;

    /**
     * 检查规则总数
     */
    @TableField("TOTAL_RULES")
    private Integer totalRules;

    /**
     * 通过规则数
     */
    @TableField("PASSED_RULES")
    private Integer passedRules;

    /**
     * 失败规则数
     */
    @TableField("FAILED_RULES")
    private Integer failedRules;

    /**
     * 警告规则数
     */
    @TableField("WARNING_RULES")
    private Integer warningRules;

    /**
     * 质量评分（0-100）
     */
    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    /**
     * 质量等级：EXCELLENT(优秀)/GOOD(良好)/FAIR(一般)/POOR(较差)
     */
    @TableField("QUALITY_LEVEL")
    private String qualityLevel;

    /**
     * 检查结果详情（JSON格式）
     */
    @TableField("CHECK_RESULT")
    private String checkResult;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

