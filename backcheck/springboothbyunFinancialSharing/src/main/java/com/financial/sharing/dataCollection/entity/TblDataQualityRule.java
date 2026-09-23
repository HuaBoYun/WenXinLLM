package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据质量规则实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_DATA_QUALITY_RULE")
public class TblDataQualityRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private Long ruleId;

    /**
     * 规则编码
     */
    @TableField("RULE_CODE")
    private String ruleCode;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 规则类型：COMPLETENESS(完整性)/ACCURACY(准确性)/CONSISTENCY(一致性)/TIMELINESS(及时性)/VALIDITY(有效性)
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 检查级别：ERROR(错误)/WARNING(警告)/INFO(提示)
     */
    @TableField("CHECK_LEVEL")
    private String checkLevel;

    /**
     * 检查SQL语句
     */
    @TableField("CHECK_SQL")
    private String checkSql;

    /**
     * 检查条件（JSON格式）
     */
    @TableField("CHECK_CONDITION")
    private String checkCondition;

    /**
     * 阈值
     */
    @TableField("THRESHOLD_VALUE")
    private String thresholdValue;

    /**
     * 错误提示信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 改进建议
     */
    @TableField("SUGGESTION")
    private String suggestion;

    /**
     * 是否启用：Y/N
     */
    @TableField("IS_ENABLED")
    private String isEnabled;

    /**
     * 排序号
     */
    @TableField("SORT_NO")
    private Integer sortNo;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

