package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 三表比对主表实体
 * @author system
 * @date 2025-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("THREE_TABLE_COMPARE")
@Schema(name = "ThreeTableCompare", description = "三表比对实体")
public class ThreeTableCompare {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    @Schema(description = "主键ID")
    private String id;

    @TableField("COMPANY_NAME")
    @Schema(description = "企业名称")
    private String companyName;

    // ---- 产权登记 ----
    @TableField("PROPERTY_STATUS")
    @Schema(description = "产权登记状态")
    private String propertyStatus;

    @TableField("PROPERTY_RATIO")
    @Schema(description = "产权登记持股比例(%)")
    private BigDecimal propertyRatio;

    @TableField("PROPERTY_CAPITAL")
    @Schema(description = "产权登记注册资本(万元)")
    private BigDecimal propertyCapital;

    // ---- 工商登记 ----
    @TableField("BIZ_STATUS")
    @Schema(description = "工商登记状态")
    private String bizStatus;

    @TableField("BIZ_RATIO")
    @Schema(description = "工商登记持股比例(%)")
    private BigDecimal bizRatio;

    @TableField("BIZ_CAPITAL")
    @Schema(description = "工商登记注册资本(万元)")
    private BigDecimal bizCapital;

    // ---- 财务并表 ----
    @TableField("FINANCE_STATUS")
    @Schema(description = "财务并表状态")
    private String financeStatus;

    @TableField("FINANCE_RATIO")
    @Schema(description = "财务并表比例(%)")
    private BigDecimal financeRatio;

    // ---- 差异信息 ----
    @TableField("DIFF_STATUS")
    @Schema(description = "差异状态(NONE/DIFF/ONLY_PROPERTY/ONLY_BUSI)")
    private String diffStatus;

    @TableField("RATIO_DIFF")
    @Schema(description = "持股比例是否有差异(0否1是)")
    private Integer ratioDiff;

    @TableField("CAPITAL_DIFF")
    @Schema(description = "注册资本是否有差异(0否1是)")
    private Integer capitalDiff;

    @TableField("STATUS_DIFF")
    @Schema(description = "登记状态是否有差异(0否1是)")
    private Integer statusDiff;

    @TableField("SUGGESTION")
    @Schema(description = "处置建议")
    private String suggestion;

    @TableField("SHAREHOLDER_TYPE")
    @Schema(description = "股东类型(ENTERPRISE/INDIVIDUAL/GOVERNMENT/INSTITUTION/FUND)")
    private String shareholderType;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "更新时间")
    private Date updateTime;

    @TableField("DEL_FLAG")
    @TableLogic
    @Schema(description = "删除标志(0正常1删除)")
    private Integer delFlag;
}
