package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("TBL_RISK_REVIEW")
@Schema(name="TblRiskReview", description="风险审查")
@Accessors(chain = true)
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class TblRiskReview extends FlexibleFieldEntity implements Serializable {


    @TableId(value = "REVIEWID",type = IdType.INPUT)
    @Schema(name = "风险审查主键")
    private Integer reviewid;

    @TableField("RISKREVIEWCODE")
    @Schema(name = "风险审查报告编码")
    private String riskreviewcode;

    @TableField("STAFFID")
    @Schema(name = "经办人")
    private String staffid;

    @TableField("STAFFDEPT")
    @Schema(name = "经办部门")
    private String staffdept;
    
    @TableField("STAFFUNIT")
    @Schema(name = "经办公司")
    private String staffunit;

    @TableField("CREATETIME")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(name = "申请时间")
    private Date createtime;

    @TableField("MATTERNAME")
    @Schema(name = "事项名称")
    private String mattername;

   /* @TableField("MATTERPERSON")
    @Schema(name = "申请人")
    private String matterperson;

    @TableField("MATTERDEPT")
    @Schema(name = "申请部门")
    private String matterdept;*/

    @TableField("MATTERPROJECTCODE")
    @Schema(name = "项目编号")
    private String matterprojectcode;

    @TableField("MATTERCODE")
    @Schema(name = "三重一大编码")
    private String mattercode;

    @TableField("MATTERCONTEXT")
    @Schema(name = "文本框")
    private String mattercontext;

    @TableField("MATTERFILEIDS")
    @Schema(name = "附件id,多个逗号隔开")
    private String matterfileids;

    @TableField("STATE")
    @Schema(name = "状态: 0-未审批，1-审批中,2-已退回 需调整，3-已撤销，6-已完成")
    private String state;

    @TableField("ISAOTO")
    @Schema(name = "判断是否从事项复制，1:创建，2:复制")
    private Integer isaoto;

    @TableField("MATTERID")
    @Schema(name = "事项主键")
    private Integer matterid;

    @TableField("DECISIONMAKING")
    @Schema(name = "项目决策主体")
    private String decisionmaking;
    

    @TableField("CREATESTAFFID")
    @Schema(name = "创建人")
    private BigDecimal createstaffid;
 
    @TableField(exist = false)
    @Schema(name = "经办人名称")
    private String staffidname;

    @TableField(exist = false)
    @Schema(name = "经办部门")
    private String staffdeptname;

    @TableField(exist = false)
    @Schema(name = "经办公司")
    private String staffunitname;

    //密级及查询条件
    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围id")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉范围名称")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;
 
    @Schema(name = "所属部门")
    @TableField("LINKDEPTID")
    private BigDecimal linkdeptid;
    
    @Schema(name = "所属公司")
    @TableField("LINKORGID")
    private BigDecimal linkorgid;
    
    
    @Schema(name = "所属公司名称")
    @TableField(exist=false)
    private String orgname;



}
