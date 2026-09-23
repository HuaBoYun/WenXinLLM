package com.huabo.audit.oracle.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 审计情况统计表-子类
 * 20230804
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AUDIT_SITUATION_SUBCLASS")
@Schema(name="审计情况统计表-子表", description="TBL_AUDIT_SITUATION_SUBCLASS")
@KeySequence(value = "HIBERNATE_SEQUENCE") //value为数据库中生成的序列名，class指主键属性类型
public class TblAuditSituationSubclassEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(type = IdType.INPUT)  //注意主键类型要指定为Input
    private Integer id;

    @Schema(name = "项目")
    @TableField("SUBPROJECT")
    private String subProject;

    @Schema(name = "合计")
    @TableField("SUBAMOUNTTO")
    private String subAmountTo;

    @Schema(name = "其中集团部总")
    @TableField("SUBMANAGER")
    private String subManager;

    @Schema(name = "其中重要二级子公司")
    @TableField("SUBSECONDARYCOMPANY")
    private String subSecondaryCompany;

    @Schema(name = "外键")
    @TableField("SITUATIONID")
    private String situationId;


}
