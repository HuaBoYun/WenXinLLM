package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 成本模拟实体类
 * 对应表：TBL_COST_SIMULATION
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_COST_SIMULATION")
public class CostSimulationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模拟ID
     */
    @TableId(value = "SIMULATION_ID", type = IdType.ASSIGN_ID)
    private String simulationId;

    /**
     * 模拟编号
     */
    @TableField("SIMULATION_NO")
    private String simulationNo;

    /**
     * 模拟名称
     */
    @TableField("SIMULATION_NAME")
    private String simulationName;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 方案ID
     */
    @TableField("SCHEME_ID")
    private String schemeId;

    /**
     * 模拟场景（BEST-最优场景, NORMAL-正常场景, WORST-最差场景）
     */
    @TableField("SIMULATION_SCENARIO")
    private String simulationScenario;

    /**
     * 输入参数（JSON格式）
     */
    @TableField("INPUT_PARAMETERS")
    private String inputParameters;

    /**
     * 模拟结果（JSON格式）
     */
    @TableField("SIMULATION_RESULT")
    private String simulationResult;

    /**
     * 预计成本
     */
    @TableField("ESTIMATED_COST")
    private BigDecimal estimatedCost;

    /**
     * 置信度（0-100）
     */
    @TableField("CONFIDENCE_LEVEL")
    private Integer confidenceLevel;

    /**
     * 模拟状态（0-待执行, 1-执行中, 2-已完成, 3-失败）
     */
    @TableField("SIMULATION_STATUS")
    private Integer simulationStatus;

    /**
     * 执行时间
     */
    @TableField("EXECUTION_TIME")
    private Date executionTime;

    /**
     * 账套ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

