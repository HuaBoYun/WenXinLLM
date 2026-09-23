package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 批量计算实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BATCH_CALCULATION")
public class BatchCalculation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计算ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String calculationId;

    /**
     * 计算名称
     */
    private String calculationName;

    /**
     * 计算编码
     */
    private String calculationCode;

    /**
     * 计算类型: FORMULA-公式计算, AGGREGATION-汇总计算, ALLOCATION-分配计算
     */
    private String calculationType;

    /**
     * 计算状态: PENDING-待执行, RUNNING-执行中, COMPLETED-已完成, FAILED-失败
     */
    private String calculationStatus;

    /**
     * 总条目数
     */
    private Integer totalItems;

    /**
     * 已完成条目数
     */
    private Integer completedItems;

    /**
     * 失败条目数
     */
    private Integer failedItems;

    /**
     * 执行时间(毫秒)
     */
    private Integer executionTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 并发数
     */
    private Integer concurrency;

    /**
     * 批次大小
     */
    private Integer batchSize;

    /**
     * 超时时间(秒)
     */
    private Integer timeout;

    /**
     * 计算公式
     */
    private String calculationFormula;

    /**
     * 任务描述
     */
    private String description;

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

