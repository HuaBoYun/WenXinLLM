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
import java.util.List;

/**
 * 表单目录表
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_FORM_DIRECTORY")
@ApiModel(value = "TblFormDirectory对象", description = "表单目录表")
public class TblFormDirectory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "目录ID")
    @TableId(value = "DIRECTORY_ID", type = IdType.ASSIGN_UUID)
    private String directoryId;

    @ApiModelProperty(value = "目录编码")
    @TableField("DIRECTORY_CODE")
    private String directoryCode;

    @ApiModelProperty(value = "目录名称")
    @TableField("DIRECTORY_NAME")
    private String directoryName;

    @ApiModelProperty(value = "父目录ID")
    @TableField("PARENT_DIRECTORY_ID")
    private String parentDirectoryId;

    @ApiModelProperty(value = "目录层级")
    @TableField("DIRECTORY_LEVEL")
    private Integer directoryLevel;

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
    @ApiModelProperty(value = "子目录列表")
    @TableField(exist = false)
    private List<TblFormDirectory> children;

    @ApiModelProperty(value = "父目录名称")
    @TableField(exist = false)
    private String parentDirectoryName;
}

