package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据录入查询VO
 * 
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="DataEntryQueryVo", description="数据录入查询VO")
public class DataEntryQueryVo extends BaseVo {

    @Schema(name = "企业ID")
    private String enterpriseId;

    @Schema(name = "企业名称")
    private String enterpriseName;

    @Schema(name = "数据类型")
    private String dataType;

    @Schema(name = "数据标题")
    private String dataTitle;

    @Schema(name = "数据来源")
    private String dataSource;

    @Schema(name = "录入人")
    private String entryPerson;

    @Schema(name = "录入状态")
    private String entryStatus;

    @Schema(name = "质量等级")
    private String qualityLevel;

    @Schema(name = "验证状态")
    private String validationStatus;

    @Schema(name = "录入时间开始")
    private String entryTimeStart;

    @Schema(name = "录入时间结束")
    private String entryTimeEnd;

    @Schema(name = "排序字段")
    private String orderBy;

    @Schema(name = "排序方向")
    private String orderDirection;
}
