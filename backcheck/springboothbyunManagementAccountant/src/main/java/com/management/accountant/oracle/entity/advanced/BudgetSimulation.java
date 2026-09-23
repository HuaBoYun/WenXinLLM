package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算模拟实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_SIMULATION")
public class BudgetSimulation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模拟ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String simulationId;

    /**
     * 模拟名称
     */
    private String simulationName;

    /**
     * 模拟编码
     */
    private String simulationCode;

    /**
     * 模拟类型: SCENARIO-场景模拟, SENSITIVITY-敏感性分析, MONTE_CARLO-蒙特卡洛
     */
    private String simulationType;

    /**
     * 基准预算ID
     */
    private String baseBudgetId;

    /**
     * 场景数量
     */
    private Integer scenarioCount;

    /**
     * 模拟参数(JSON格式)
     */
    private String simulationParams;

    /**
     * 模拟状态: DRAFT-草稿, IN_PROGRESS-执行中, COMPLETED-已完成, FAILED-失败
     */
    private String simulationStatus;

    /**
     * 模拟结果(JSON格式)
     */
    private String simulationResult;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志(0-未删除, 1-已删除)
     */
    private Integer delFlag;
}

