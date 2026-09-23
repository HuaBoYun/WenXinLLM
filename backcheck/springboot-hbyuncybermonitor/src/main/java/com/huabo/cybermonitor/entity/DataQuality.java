package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据质量管理实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DATA_QUALITY")
@Schema(name="DataQuality", description="数据质量管理实体")
public class DataQuality {

    @Schema(name = "数据质量ID")
    @TableId(value = "QUALITY_ID", type = IdType.ASSIGN_UUID)
    private String qualityId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "数据录入ID")
    @TableField("DATA_ENTRY_ID")
    private String dataEntryId;

    @Schema(name = "检查类型")
    @TableField("CHECK_TYPE")
    private String checkType;

    @Schema(name = "检查名称")
    @TableField("CHECK_NAME")
    private String checkName;

    @Schema(name = "检查描述")
    @TableField("CHECK_DESCRIPTION")
    private String checkDescription;

    @Schema(name = "检查规则")
    @TableField("CHECK_RULES")
    private String checkRules;

    @Schema(name = "检查时间")
    @TableField("CHECK_TIME")
    private LocalDateTime checkTime;

    @Schema(name = "检查人")
    @TableField("CHECK_PERSON")
    private String checkPerson;

    @Schema(name = "检查状态")
    @TableField("CHECK_STATUS")
    private String checkStatus;

    @Schema(name = "检查结果")
    @TableField("CHECK_RESULT")
    private String checkResult;

    @Schema(name = "质量评分")
    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @Schema(name = "质量等级")
    @TableField("QUALITY_LEVEL")
    private String qualityLevel;

    @Schema(name = "完整性评分")
    @TableField("COMPLETENESS_SCORE")
    private BigDecimal completenessScore;

    @Schema(name = "准确性评分")
    @TableField("ACCURACY_SCORE")
    private BigDecimal accuracyScore;

    @Schema(name = "一致性评分")
    @TableField("CONSISTENCY_SCORE")
    private BigDecimal consistencyScore;

    @Schema(name = "及时性评分")
    @TableField("TIMELINESS_SCORE")
    private BigDecimal timelinessScore;

    @Schema(name = "有效性评分")
    @TableField("VALIDITY_SCORE")
    private BigDecimal validityScore;

    @Schema(name = "唯一性评分")
    @TableField("UNIQUENESS_SCORE")
    private BigDecimal uniquenessScore;

    @Schema(name = "问题数量")
    @TableField("ISSUE_COUNT")
    private Integer issueCount;

    @Schema(name = "问题详情")
    @TableField("ISSUE_DETAILS")
    private String issueDetails;

    @Schema(name = "问题等级")
    @TableField("ISSUE_LEVEL")
    private String issueLevel;

    @Schema(name = "修复建议")
    @TableField("FIX_SUGGESTIONS")
    private String fixSuggestions;

    @Schema(name = "修复状态")
    @TableField("FIX_STATUS")
    private String fixStatus;

    @Schema(name = "修复人")
    @TableField("FIX_PERSON")
    private String fixPerson;

    @Schema(name = "修复时间")
    @TableField("FIX_TIME")
    private LocalDateTime fixTime;

    @Schema(name = "修复结果")
    @TableField("FIX_RESULT")
    private String fixResult;

    @Schema(name = "验证状态")
    @TableField("VALIDATION_STATUS")
    private String validationStatus;

    @Schema(name = "验证人")
    @TableField("VALIDATION_PERSON")
    private String validationPerson;

    @Schema(name = "验证时间")
    @TableField("VALIDATION_TIME")
    private LocalDateTime validationTime;

    @Schema(name = "验证结果")
    @TableField("VALIDATION_RESULT")
    private String validationResult;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "创建人")
    @TableField("CREATE_BY")
    private String createBy;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_BY")
    private String updateBy;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志")
    @TableField("DEL_FLAG")
    private String delFlag;
}
