package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 公式追踪任务实体类
 * 
 * @author AI Agent
 * @date 2026-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FORMULA_TRACE_TASK")
public class FormulaTraceTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID (主键)
     */
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 公式ID列表 (JSON格式: ["id1","id2"])
     */
    @TableField("FORMULA_IDS")
    private String formulaIds;

    /**
     * 追踪类型: forward(前向追踪)/backward(后向追踪)
     */
    @TableField("TRACE_TYPE")
    private String traceType;

    /**
     * 追踪深度 (默认5层)
     */
    @TableField("TRACE_DEPTH")
    private Integer traceDepth;

    /**
     * 状态: pending/executing/completed/failed
     */
    @TableField("STATUS")
    private String status;

    /**
     * 执行结果数据 (JSON格式)
     */
    @TableField("RESULT_DATA")
    private String resultData;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("COMPANY_NAME")
    private String companyName;

    /**
     * 创建人ID
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新人姓名
     */
    @TableField("UPDATER_NAME")
    private String updaterName;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 执行时间
     */
    @TableField("EXECUTE_TIME")
    private Date executeTime;

    /**
     * 完成时间
     */
    @TableField("COMPLETE_TIME")
    private Date completeTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

