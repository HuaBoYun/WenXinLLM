package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 监管模型需求-需求点/关键指标子表
 */
@Data
@TableName("ai_regulatory_kpi")
@Schema(description = "监管模型需求-关键指标")
public class RegulatoryKpi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "所属需求主键")
    private String requirementId;

    @Schema(description = "序号")
    private Integer seqNo;

    @Schema(description = "需求名称")
    private String reqName;

    @Schema(description = "业务域")
    private String businessDomain;

    @Schema(description = "监管目标")
    private String regGoal;

    @Schema(description = "关键指标列表(JSON数组或换行文本)")
    private String kpiList;

    @Schema(description = "预期效果")
    private String expectedEffect;

    @Schema(description = "附件列表(JSON数组:name/type/base64)")
    private String attachList;

    @Schema(description = "创建时间")
    private Date createTime;
}
