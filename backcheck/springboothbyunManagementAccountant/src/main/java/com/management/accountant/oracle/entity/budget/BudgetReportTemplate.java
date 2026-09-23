package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 预算报告模板实体
 *
 * @author AI Agent
 * @date 2026-04-10
 */
@Data
@TableName("TBL_BUDGET_REPORT_TEMPLATE")
public class BudgetReportTemplate {

    /** 主键ID */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /** 模板编码 */
    private String templateCode;

    /** 模板名称 */
    private String name;

    /** 模板描述 */
    private String description;

    /** 适用报告类型(VARIANCE/TREND/COMPARISON/PERFORMANCE/FORECAST/SCENARIO/SENSITIVITY/COMPREHENSIVE) */
    private String reportType;

    /** 图标样式 */
    private String icon;

    /** 使用次数 */
    private Integer usageCount;

    /** 是否快速模板: 0-否, 1-是 */
    private Integer isQuick;

    /** 模板内容(JSON格式，定义报告结构) */
    private String templateContent;

    /** 状态: 0-禁用, 1-启用 */
    private Integer status;

    /** 排序号 */
    private Integer sortOrder;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 删除标志: 0-正常, 1-已删除 */
    private Integer delFlag;
}
