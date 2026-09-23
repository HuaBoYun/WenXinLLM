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
 * 报表任务表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_REPORT_TASK")
@ApiModel(value = "TblReportTask对象", description = "报表任务表")
public class TblReportTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    @ApiModelProperty(value = "任务编码")
    @TableField("TASK_CODE")
    private String taskCode;

    @ApiModelProperty(value = "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    @ApiModelProperty(value = "表单组ID")
    @TableField("GROUP_ID")
    private String groupId;

    @ApiModelProperty(value = "组织体系")
    @TableField("ORG_SYSTEM")
    private String orgSystem;

    @ApiModelProperty(value = "周期类型：YEAR/HALF_YEAR/QUARTER/MONTH")
    @TableField("PERIOD_TYPE")
    private String periodType;

    @ApiModelProperty(value = "填报期间偏移")
    @TableField("PERIOD_OFFSET")
    private Integer periodOffset;

    @ApiModelProperty(value = "起始期间")
    @TableField("START_PERIOD")
    private String startPeriod;

    @ApiModelProperty(value = "终止期间")
    @TableField("END_PERIOD")
    private String endPeriod;

    @ApiModelProperty(value = "是否逐级上报：Y/N")
    @TableField("IS_HIERARCHICAL")
    private String isHierarchical;

    @ApiModelProperty(value = "截止天数")
    @TableField("DEADLINE_DAYS")
    private Integer deadlineDays;

    @ApiModelProperty(value = "是否启用节点检查：Y/N")
    @TableField("IS_NODE_CHECK")
    private String isNodeCheck;

    @ApiModelProperty(value = "是否启用归档控制：Y/N")
    @TableField("IS_ARCHIVE_CONTROL")
    private String isArchiveControl;

    @ApiModelProperty(value = "状态：DRAFT(草稿)/PUBLISHED(已发布)")
    @TableField("STATUS")
    private String status;

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
    @ApiModelProperty(value = "表单组名称")
    @TableField(exist = false)
    private String groupName;
}

