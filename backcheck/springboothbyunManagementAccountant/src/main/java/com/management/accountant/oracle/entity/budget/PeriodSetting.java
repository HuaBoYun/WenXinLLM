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
 * 期间设置实体类
 *
 * @author AI Agent
 * @date 2026-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PERIOD_SETTING")
public class PeriodSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设置ID (主键)
     */
    @TableId(value = "SETTING_ID", type = IdType.ASSIGN_UUID)
    private String settingId;

    /**
     * 默认期间类型
     */
    @TableField("DEFAULT_PERIOD_TYPE")
    private String defaultPeriodType;

    /**
     * 期间命名规则
     */
    @TableField("NAMING_RULE")
    private String namingRule;

    /**
     * 自动开启下个期间
     */
    @TableField("AUTO_OPEN_NEXT")
    private Boolean autoOpenNext;

    /**
     * 期间结束日期类型
     */
    @TableField("PERIOD_END_DATE")
    private String periodEndDate;

    /**
     * 固定日期
     */
    @TableField("FIXED_DAY")
    private Integer fixedDay;

    /**
     * 允许跨期间调整
     */
    @TableField("ALLOW_CROSS_PERIOD")
    private Boolean allowCrossPeriod;

    /**
     * 期间锁定规则
     */
    @TableField("LOCK_RULE")
    private String lockRule;

    /**
     * 关闭后允许编辑
     */
    @TableField("ALLOW_EDIT_AFTER_CLOSE")
    private Boolean allowEditAfterClose;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

}
