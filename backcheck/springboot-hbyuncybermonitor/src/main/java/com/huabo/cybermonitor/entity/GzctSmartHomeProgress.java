package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 智慧首页-监控进度实体
 *
 * @author cybermonitor
 * @date 2026-05-28
 */
@Data
@TableName("TBL_GZCT_SMART_HOME_PROGRESS")
public class GzctSmartHomeProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_UUID)
    @TableField("ID")
    private String id;

    /** 名称 */
    @TableField("NAME")
    private String name;

    /** 完成率 */
    @TableField("RATE")
    private Integer rate;

    /** 异常数量 */
    @TableField("ABNORMAL_COUNT")
    private Integer abnormalCount;

    /** 范围 */
    @TableField("SCOPE")
    private String scope;

    /** 覆盖率 */
    @TableField("COVERAGE")
    private Integer coverage;

    /** 风险数量 */
    @TableField("RISK_COUNT")
    private Integer riskCount;

    /** 跳转路由 */
    @TableField("ROUTE")
    private String route;

    /** 排序号 */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /** 状态 */
    @TableField("STATUS")
    private String status;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
