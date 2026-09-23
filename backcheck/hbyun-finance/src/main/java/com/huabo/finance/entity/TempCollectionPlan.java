package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 临时采集方案实体类
 * 用于存储采集任务的临时配置方案
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TEMP_COLLECTION_PLAN")
@Schema(name = "TempCollectionPlan对象", description = "临时采集方案")
public class TempCollectionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "方案ID(主键)")
    @TableId("PLAN_ID")
    private String planId;

    @Schema(name =  "关联采集任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @Schema(name =  "基础方案ID(复制来源)")
    @TableField("BASE_PLAN_ID")
    private String basePlanId;

    @Schema(name =  "方案名称")
    @TableField("PLAN_NAME")
    private String planName;

    @Schema(name =  "方案配置(JSON格式,包含字段映射和过滤条件)")
    @TableField("PLAN_CONFIG")
    private String planConfig;

    @Schema(name =  "是否临时方案(1:是,0:否)")
    @TableField("IS_TEMPORARY")
    private Integer isTemporary;

    @Schema(name =  "是否保留(1:保留,0:删除)")
    @TableField("IS_KEEP")
    private Integer isKeep;

    @Schema(name =  "所属公司")
    @TableField("LINK_ORG_ID")
    private BigDecimal linkOrgId;

    @Schema(name =  "所属部门")
    @TableField("LINK_DEPT_ID")
    private BigDecimal linkDeptId;

    @Schema(name =  "创建人")
    @TableField("CREATOR")
    private BigDecimal creator;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name =  "修改人")
    @TableField("MODIFIER")
    private BigDecimal modifier;

    @Schema(name =  "修改时间")
    @TableField("MODIFY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @Schema(name =  "备注")
    @TableField("REMARK")
    private String remark;
}

