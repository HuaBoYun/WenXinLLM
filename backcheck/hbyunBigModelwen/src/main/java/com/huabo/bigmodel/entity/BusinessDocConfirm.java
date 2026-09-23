package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务梳理-签字确认记录
 */
@Data
@TableName("ai_business_doc_confirm")
@Schema(description = "业务梳理签字确认记录")
public class BusinessDocConfirm implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "所属需求ID")
    private String templateId;

    @Schema(description = "确认时的版本ID")
    private String versionId;

    @Schema(description = "确认时的版本号")
    private Integer versionNo;

    @Schema(description = "签名图base64(canvas导出)")
    private String signatureImg;

    @Schema(description = "确认人姓名,可多个逗号分隔")
    private String confirmerNames;

    @Schema(description = "确认意见")
    private String confirmOpinion;

    @Schema(description = "操作人ID")
    private String operatorId;

    @Schema(description = "操作人姓名")
    private String operatorName;

    @Schema(description = "确认时间")
    private Date confirmTime;
}
