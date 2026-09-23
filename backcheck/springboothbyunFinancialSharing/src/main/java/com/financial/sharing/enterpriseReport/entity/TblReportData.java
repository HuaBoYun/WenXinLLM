package com.financial.sharing.enterpriseReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表数据表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_REPORT_DATA")
@ApiModel(value = "TblReportData对象", description = "报表数据表")
public class TblReportData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据ID")
    @TableId(value = "DATA_ID", type = IdType.ASSIGN_UUID)
    private String dataId;

    @ApiModelProperty(value = "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "模板ID")
    @TableField("TEMPLATE_ID")
    private String templateId;

    @ApiModelProperty(value = "指标ID")
    @TableField("INDICATOR_ID")
    private String indicatorId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "期间")
    @TableField("PERIOD")
    private String period;

    @ApiModelProperty(value = "维度值（JSON格式）")
    @TableField("DIMENSION_VALUES")
    private String dimensionValues;

    @ApiModelProperty(value = "数据值")
    @TableField("DATA_VALUE")
    private String dataValue;

    @ApiModelProperty(value = "数据来源：MANUAL(手工)/FETCH(取数)/CALCULATION(计算)")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @ApiModelProperty(value = "单元格颜色")
    @TableField("CELL_COLOR")
    private String cellColor;

    @ApiModelProperty(value = "是否可编辑：Y/N")
    @TableField("IS_EDITABLE")
    private String isEditable;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private String tenantId;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @ApiModelProperty(value = "修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    // 非数据库字段
    @ApiModelProperty(value = "任务名称")
    @TableField(exist = false)
    private String taskName;

    @ApiModelProperty(value = "模板名称")
    @TableField(exist = false)
    private String templateName;

    @ApiModelProperty(value = "指标名称")
    @TableField(exist = false)
    private String indicatorName;

    @ApiModelProperty(value = "组织名称")
    @TableField(exist = false)
    private String orgName;
}

