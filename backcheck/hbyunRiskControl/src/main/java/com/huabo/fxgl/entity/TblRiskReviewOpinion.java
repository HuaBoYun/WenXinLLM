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
@TableName("TBL_RISK_REVIEWOPINION")
@Schema(name="TblRiskReviewOpinion", description="风险审查意见")
@Accessors(chain = true)
@KeySequence(value = "SEQ_RISK_CTR", dbType = DbType.ORACLE)
public class TblRiskReviewOpinion extends FlexibleFieldEntity implements Serializable {


    @TableId(value = "ID",type = IdType.INPUT)
    @Schema(name = "风险审查意见主键")
    private BigDecimal id;

    @TableField("REVIEWID")
    @Schema(name = "风险审查关联ID主键")
    private BigDecimal reviewid;

    @TableField("CREATETIME")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(name = "创建时间")
    private Date createtime;

    @TableField("MATTERNAME")
    @Schema(name = "事项名称")
    private String mattername;
 
    @TableField("MATTERCONTEXT")
    @Schema(name = "文本框")
    private String mattercontext;

    @TableField("MATTERFILEIDS")
    @Schema(name = "附件id,多个逗号隔开")
    private String matterfileids;

    @TableField("STATUS")
    @Schema(name = "状态: 0-未审批，1-审批中,2-已退回 需调整，3-已撤销，6-已完成")
    private String status;
 
 
   

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
   @TableField("LINKORG")
    private BigDecimal linkorg;
   
   @Schema(name="创建人")
   @TableField("CREATESTAFFID")
   private BigDecimal createstaffid;
   


}
