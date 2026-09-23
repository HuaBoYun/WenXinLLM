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
 * BI数据集实体类
 *
 * @author AI Agent
 * @date 2026-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_BI_DATASET")
public class BudgetBiDataset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "DATASET_ID", type = IdType.ASSIGN_UUID)
    private String datasetId;

    @TableField("BI_ID")
    private String biId;

    @TableField("DATASET_NAME")
    private String datasetName;

    @TableField("DATASET_TYPE")
    private String datasetType;

    @TableField("TABLE_COUNT")
    private Integer tableCount;

    @TableField("ROW_COUNT")
    private Integer rowCount;

    @TableField("LAST_REFRESH")
    private Date lastRefresh;

    @TableField("REFRESH_MODE")
    private String refreshMode;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATED_BY")
    private String createdBy;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_BY")
    private String updatedBy;

    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
