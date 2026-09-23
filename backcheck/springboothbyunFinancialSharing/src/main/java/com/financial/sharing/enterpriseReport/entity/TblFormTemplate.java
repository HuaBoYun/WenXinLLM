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
 * 表单模板表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_FORM_TEMPLATE")
@ApiModel(value = "TblFormTemplate对象", description = "表单模板表")
public class TblFormTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板ID")
    @TableId(value = "TEMPLATE_ID", type = IdType.ASSIGN_UUID)
    private String templateId;

    @ApiModelProperty(value = "表单组ID")
    @TableField("GROUP_ID")
    private String groupId;

    @ApiModelProperty(value = "模板编码")
    @TableField("TEMPLATE_CODE")
    private String templateCode;

    @ApiModelProperty(value = "模板名称")
    @TableField("TEMPLATE_NAME")
    private String templateName;

    @ApiModelProperty(value = "模板类型：FIXED(固定表)/FLOATING(浮动表)")
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @ApiModelProperty(value = "模板内容（JSON格式）")
    @TableField("TEMPLATE_CONTENT")
    private String templateContent;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION_NO")
    private String versionNo;

    @ApiModelProperty(value = "起始期间")
    @TableField("START_PERIOD")
    private String startPeriod;

    @ApiModelProperty(value = "终止期间")
    @TableField("END_PERIOD")
    private String endPeriod;

    @ApiModelProperty(value = "是否默认版本：Y/N")
    @TableField("IS_DEFAULT")
    private String isDefault;

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
    @ApiModelProperty(value = "表单组名称")
    @TableField(exist = false)
    private String groupName;
}

