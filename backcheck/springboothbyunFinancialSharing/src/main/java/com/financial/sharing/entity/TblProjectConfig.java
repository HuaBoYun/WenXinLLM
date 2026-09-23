package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目配置表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROJECT_CONFIG")
@ApiModel(value = "TblProjectConfig", description = "项目配置表")
public class TblProjectConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PROJECT_ID")
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @TableField("PROJECT_CODE")
    @ApiModelProperty(value = "项目编码")
    private String projectCode;

    @TableField("PROJECT_NAME")
    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @TableField("PROJECT_TYPE")
    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @TableField("PROJECT_MANAGER_ID")
    @ApiModelProperty(value = "项目经理ID")
    private String projectManagerId;

    @TableField("PROJECT_MANAGER_NAME")
    @ApiModelProperty(value = "项目经理姓名")
    private String projectManagerName;

    @TableField(exist = false)
    @ApiModelProperty(value = "项目经理(前端字段,映射到projectManagerName)")
    private String projectManager;

    @TableField("DEPARTMENT_ID")
    @ApiModelProperty(value = "部门ID")
    private String departmentId;

    @TableField("DEPARTMENT_NAME")
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @TableField("PROJECT_STATUS")
    @ApiModelProperty(value = "项目状态")
    private String projectStatus;

    @TableField("START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @TableField("END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @TableField("BUDGET_AMOUNT")
    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;

    @TableField("USED_AMOUNT")
    @ApiModelProperty(value = "已使用金额")
    private BigDecimal usedAmount;

    @TableField("PROJECT_DESCRIPTION")
    @ApiModelProperty(value = "项目描述")
    private String projectDescription;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}

