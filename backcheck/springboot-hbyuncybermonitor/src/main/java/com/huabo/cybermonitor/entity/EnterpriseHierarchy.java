package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业层级关系表
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ENTERPRISE_HIERARCHY")
public class EnterpriseHierarchy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 层级关系ID
     */
    @TableId(value = "HIERARCHY_ID", type = IdType.ASSIGN_UUID)
    private String hierarchyId;

    /**
     * 母公司ID
     */
    @TableField("PARENT_ENTERPRISE_ID")
    private String parentEnterpriseId;

    /**
     * 子公司ID
     */
    @TableField("CHILD_ENTERPRISE_ID")
    private String childEnterpriseId;

    /**
     * 层级级别
     */
    @TableField("HIERARCHY_LEVEL")
    private Integer hierarchyLevel;

    /**
     * 关系类型：SUBSIDIARY-子公司，BRANCH-分公司，HOLDING-控股公司，PARTICIPATING-参股公司
     */
    @TableField("RELATIONSHIP_TYPE")
    private String relationshipType;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 非数据库字段 - 用于显示
    @TableField(exist = false)
    private String parentEnterpriseName;

    @TableField(exist = false)
    private String childEnterpriseName;

    // 关系类型常量
    public static final String TYPE_SUBSIDIARY = "SUBSIDIARY";
    public static final String TYPE_BRANCH = "BRANCH";
    public static final String TYPE_HOLDING = "HOLDING";
    public static final String TYPE_PARTICIPATING = "PARTICIPATING";
}
