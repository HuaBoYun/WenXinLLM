package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_QUALIFICATION")
@Schema(name="TblLegalQualification对象")
public class TblLegalQualification implements Serializable {
//资质保全、资产保全
    private static final long serialVersionUID = 1L;

      @TableId(value = "QUALID",type=IdType.INPUT)
      private BigDecimal qualid;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("DISPUTEINFO")
    private BigDecimal disputeinfo;

    @TableField("APPLYPRESERVATION")//是否申请保全：applypreservation：1是，2否
    private BigDecimal applypreservation;

    @TableField("PRESERVEDAMOUNT")//（被）保全资产数额（万元）
    private BigDecimal preservedamount;

    @TableField("PRESERVEDNATURE")//保全资产性质
    private String preservednature;

    @TableField("ISPERFORMED")//是否执行扣划：isperformed：1是，2否
    private BigDecimal isperformed;

    @TableField("EXCETEAMOUNT")//（被）执行金额（万元）
    private BigDecimal exceteamount;

    @TableField("ISCANCEL")//是否接触保全：iscancel：1是，2否。
    private BigDecimal iscancel;

    @TableField("STILLFROZEN")
    private String stillfrozen;

    @TableField("PRESERVATION")
    private String preservation;

      @Schema(name = "贵安-资质保全-被保全资产名称及编号")
      @TableField("PRESERVEDNAME")
    private String preservedname;

      @Schema(name = "贵安-资质保全-执行阶段")
      @TableField("EXECUTIONPHASE")
    private String executionphase;

      @Schema(name = "贵安-资质保全-被保全资产编号")
      @TableField("PRESERVEDCODE")
    private String preservedcode;

      @Schema(name = "贵安冻结法院")
      @TableField("COURT")
    private String court;

      @Schema(name = "贵安冻结标的")
      @TableField("SUBJECTMATTER")
    private String subjectmatter;

      @Schema(name = "贵安申请人")
      @TableField("APPLICANT")
    private String applicant;

      @Schema(name = "贵安被申请人")
      @TableField("RESPONDENT")
    private String respondent;

      @Schema(name = "贵安申请金额")
      @TableField("APPLYAMOUNT")
    private BigDecimal applyamount;

      @Schema(name = "贵安实际冻结金额")
      @TableField("FROZENAMOUNT")
    private BigDecimal frozenamount;

      @Schema(name = "贵安划扣金额")
      @TableField("KOUHUAAMOUNT")
    private BigDecimal kouhuaamount;

      @Schema(name = "贵安冻结备注")
      @TableField("FROZENMEMO")
    private String frozenmemo;

      @Schema(name = "贵安实际保全资产情况")
      @TableField("ACTUALPRESERVED")
    private String actualpreserved;

     
    private String disputeitem;//隶属纠纷

     
    private BigDecimal minExceteAmount;
   
    private BigDecimal minPreservedAmount;


    //映射TblLegalDisputregistration

   
    private BigDecimal disputeid;
   
    private String disputeno;
   
    private BigDecimal disputestatus;
   
    private String disputetype;
   
    private BigDecimal contractinfo;
   
    private String disputecours;
   
    private BigDecimal isuegent;
   
    private BigDecimal whethersued;//起诉类型
   
    private BigDecimal disputeundertaker;
   
    private Date lastdealdate;
   
    private String solutionsuggestions;
   
    private String plaintiff;
   
    private String defendant;
   
    private String attorney;
   
    private String attorneyphont;
   
    private BigDecimal dispuinfo;
   
    private BigDecimal isattorney;
    @Schema(name = "贵安配合人员")
    private String coordination;
    @Schema(name = "贵安纠纷登记-案由")
    private String casecause;
    @Schema(name = "贵安纠纷登记-涉诉项目")
    private String ssproject;
    @Schema(name = "贵安纠纷登记-管辖法院（仲裁机构）")
    private String courtfirst;
    @Schema(name = "贵安纠纷登记-诉讼金额")
    private BigDecimal litigationamount;
    @Schema(name = "贵安纠纷登记-发生时间")
    private Date occurrencetime;
    @Schema(name = "贵安纠纷登记-涉及子公司")
    private String subsidiaries;
}
