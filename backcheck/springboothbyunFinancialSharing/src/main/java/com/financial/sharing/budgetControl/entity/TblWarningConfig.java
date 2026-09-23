package com.financial.sharing.budgetControl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预警配置实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_WARNING_CONFIG")
public class TblWarningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CONFIG_ID", type = IdType.ASSIGN_UUID)
    private String configId;

    @TableField("CONFIG_CODE")
    private String configCode;

    @TableField("CONFIG_NAME")
    private String configName;

    @TableField("WARNING_TYPE")
    private String warningType;

    @TableField("BIZ_ORG_ID")
    private String bizOrgId;

    @TableField("SUBJECT_CODE")
    private String subjectCode;

    @TableField("PERIOD")
    private String period;

    @TableField("THRESHOLD_TYPE")
    private String thresholdType;

    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @TableField("WARNING_LEVEL")
    private String warningLevel;

    @TableField("RECEIVERS")
    private String receivers;

    @TableField("MESSAGE_TEMPLATE")
    private String messageTemplate;

    @TableField("SEND_METHODS")
    private String sendMethods;

    @TableField("IS_ENABLED")
    private String isEnabled;

    @TableField("REMARK")
    private String remark;

    @TableField("STATUS")
    private String status;

    @TableField("ORG_ID")
    private String orgId;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("UPDATE_TIME")
    private Date updateTime;
}

