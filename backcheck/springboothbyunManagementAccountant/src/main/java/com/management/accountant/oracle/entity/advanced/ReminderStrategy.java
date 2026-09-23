package com.management.accountant.oracle.entity.advanced;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 催报策略实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_REMINDER_STRATEGY")
public class ReminderStrategy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 策略ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String strategyId;

    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 策略编码
     */
    private String strategyCode;

    /**
     * 催报类型: EMAIL-邮件, SMS-短信, SYSTEM-系统通知, WECHAT-微信
     */
    private String reminderType;

    /**
     * 触发条件: OVERDUE-逾期, BEFORE_DEADLINE-截止前, PERIODIC-周期性
     */
    private String triggerCondition;

    /**
     * 催报内容
     */
    private String reminderContent;

    /**
     * 收件人配置(JSON格式)
     */
    private String recipientConfig;

    /**
     * 调度配置(JSON格式)
     */
    private String scheduleConfig;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 最后执行时间
     */
    private Date lastExecutionTime;

    /**
     * 执行次数
     */
    private Integer executionCount;

    /**
     * 备注
     */
    private String remark;

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

