package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
  @TableName("TBL_LEGAL_LITIGATIONSETTLEMENT")
@Schema(name="诉讼过程")
public class TblLegalLitigationsettlement implements Serializable {
//法务管理-诉讼过程-新建接口
  private static final long serialVersionUID = 1L;

    @TableId(value = "LITIGATIONID",type = IdType.INPUT)
    private BigDecimal litigationid;

  @TableField("FIRSTCOURT")//案件名称。必填
  private String firstcourt;

  @TableField("PRESIDINGJUDGE")//审判长
  private String presidingjudge;

  @TableField("COLLEGIALPANEL")//合议庭组成
  private String collegialpanel;

  @TableField("LITIGATIONAMOUNT")//诉讼金额
  private BigDecimal litigationamount;

  @TableField("DEALDATE")//诉讼受理日期
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date dealdate;

  @TableField("FIRSTHEARINGDATE")
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date firsthearingdate;//首次开庭日期

  @TableField("LITIGATIONENDDATE")
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date litigationenddate;//結案日期

  @TableField("LITIGATIONRESULT")//谈判结果
  private String litigationresult;

  @TableField("CREATESTAFF")
  private BigDecimal createstaff;

  @TableField("CREATETIME")
  private Date createtime;

  @TableField("LINKORG")
  private BigDecimal linkorg;

  @TableField("LITISTATUS")
  private BigDecimal litistatus;

  @TableField("ACTIONOBJECT")//诉讼标的物
  private String actionobject;

  @TableField("ISEFFECT")//判决是否生效：1是，2否
  private BigDecimal iseffect;

  @TableField("JUDGEMONEY")//判决金额
  private BigDecimal judgemoney;

  @TableField("DISPUTEINFO")
  private BigDecimal disputeinfo;
  
  
  //
  @Schema(name = "关联纠纷登记")
  @TableField("DISPUTEID")
  private BigDecimal disputeid;

	@Schema(name = "关联纠纷登记名称")
	private String disputeidname;
  
  @Schema(name = "关联仲裁")
  @TableField("ARBITRAID")//
  private BigDecimal arbitraid;
  
  @Schema(name = "填报单位")
  @TableField("FILLUNIT")//
  private String fillunit;
  
  @Schema(name = "所属集团")
  @TableField("BELONGGROUP")
  private String belonggroup;
  
  @Schema(name = "案由")
  @TableField("CAUSECASE")
  private String causecase;
  
  @Schema(name = "纠纷承办")
  @TableField("DISPUTEHAND")
  private String disputehand;
  
  @Schema(name = "纠纷名称")
  @TableField("DISPUTENAME")
  private String disputename;
  
  @Schema(name = "纠纷类型")
  @TableField("DISPUTETYPE")
  private String disputetype;
  
  @Schema(name = "律师事务所")
  @TableField("LAWFIRM")
  private String lawfirm;
  
  @Schema(name = "是否外聘律师")
  @TableField("ISATTORNEY")
  private BigDecimal isattorney;
  
  @Schema(name = "律师名称")
  @TableField("LAWNAME")
  private String lawname;
  
  @Schema(name = "外聘律师类型")
  @TableField("LAWTYPE")
  private String lawtype;
  
  @Schema(name = "关联合同")
  @TableField("ISLINKCONTRACT")
  private BigDecimal islinkcontract;
  
  @Schema(name = "纠纷承办人")
  @TableField("DISPUTEUNDER")
  private BigDecimal disputeunder;

	
	@Schema(name="纠纷承办人名称")
	private String disputeundername;

  @Schema(name = "诉讼地位")
  @TableField("LITIGIOUSSTATUS")
  private String litigiousstatus;
  
  @Schema(name = "我方涉诉企业")
  @TableField("OURDISPUTEORG")
  private String ourdisputeorg;
  
  @Schema(name = "对方当事人")
  @TableField("OPPOSITEPARTY")
  private String oppositeparty;
  
  @Schema(name = "标的额（万元）")
  @TableField("SUBJECTAMOUNT")
  private String subjectamount;
  
  @Schema(name = "管辖机构")
  @TableField("JURISDICTION")
  private String jurisdiction;
  
  @Schema(name = "纠纷状态")
  @TableField("DISPUTESTATUS")
  private BigDecimal disputestatus;
  
  @Schema(name = "业务发生时间")
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @TableField("BIZTIME")
  private Date biztime;
  
  @Schema(name = "立案时间")
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @TableField("CASETIME")
  private Date casetime;
  
  @Schema(name = "是否涉及刑事案件")
  @TableField("ISCRIMINALCASES")
  private BigDecimal iscriminalcases;
  
  @Schema(name = "对方当事人是否具备偿还能力")
  @TableField("ISREPAYMENT")
  private BigDecimal isrepayment;
  
  @Schema(name = "是否有担保或保全措施")
  @TableField("ISPRESERVATION")
  private BigDecimal ispreservation;
  
  @Schema(name = "执行状态")
  @TableField("EXECSTATUS")
  private BigDecimal execstatus;
  
  @Schema(name = "是否为紧急事项")
  @TableField("ISURGENT")
  private BigDecimal isurgent;
  
  @Schema(name = "紧急情况说明")
  @TableField("URGENTMEMO")
  private String urgentmemo;
  
  @Schema(name = "案件的基本情况")
  @TableField("CASEBASICINFO")
  private String casebasicinfo;
  
  @Schema(name = "企业采取的处理措施或诉讼思路")
  @TableField("TAKEMEASURES")
  private String takemeasures;
  
  @Schema(name = "判决或仲裁结果")
  @TableField("JUDGMENTRESULT")
  private String judgmentresult;
  
  
  @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
  private List<TblAttachment> attList;

  @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
  private TblLegalDisputregistration dispute;

//  
//  private String disputeitem;//隶属纠纷

  @TableField(exist=false)
  private String startdate;
  @TableField(exist=false)
  private String enddate;

  //映射TblLegalProceedingsrecord

  @TableField(exist=false)
  private BigDecimal proceedid;
  //案号
  @TableField(exist=false)
  private String proceedno;
  //诉讼阶段
  @TableField(exist=false)
  private String porceedstage;
  //审理法院
  @TableField(exist=false)
  private String court;
  @TableField(exist=false)
  private String courtlink;
  @TableField(exist=false)
  private String courtcontact;
  
  @JSONField(format = "yyyy-MM-dd")
  @TableField(exist=false)
  private Date filingtime;
  
  @JSONField(format = "yyyy-MM-dd")
  @TableField(exist=false)
  private Date paymentremindtime;
  
  @JSONField(format = "yyyy-MM-dd")
  @TableField(exist=false)
  private Date openingtime;
  
  @JSONField(format = "yyyy-MM-dd")
  @TableField(exist=false)
  private Date judgetiem;
  @TableField(exist=false)
  private BigDecimal isexternallawyer;
  @TableField(exist=false)
  private String lawyearword;
  @TableField(exist=false)
  private String lawyearname;
  @TableField(exist=false)
  private String lawyearlink;
  @TableField(exist=false)
  private BigDecimal litigationinfo;
  @TableField(exist=false)
  private String negotiator;
  @TableField(exist=false)
  private String negotiatorlink;
  @TableField(exist=false)
  private String presedingjudge;
  @TableField(exist=false)
  private String casepromotion;
  @TableField(exist=false)
  private String existingdifficulties;
  @TableField(exist=false)
  private String measurespromote;


  //TblLegalDisputregistration
//  
//  private Integer disputeid;
  @TableField(exist=false)
  private String disputeno;//登记编号
//  
//  private BigDecimal disputestatus;
//  //纠纷类型
//  private String disputetype;
  @TableField(exist=false)
  private BigDecimal contractinfo;
  //争议焦点
  @TableField(exist=false)
  private String disputecours;
  //是否紧急：1是，2否
  @TableField(exist=false)
  private BigDecimal isuegent;
  //起诉类型：1起诉，2被诉
  @TableField(exist=false)
  private BigDecimal whethersued;
  @TableField(exist=false)
  private BigDecimal disputeundertaker;
  //办结时间
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @TableField(exist=false)
  private Date lastdealdate;
  @TableField(exist=false)
  private String solutionsuggestions;//初步解决建议
  //原告
  @TableField(exist=false)
  private String plaintiff;
  //被告
  @TableField(exist=false)
  private String defendant;
  //代理律师
  @TableField(exist=false)
  private String attorney;
  //代理律师联系电话
  @TableField(exist=false)
  private String attorneyphont;

  @TableField(exist=false)
  private BigDecimal dispuinfo;
  @TableField(exist=false)
  private String disputeitem;//纠纷主题
//  //是否聘请律师：1是，2否
//  private BigDecimal isattorney;

  @Schema(name = "贵安配合人员")
  @TableField(exist=false)
  private String coordination;

  @Schema(name = "贵安纠纷登记-案由")
  @TableField(exist=false)
  private String casecause;

  @Schema(name = "贵安纠纷登记-涉诉项目")
  @TableField(exist=false)
  private String ssproject;

  @Schema(name = "贵安纠纷登记-管辖法院（仲裁机构）")
  @TableField(exist=false)
  private String courtfirst;



  @Schema(name = "贵安纠纷登记-发生时间")
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @TableField(exist=false)
  private Date occurrencetime;

  @Schema(name = "贵安纠纷登记-涉及子公司")
  @TableField(exist=false)
  private String subsidiaries;

}
