package com.huabo.finance.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 财务软件版本字段映射实体类
 * 用于记录财务软件版本中的源表字段与目标系统表字段的映射关系
 *
 * @author 开发者
 * @date 2025-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_VERSION_FIELD_MAPPING")
@Schema(name = "VersionFieldMapping对象", description = "财务软件版本字段映射")
public class VersionFieldMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "MAPPING_ID")
    @Schema(name =  "映射ID")
    private String mappingId;

    @Schema(name =  "版本FID(关联财务软件版本)")
    private String versionFid;

    @Schema(name =  "源表名称")
    private String sourceTableName;

    @Schema(name =  "源字段名称")
    private String sourceFieldName;

    @Schema(name =  "目标表名称")
    private String targetTableName;

    @Schema(name =  "目标字段名称")
    private String targetFieldName;

    @Schema(name =  "计算逻辑(如: ROUND(BALANCE_AMOUNT, 2))")
    private String calculationLogic;

    @Schema(name =  "查询条件(如: STATUS = 1)")
    private String queryCondition;

    @Schema(name =  "字段类型(VARCHAR2, NUMBER, DATE等)")
    private String fieldType;

    @Schema(name =  "字段长度")
    private Integer fieldLength;

    @Schema(name =  "是否主键(Y/N)")
    private String isKey;

    @Schema(name =  "是否必填(Y/N)")
    private String isRequired;

    @Schema(name =  "排序顺序")
    private Integer sortOrder;

    @Schema(name =  "状态(ACTIVE/INACTIVE)")
    private String status;

    @Schema(name =  "创建时间")
    private Date createTime;

    @Schema(name =  "更新时间")
    private Date updateTime;

    @Schema(name =  "创建用户")
    private String createUser;

    @Schema(name =  "更新用户")
    private String updateUser;

    @Schema(name =  "备注")
    private String remark;
}

