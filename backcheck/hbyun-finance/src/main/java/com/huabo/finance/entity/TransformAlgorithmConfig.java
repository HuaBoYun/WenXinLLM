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
 * 转化算法配置实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_TRANSFORM_ALGORITHM_CONFIG")
@Schema(name = "TransformAlgorithmConfig对象", description = "转化算法配置")
public class TransformAlgorithmConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "配置ID")
    @TableId("CONFIG_ID")
    private String configId;

    @Schema(name =  "转化任务ID")
    @TableField("TRANSFORM_TASK_ID")
    private String transformTaskId;

    @Schema(name =  "算法编码(CALC_BOTTOM-计算底层科目,CALC_PARENT-计算上级科目,CALC_BALANCE-计算余额,CALC_SUMMARY-逐级汇总,CALC_AUX_BALANCE-辅助账余额)")
    @TableField("ALGORITHM_CODE")
    private String algorithmCode;

    @Schema(name =  "算法名称")
    @TableField("ALGORITHM_NAME")
    private String algorithmName;

    @Schema(name =  "算法参数(JSON格式)")
    @TableField("ALGORITHM_PARAMS")
    private String algorithmParams;

    @Schema(name =  "是否启用(1-启用,0-禁用)")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @Schema(name =  "执行顺序")
    @TableField("EXECUTE_ORDER")
    private Integer executeOrder;

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
}

