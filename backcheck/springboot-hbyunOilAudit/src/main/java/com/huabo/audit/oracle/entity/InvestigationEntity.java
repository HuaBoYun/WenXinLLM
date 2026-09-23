package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.ReservedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName InvestigationEntity
 * @Description
 * @DATE 2023/10/01
 */

@Data
@TableName("TBL_YQNS_INVESTIGATION")
@Schema(name="调查表")
@Accessors(chain = true)
public class InvestigationEntity extends ReservedEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="PROJECT_NAME")
    @Schema(name="项目名称")
    private String projectName;

    @TableField(value="PLAN_NO")
    @Schema(name="计划文号")
    private String planNo;

    @TableField(value="GP_NO")
    @Schema(name="概批编号")
    private String gpNo;

    @TableField(value="CONTRACT_NO")
    @Schema(name="合同编号")
    private String contractNo;

    @TableField(value="SETTLEMENT_AMOUNT")
    @Schema(name="结算金额")
    private BigDecimal settlementAmount;

    @TableField(value="MATERIAL_AMOUNT")
    @Schema(name="甲供物资金额")
    private BigDecimal materialAmount;

    @TableField(value="CONTRACT_START_TIME")
    @Schema(name="合同开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date contractStartTime;

    @TableField(value="CONTRACT_END_TIME")
    @Schema(name="合同竣工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date contractEndTime;

    @TableField(value="WORK_START_TIME")
    @Schema(name="实际开工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workStartTime;

    @TableField(value="WORK_END_TIME")
    @Schema(name="实际竣工时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workEndTime;

    @TableField(value="CONSTRUCTION_CONTENT")
    @Schema(name="主要施工内容")
    private String constructionContent;

    @TableField(value="SETTLEMENT_PROGRESS")
    @Schema(name="结算进度")
    private String settlementProgress;

    @TableField(value="PROJECT_ADDRESS")
    @Schema(name="项目实施地点")
    private String projectAddress;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private String attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;
    

}
