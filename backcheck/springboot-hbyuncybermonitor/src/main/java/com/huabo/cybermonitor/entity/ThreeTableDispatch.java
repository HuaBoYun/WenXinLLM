package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 三表比对差异核查派单表实体
 * @author system
 * @date 2025-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("THREE_TABLE_DISPATCH")
@Schema(name = "ThreeTableDispatch", description = "三表比对差异核查派单实体")
public class ThreeTableDispatch {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    @Schema(description = "主键ID")
    private String id;

    @TableField("COMPARE_ID")
    @Schema(description = "关联三表比对ID")
    private String compareId;

    @TableField("COMPANY_NAME")
    @Schema(description = "差异企业名称")
    private String companyName;

    @TableField("OWNER")
    @Schema(description = "核查责任人")
    private String owner;

    @TableField("DEADLINE")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "核查期限")
    private Date deadline;

    @TableField("REQUIREMENT")
    @Schema(description = "核查要求")
    private String requirement;

    @TableField("STATUS")
    @Schema(description = "派单状态(PENDING/IN_PROGRESS/COMPLETED/CANCELLED)")
    private String status;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    @TableField("DEL_FLAG")
    @TableLogic
    @Schema(description = "删除标志(0正常1删除)")
    private Integer delFlag;
}
