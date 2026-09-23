package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务梳理-文档版本
 */
@Data
@TableName("ai_business_doc_version")
@Schema(description = "业务梳理文档版本")
public class BusinessDocVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "所属模板ID")
    private String templateId;

    @Schema(description = "版本号(同 template 内自增)")
    private Integer versionNo;

    @Schema(description = "文档HTML内容")
    private String content;

    @Schema(description = "是否最新版本 1是 0否")
    private Integer isLatest;

    @Schema(description = "变更说明")
    private String changeSummary;

    @Schema(description = "提交人ID")
    private String creatorId;

    @Schema(description = "提交人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "编辑类型:manual/ai/restore/confirm")
    private String editType;
}

