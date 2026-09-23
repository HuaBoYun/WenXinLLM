package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算模型版本实体类
 * 
 * @description 预算模型版本管理实体,支持模型版本控制和历史追溯
 * @author AI Agent
 * @date 2026-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_MODEL_VERSION")
@ApiModel(value = "BudgetModelVersion对象", description = "预算模型版本")
public class BudgetModelVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "VERSION_ID", type = IdType.ASSIGN_UUID)
    private String versionId;

    @ApiModelProperty(value = "模型ID")
    @TableField("MODEL_ID")
    private String modelId;

    @ApiModelProperty(value = "版本号（如：V1.0、V1.1）")
    @TableField("VERSION_NO")
    private String versionNo;

    @ApiModelProperty(value = "版本名称")
    @TableField("VERSION_NAME")
    private String versionName;

    @ApiModelProperty(value = "版本类型:MANUAL-手动创建,AUTO-自动创建")
    @TableField("VERSION_TYPE")
    private String versionType;

    @ApiModelProperty(value = "版本状态:DRAFT-草稿,PUBLISHED-已发布,ARCHIVED-已归档")
    @TableField("VERSION_STATUS")
    private String versionStatus;

    @ApiModelProperty(value = "模型配置（JSON格式）")
    @TableField("MODEL_CONFIG")
    private String modelConfig;

    @ApiModelProperty(value = "变更说明")
    @TableField("CHANGE_DESCRIPTION")
    private String changeDescription;

    @ApiModelProperty(value = "变更类型:CREATE-创建,UPDATE-更新,COPY-复制")
    @TableField("CHANGE_TYPE")
    private String changeType;

    @ApiModelProperty(value = "是否当前版本:0-否,1-是")
    @TableField("IS_CURRENT")
    private Integer isCurrent;

    @ApiModelProperty(value = "是否已发布:0-否,1-是")
    @TableField("IS_PUBLISHED")
    private Integer isPublished;

    @ApiModelProperty(value = "发布时间")
    @TableField("PUBLISH_TIME")
    private Date publishTime;

    @ApiModelProperty(value = "发布人ID")
    @TableField("PUBLISH_BY")
    private String publishBy;

    @ApiModelProperty(value = "发布人姓名")
    @TableField("PUBLISH_BY_NAME")
    private String publishByName;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATOR_ID")
    private String creatorId;

    @ApiModelProperty(value = "创建人姓名")
    @TableField("CREATOR_NAME")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField("UPDATER_ID")
    private String updaterId;

    @ApiModelProperty(value = "更新人姓名")
    @TableField("UPDATER_NAME")
    private String updaterName;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "删除标记:0-未删除,1-已删除")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("COMPANY_NAME")
    private String companyName;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;
}

