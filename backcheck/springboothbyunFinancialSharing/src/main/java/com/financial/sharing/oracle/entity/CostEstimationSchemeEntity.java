package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 成本估算方案实体类
 * 对应表：TBL_COST_ESTIMATION_SCHEME
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_COST_ESTIMATION_SCHEME")
public class CostEstimationSchemeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 方案ID
     */
    @TableId(value = "SCHEME_ID", type = IdType.ASSIGN_ID)
    private String schemeId;

    /**
     * 方案编码
     */
    @TableField("SCHEME_CODE")
    private String schemeCode;

    /**
     * 方案名称
     */
    @TableField("SCHEME_NAME")
    private String schemeName;

    /**
     * 方案类型（PRODUCT-产品成本估算, PROJECT-项目成本估算, ACTIVITY-作业成本估算）
     */
    @TableField("SCHEME_TYPE")
    private String schemeType;

    /**
     * 估算方法（STANDARD-标准成本法, ACTUAL-实际成本法, ABC-作业成本法）
     */
    @TableField("ESTIMATION_METHOD")
    private String estimationMethod;

    /**
     * 适用范围
     */
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    /**
     * 方案描述
     */
    @TableField("SCHEME_DESCRIPTION")
    private String schemeDescription;

    /**
     * 是否启用（1-启用, 0-停用）
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private Integer version;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private Date expiryDate;

    /**
     * 账套ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

