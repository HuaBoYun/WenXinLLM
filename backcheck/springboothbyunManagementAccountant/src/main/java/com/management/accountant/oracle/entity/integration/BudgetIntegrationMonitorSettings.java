package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 集成监控设置实体类
 *
 * @author AI Agent
 * @date 2026-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INTEGRATION_MONITOR_SETTINGS")
public class BudgetIntegrationMonitorSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "SETTINGS_ID", type = IdType.ASSIGN_UUID)
    private String settingsId;

    @TableField("SETTINGS_KEY")
    private String settingsKey;

    @TableField("SETTINGS_VALUE")
    private String settingsValue;

    @TableField("SETTINGS_DESC")
    private String settingsDesc;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
