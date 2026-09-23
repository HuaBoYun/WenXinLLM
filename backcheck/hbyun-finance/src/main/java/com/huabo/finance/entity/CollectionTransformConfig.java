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
 * 采集任务转化配置实体类
 * 用于存储采集任务的转化配置
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_COLLECTION_TRANSFORM_CONFIG")
@Schema(name = "CollectionTransformConfig对象", description = "采集任务转化配置")
public class CollectionTransformConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "配置ID(主键)")
    @TableId("CONFIG_ID")
    private String configId;

    @Schema(name = "采集任务ID")
    @TableField("COLLECTION_TASK_ID")
    private String collectionTaskId;

    @Schema(name = "是否自动执行(1:是,0:否)")
    @TableField("AUTO_EXECUTE")
    private Integer autoExecute;

    @Schema(name = "错误处理策略(CONTINUE/STOP/ROLLBACK)")
    @TableField("ON_ERROR")
    private String onError;

    @Schema(name = "是否验证数据(1:是,0:否)")
    @TableField("VALIDATE_DATA")
    private Integer validateData;

    @Schema(name = "执行状态(PENDING/RUNNING/SUCCESS/FAILED)")
    @TableField("EXECUTE_STATUS")
    private String executeStatus;

    @Schema(name = "开始时间")
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @Schema(name = "结束时间")
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Schema(name = "错误信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @Schema(name = "成功数量")
    @TableField("SUCCESS_COUNT")
    private Integer successCount;

    @Schema(name = "失败数量")
    @TableField("FAILED_COUNT")
    private Integer failedCount;

    @Schema(name = "所属公司")
    @TableField("LINK_ORG_ID")
    private BigDecimal linkOrgId;

    @Schema(name = "所属部门")
    @TableField("LINK_DEPT_ID")
    private BigDecimal linkDeptId;

    @Schema(name = "创建人")
    @TableField("CREATOR")
    private BigDecimal creator;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name = "修改人")
    @TableField("MODIFIER")
    private BigDecimal modifier;

    @Schema(name = "修改时间")
    @TableField("MODIFY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @Schema(name = "备注")
    @TableField("REMARK")
    private String remark;
}

