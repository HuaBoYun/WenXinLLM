package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_NBSJ_PROJECT")
@Schema(name="TblNbsjProject对象", description="")
public class TblNbsjProject implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="审计项目ID")
        @TableId(value="PROJECTID",type = IdType.INPUT)
      private BigDecimal projectid;

      @Schema(name="审计项目名称")
      @TableField("PRJOECTNAME")
    private String prjoectname;

      @Schema(name="计划年份")
      @TableField("PLANYEAR")
    private String planyear;

      @Schema(name="项目来源")
      @TableField("PROJECTSOURCE")
    private String projectsource;

      @Schema(name="开始时间")
      @TableField("STARTDATE")
    private Date startdate;

      @Schema(name="结束时间")
      @TableField("ENDDATE")
    private Date enddate;

      @Schema(name="项目经理")
      @TableField("PMID")
    private BigDecimal pmid;

      @Schema(name="审计模板")
      @TableField("TEMPID")
    private BigDecimal tempid;

      @Schema(name="费用")
      @TableField("COSTS")
    private BigDecimal costs;

      @Schema(name="审计目标和范围")
      @TableField("PURPOSE")
    private String purpose;

      @Schema(name="审计内容和重点")
      @TableField("SCOPES")
    private String scopes;

      @Schema(name="审计程序和方法")
      @TableField("PURSUANT")
    private String pursuant;

      @Schema(name="备注")
      @TableField("COMMENTS")
    private String comments;

      @Schema(name="主审人")
      @TableField("UMPIREID")
    private BigDecimal umpireid;

      @Schema(name="质控经理")
      @TableField("CONTROLID")
    private BigDecimal controlid;

      @Schema(name="审计类型")
      @TableField("AUDITTYPE")
    private String audittype;

      @Schema(name="项目编号")
      @TableField("PROJECTCODE")
    private String projectcode;

      @Schema(name="状态")
      @TableField("STATUS")
    private BigDecimal status;

      @Schema(name="创建时间")
      @TableField("CREATETIME")
    private Date createtime;

      @Schema(name="修改时间")
      @TableField("UPDATETIME")
    private Date updatetime;

      @Schema(name="选择的项目")
      @TableField("CURRENTSTATRE")
    private BigDecimal currentstatre;

      @Schema(name="创建人")
      @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;

      @Schema(name="指派项目经理时间")
      @TableField("ASSIGBEDPMTIME")
    private Date assigbedpmtime;

      @Schema(name="指派主审人时间")
      @TableField("ASSIGBEDUMPETIME")
    private Date assigbedumpetime;

      @Schema(name="指派质控经理时间")
      @TableField("ASSIGBEDCONTROLTIME")
    private Date assigbedcontroltime;

      @Schema(name="计划编号")
      @TableField("PLANID")
    private BigDecimal planid;

      @Schema(name="审计对象组织")
      @TableField("AUDITORGID")
    private BigDecimal auditorgid;

      @Schema(name="指引模板")
      @TableField("TEMPZYID")
    private BigDecimal tempzyid;

      @Schema(name="修改模板时间")
      @TableField("UPDATESTATUS")
    private BigDecimal updatestatus;

      @Schema(name="隶属组织")
      @TableField("ORGID")
    private BigDecimal orgid;

      @Schema(name="完成时间")
      @TableField("FINISHTIME")
    private Date finishtime;

      @Schema(name="实施时间")
      @TableField("IMPLEMENTTIME")
    private Date implementtime;

      @Schema(name="审批状态")
      @TableField("EXAMINETYPE")
    private Integer examinetype;

      @Schema(name="相关内容")
      @TableField("PRO_DESC")
    private String proDesc;

      @Schema(name="审计方式")
      @TableField("PRO_SJFS")
    private String proSjfs;

      @Schema(name="审计对象人")
      @TableField("AUDITSTAFFID")
    private BigDecimal auditstaffid;

    @TableField("SJLX")
    private String sjlx;

    @TableField("SJZR")
    private String sjzr;

    @TableField("YQJCQK")
    private String yqjcqk;

    @TableField("EJFHR")
    private BigDecimal ejfhr;

    @TableField("TEMPLETEID")
    private BigDecimal templeteid;

    @TableField("STAFFID")
    private BigDecimal staffid;

    @TableField("AUDITORG")
    private String auditorg;

    @TableField("FPSTATUS")
    private BigDecimal fpstatus;

    @TableField("PLANPROJECTID")
    private BigDecimal planprojectid;

    @TableField("PPROJECTNAME")
    private String pprojectname;

    @TableField("TARGETNAME")
    private String targetname;

    @TableField("ORGIDS")
    private String orgids;

    @TableField("ORGIDNAMES")
    private String orgidnames;

    @TableField("EXTERNALASSIG")
    private BigDecimal externalassig;

    @TableField("PCOUNT")
    private BigDecimal pcount;

    @TableField("PSTATUS")
    private BigDecimal pstatus;

    @TableField("FILCODE")
    private String filcode;

    @TableField("FILNAME")
    private String filname;

    @TableField("AUDITUNITNAME")
    private String auditunitname;


}
