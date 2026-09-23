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
 * 消息队列消息日志实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_MQ_MESSAGE")
public class BudgetMqMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "MESSAGE_ID", type = IdType.ASSIGN_UUID)
    private String messageId;

    @TableField("MQ_ID")
    private String mqId;

    @TableField("MESSAGE_TYPE")
    private String messageType;

    @TableField("PRIORITY")
    private Integer priority;

    @TableField("PAYLOAD")
    private String payload;

    @TableField("STATUS")
    private String status;

    @TableField("CREATED_TIME")
    private Date createdTime;

    @TableField("UPDATED_TIME")
    private Date updatedTime;
}
