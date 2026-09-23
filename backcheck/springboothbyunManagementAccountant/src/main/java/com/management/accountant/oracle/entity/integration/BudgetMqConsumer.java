package com.management.accountant.oracle.entity.integration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息队列消费者实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_MQ_CONSUMER")
public class BudgetMqConsumer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "CONSUMER_ID", type = IdType.ASSIGN_UUID)
    private String consumerId;

    @TableField("MQ_ID")
    private String mqId;

    @TableField("CONSUMER_NAME")
    private String consumerName;

    @TableField("CONSUMER_TYPE")
    private String consumerType;

    @TableField("PROCESSED_COUNT")
    private Integer processedCount;

    @TableField("ERROR_COUNT")
    private Integer errorCount;

    @TableField("LAST_ACTIVITY")
    private Date lastActivity;

    @TableField("STATUS")
    private String status;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
