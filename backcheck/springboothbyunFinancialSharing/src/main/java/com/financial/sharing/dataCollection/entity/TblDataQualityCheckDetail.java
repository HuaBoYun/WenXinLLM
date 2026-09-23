package com.financial.sharing.dataCollection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据质量检查明细实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_DATA_QUALITY_CHECK_DETAIL")
public class TblDataQualityCheckDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @TableId(value = "DETAIL_ID", type = IdType.ASSIGN_ID)
    private Long detailId;

    /**
     * 检查ID
     */
    @TableField("CHECK_ID")
    private Long checkId;

    /**
     * 规则ID
     */
    @TableField("RULE_ID")
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
     * 规则类型
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 检查结果：PASS(通过)/FAIL(失败)/WARNING(警告)
     */
    @TableField("CHECK_RESULT")
    private String checkResult;

    /**
     * 检查值
     */
    @TableField("CHECK_VALUE")
    private String checkValue;

    /**
     * 阈值
     */
    @TableField("THRESHOLD_VALUE")
    private String thresholdValue;

    /**
     * 错误数量
     */
    @TableField("ERROR_COUNT")
    private Integer errorCount;

    /**
     * 错误记录（JSON格式）
     */
    @TableField("ERROR_RECORDS")
    private String errorRecords;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 改进建议
     */
    @TableField("SUGGESTION")
    private String suggestion;

    /**
     * 检查时间
     */
    @TableField("CHECK_TIME")
    private Date checkTime;

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
}

