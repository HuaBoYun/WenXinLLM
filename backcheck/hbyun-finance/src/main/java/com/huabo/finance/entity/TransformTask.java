package com.huabo.finance.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据转换任务实体类
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TRANSFORM_TASK")
@Schema(name = "TransformTask对象", description = "数据转换任务")
public class TransformTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "任务ID")
    @TableId("TASK_ID")
    private String taskId;

    @Schema(name =  "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    @Schema(name =  "关联的采集任务ID")
    @TableField("COLLECTION_TASK_ID")
    private String collectionTaskId;

    @Schema(name =  "任务状态(PENDING-待执行,RUNNING-执行中,SUCCESS-成功,FAILED-失败)")
    @TableField("STATUS")
    private String status;

    @Schema(name =  "总记录数")
    @TableField("TOTAL_RECORDS")
    private Integer totalRecords;

    @Schema(name =  "成功记录数")
    @TableField("SUCCESS_RECORDS")
    private Integer successRecords;

    @Schema(name =  "失败记录数")
    @TableField("FAILED_RECORDS")
    private Integer failedRecords;

    @Schema(name =  "开始时间")
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @Schema(name =  "结束时间")
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @Schema(name =  "耗时(毫秒)")
    @TableField("ELAPSED_TIME")
    private Long elapsedTime;

    @Schema(name =  "错误信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @Schema(name =  "备注")
    @TableField("REMARK")
    private String remark;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name =  "更新时间")
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(name =  "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name =  "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;
}

