package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 期间操作历史实体类
 *
 * @author AI Agent
 * @date 2026-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PERIOD_HISTORY")
public class PeriodHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史ID (主键)
     */
    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_UUID)
    private String historyId;

    /**
     * 期间ID
     */
    @TableField("PERIOD_ID")
    private String periodId;

    /**
     * 操作类型
     * CREATE: 创建
     * UPDATE: 更新
     * DELETE: 删除
     * OPEN: 开启
     * CLOSE: 关闭
     * LOCK: 锁定
     * UNLOCK: 解锁
     */
    @TableField("OPERATION_TYPE")
    private String operationType;

    /**
     * 操作描述
     */
    @TableField("OPERATION_DESC")
    private String operationDesc;

    /**
     * 修改前的值
     */
    @TableField("BEFORE_VALUE")
    private String beforeValue;

    /**
     * 修改后的值
     */
    @TableField("AFTER_VALUE")
    private String afterValue;

    /**
     * 操作人
     */
    @TableField("OPERATOR")
    private String operator;

    /**
     * 操作时间
     */
    @TableField("OPERATE_TIME")
    private Date operateTime;

    /**
     * IP地址
     */
    @TableField("IP_ADDRESS")
    private String ipAddress;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;
}
