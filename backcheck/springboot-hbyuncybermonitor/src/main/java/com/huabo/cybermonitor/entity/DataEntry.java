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
 * 数据录入实体类
 *
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DATA_ENTRY")
@Schema(name="DataEntry", description="数据录入实体")
public class DataEntry {

    @Schema(name = "数据录入ID")
    @TableId(value = "DATA_ENTRY_ID", type = IdType.ASSIGN_UUID)
    private String dataEntryId;

    @Schema(name = "企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name = "数据类型")
    @TableField("DATA_TYPE")
    private String dataType;

    @Schema(name = "数据标题")
    @TableField("DATA_TITLE")
    private String dataTitle;

    @Schema(name = "数据描述")
    @TableField("DATA_DESCRIPTION")
    private String dataDescription;

    @Schema(name = "数据来源")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @Schema(name = "数据内容")
    @TableField("DATA_CONTENT")
    private String dataContent;

    @Schema(name = "数据格式")
    @TableField("DATA_FORMAT")
    private String dataFormat;

    @Schema(name = "数据大小")
    @TableField("DATA_SIZE")
    private Integer dataSize;

    @Schema(name = "数据单位")
    @TableField("DATA_UNIT")
    private String dataUnit;

    @Schema(name = "录入人")
    @TableField("ENTRY_PERSON")
    private String entryPerson;

    @Schema(name = "录入时间")
    @TableField("ENTRY_TIME")
    private LocalDateTime entryTime;

    @Schema(name = "录入状态")
    @TableField("ENTRY_STATUS")
    private String entryStatus;

    @Schema(name = "审核人")
    @TableField("AUDIT_PERSON")
    private String auditPerson;

    @Schema(name = "审核时间")
    @TableField("AUDIT_TIME")
    private LocalDateTime auditTime;

    @Schema(name = "审核意见")
    @TableField("AUDIT_OPINION")
    private String auditOpinion;

    @Schema(name = "质量评分")
    @TableField("QUALITY_SCORE")
    private BigDecimal qualityScore;

    @Schema(name = "质量等级")
    @TableField("QUALITY_LEVEL")
    private String qualityLevel;

    @Schema(name = "验证状态")
    @TableField("VALIDATION_STATUS")
    private String validationStatus;

    @Schema(name = "验证结果")
    @TableField("VALIDATION_RESULT")
    private String validationResult;

    @Schema(name = "错误信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    @Schema(name = "数据版本")
    @TableField("DATA_VERSION")
    private String dataVersion;

    @Schema(name = "是否最新版本")
    @TableField("IS_LATEST")
    private String isLatest;

    @Schema(name = "父数据ID")
    @TableField("PARENT_DATA_ID")
    private String parentDataId;

    @Schema(name = "关联数据ID")
    @TableField("RELATED_DATA_ID")
    private String relatedDataId;

    @Schema(name = "标签")
    @TableField("TAGS")
    private String tags;

    @Schema(name = "备注")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "扩展字段1")
    @TableField("EXT_FIELD1")
    private String extField1;

    @Schema(name = "扩展字段2")
    @TableField("EXT_FIELD2")
    private String extField2;

    @Schema(name = "扩展字段3")
    @TableField("EXT_FIELD3")
    private String extField3;

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
