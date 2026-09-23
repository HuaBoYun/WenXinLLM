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
 * 预算消息队列集成实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_MQ_INTEGRATION")
public class BudgetMessageQueueIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息队列集成ID (主键)
     */
    @TableId(value = "MQ_ID", type = IdType.ASSIGN_UUID)
    private String mqId;

    /**
     * 消息队列编码
     */
    @TableField("MQ_CODE")
    private String mqCode;

    /**
     * 消息队列名称
     */
    @TableField("MQ_NAME")
    private String mqName;

    /**
     * 消息队列类型 (RABBITMQ/KAFKA/ROCKETMQ/ACTIVEMQ)
     */
    @TableField("MQ_TYPE")
    private String mqType;

    /**
     * 服务器地址
     */
    @TableField("SERVER_HOST")
    private String serverHost;

    /**
     * 服务器端口
     */
    @TableField("SERVER_PORT")
    private Integer serverPort;

    /**
     * 虚拟主机(RabbitMQ)
     */
    @TableField("VIRTUAL_HOST")
    private String virtualHost;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 密码(加密)
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 队列/主题名称
     */
    @TableField("QUEUE_TOPIC_NAME")
    private String queueTopicName;

    /**
     * 交换机名称(RabbitMQ)
     */
    @TableField("EXCHANGE_NAME")
    private String exchangeName;

    /**
     * 路由键(RabbitMQ)
     */
    @TableField("ROUTING_KEY")
    private String routingKey;

    /**
     * 消费者组(Kafka)
     */
    @TableField("CONSUMER_GROUP")
    private String consumerGroup;

    /**
     * 消息模式 (PRODUCER/CONSUMER/BIDIRECTIONAL)
     */
    @TableField("MESSAGE_MODE")
    private String messageMode;

    /**
     * 消息格式 (JSON/XML/TEXT)
     */
    @TableField("MESSAGE_FORMAT")
    private String messageFormat;

    /**
     * 消息模板 (JSON格式)
     */
    @TableField("MESSAGE_TEMPLATE")
    private String messageTemplate;

    /**
     * 数据映射配置 (JSON格式)
     */
    @TableField("DATA_MAPPING")
    private String dataMapping;

    /**
     * 是否持久化
     */
    @TableField("IS_PERSISTENT")
    private Boolean isPersistent;

    /**
     * 是否自动确认
     */
    @TableField("AUTO_ACK")
    private Boolean autoAck;

    /**
     * 预取数量
     */
    @TableField("PREFETCH_COUNT")
    private Integer prefetchCount;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 最后消息时间
     */
    @TableField("LAST_MESSAGE_TIME")
    private Date lastMessageTime;

    /**
     * 发送成功次数
     */
    @TableField("SEND_SUCCESS_COUNT")
    private Integer sendSuccessCount;

    /**
     * 发送失败次数
     */
    @TableField("SEND_FAILURE_COUNT")
    private Integer sendFailureCount;

    /**
     * 接收成功次数
     */
    @TableField("RECEIVE_SUCCESS_COUNT")
    private Integer receiveSuccessCount;

    /**
     * 接收失败次数
     */
    @TableField("RECEIVE_FAILURE_COUNT")
    private Integer receiveFailureCount;

    /**
     * 集成状态 (ACTIVE/INACTIVE/ERROR/TESTING)
     */
    @TableField("INTEGRATION_STATUS")
    private String integrationStatus;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人
     */
    @TableField("CREATED_BY")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField("UPDATED_TIME")
    private Date updatedTime;
}

