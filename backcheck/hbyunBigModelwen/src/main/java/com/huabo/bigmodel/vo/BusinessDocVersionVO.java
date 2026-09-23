package com.huabo.bigmodel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date; 

/**
 * 业务梳理文档版本（不含 content，用于列表）
 */
@Data
@Schema(description = "业务梳理文档版本概要")
public class BusinessDocVersionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "版本ID")
    private String id;

    @Schema(description = "所属模板ID")
    private String templateId;

    @Schema(description = "版本号")
    private Integer versionNo;

    @Schema(description = "是否最新")
    private Integer isLatest;

    @Schema(description = "变更说明")
    private String changeSummary;

    @Schema(description = "提交人ID")
    private String creatorId;

    @Schema(description = "提交人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private Date createTime;
}

