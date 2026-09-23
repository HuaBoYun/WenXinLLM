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
 * 表单组表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_FORM_GROUP")
@ApiModel(value = "TblFormGroup对象", description = "表单组表")
public class TblFormGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表单组ID")
    @TableId(value = "GROUP_ID", type = IdType.ASSIGN_UUID)
    private String groupId;

    @ApiModelProperty(value = "目录ID")
    @TableField("DIRECTORY_ID")
    private String directoryId;

    @ApiModelProperty(value = "表单组编码")
    @TableField("GROUP_CODE")
    private String groupCode;

    @ApiModelProperty(value = "表单组名称")
    @TableField("GROUP_NAME")
    private String groupName;

    @ApiModelProperty(value = "固定维（JSON数组）")
    @TableField("FIXED_DIMENSIONS")
    private String fixedDimensions;

    @ApiModelProperty(value = "参数维（JSON数组）")
    @TableField("PARAMETER_DIMENSIONS")
    private String parameterDimensions;

    @ApiModelProperty(value = "周期类型：YEAR(年)/HALF_YEAR(半年)/QUARTER(季度)/MONTH(月)")
    @TableField("PERIOD_TYPE")
    private String periodType;

    @ApiModelProperty(value = "排序号")
    @TableField("SORT_NO")
    private Integer sortNo;

    @ApiModelProperty(value = "状态：ACTIVE(启用)/INACTIVE(停用)")
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
    @ApiModelProperty(value = "目录名称")
    @TableField(exist = false)
    private String directoryName;
}

