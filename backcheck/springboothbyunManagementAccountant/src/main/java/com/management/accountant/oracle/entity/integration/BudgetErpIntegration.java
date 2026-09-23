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
 * 预算ERP系统集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_ERP_INTEGRATION")
public class BudgetErpIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ERP集成ID (主键)
     */
    @TableId(value = "ERP_ID", type = IdType.ASSIGN_UUID)
    private String erpId;

    /**
     * ERP集成编码
     */
    @TableField("ERP_CODE")
    private String erpCode;

    /**
     * ERP集成名称
     */
    @TableField("ERP_NAME")
    private String erpName;

    /**
     * ERP系统类型 (SAP/ORACLE/UFIDA/KINGDEE/CUSTOM)
     */
    @TableField("ERP_TYPE")
    private String erpType;

    /**
     * ERP系统版本
     */
    @TableField("ERP_VERSION")
    private String erpVersion;

    /**
     * 连接方式 (API/DATABASE/FILE/WEBSERVICE)
     */
    @TableField("CONNECTION_TYPE")
    private String connectionType;

    /**
     * 连接配置 (JSON格式)
     */
    @TableField("CONNECTION_CONFIG")
    private String connectionConfig;

    /**
     * 数据同步方向 (IMPORT/EXPORT/BIDIRECTIONAL)
     */
    @TableField("SYNC_DIRECTION")
    private String syncDirection;

    /**
     * 同步频率 (REALTIME/HOURLY/DAILY/WEEKLY/MONTHLY/MANUAL)
     */
    @TableField("SYNC_FREQUENCY")
    private String syncFrequency;

    /**
     * 同步时间配置 (CRON表达式)
     */
    @TableField("SYNC_SCHEDULE")
    private String syncSchedule;

    /**
     * 数据映射配置 (JSON格式)
     */
    @TableField("DATA_MAPPING")
    private String dataMapping;

    /**
     * 同步范围 (JSON格式)
     */
    @TableField("SYNC_SCOPE")
    private String syncScope;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 最后同步时间
     */
    @TableField("LAST_SYNC_TIME")
    private Date lastSyncTime;

    /**
     * 下次同步时间
     */
    @TableField("NEXT_SYNC_TIME")
    private Date nextSyncTime;

    /**
     * 同步成功次数
     */
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    /**
     * 同步失败次数
     */
    @TableField("FAILURE_COUNT")
    private Integer failureCount;

    /**
     * 最后同步记录数
     */
    @TableField("LAST_SYNC_RECORDS")
    private Integer lastSyncRecords;

    /**
     * 集成状态 (ACTIVE/INACTIVE/ERROR/TESTING)
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    /**
     * 删除标志: 0-正常, 1-已删除
     */
    private Integer delFlag;


    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========
    public String getErpId() {
        return erpId;
    }
    public void setErpId(String erpId) {
        this.erpId = erpId;
    }
}

