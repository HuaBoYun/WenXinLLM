package com.huabo.contract.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
  @TableName("TBL_LEGAL_DISPUTREGISTRATION")
@Schema(name="TblLegalDisputregistration对象")
public class TblLegalDisputregistration implements Serializable {
//隶属纠纷
  private static final long serialVersionUID = 1L;

    @TableId(value = "DISPUTEID",type=IdType.INPUT)
    @Schema(name = "主键Id 自增")
    private BigDecimal disputeid;

  @TableField("DISPUTENO")
  private String disputeno;//登记编号

  @TableField("DISPUTESTATUS")//状态状态0-未审批、1-审批中、2-需调整、3-已通过、"  "4-已终止、5-已跟踪、6-已完成
  private Integer disputestatus;

  @TableField("DISPUTETYPE")//纠纷类型
  private String disputetype;

  @TableField("CONTRACTINFO")
  private BigDecimal contractinfo;

  @TableField("DISPUTECOURS")//争议焦点
  private String disputecours;

  @TableField("ISUEGENT")//是否紧急：1是，2否
  private BigDecimal isuegent;

  @TableField("WHETHERSUED")//起诉类型：1起诉，2被诉//在详细信息为诉讼定位
  private BigDecimal whethersued;
  
  @TableField("LITIGATIONPOS")//诉讼定位
  private String litigationpos;

  @Schema(name="公司经办人ID")
  @TableField("DISPUTEUNDERTAKER")
  private BigDecimal disputeundertaker;

  @Schema(name="公司经办人名称")
  private String disputeundertakername;

  @TableField("LASTDEALDATE")//办结时间
  @JSONField(format = "yyyy-MM-dd")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date lastdealdate;

  @TableField("SOLUTIONSUGGESTIONS")
  private String solutionsuggestions;//初步解决建议
  
  
  @TableField("URGENTMEMO")
  private String urgentmemo;//紧急事项情况说明
  
  @TableField("LEGALEXAM")
  private String legalexam;//法务部审核意见
  
  @TableField("COUNSELEXAM")
  private String counselexam;//总法律顾问审核意见
  
  @TableField("CHAIRMANEXAM")
  private String chairmanexam;//董事长审核意见
  
  @TableField("GMANEXAM")
  private String gmanexam;//总经理审核意见
  

  @TableField("PLAINTIFF")//原告
  private String plaintiff;

  @TableField("DEFENDANT")//被告
  private String defendant;

  @TableField("ATTORNEY")//代理律师
  private String attorney;

  @TableField("ATTORNEYPHONT")//代理律师联系电话
  private String attorneyphont;

  @TableField("LINKORG")
  private BigDecimal linkorg;

  @TableField("DISPUINFO")
  private BigDecimal dispuinfo;

  @TableField("CREATESTAFF")
  private BigDecimal createstaff;

  @TableField("CREATETIME")
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @JSONField(format = "yyyy-MM-dd")
  private Date createtime;//查询中的受理日期

  @TableField("DISPUTEITEM")
  private String disputeitem;//纠纷主题(名称)

  @TableField("ISATTORNEY")//是否聘请律师：1是，2否
  private BigDecimal isattorney;

    @Schema(name = "贵安配合人员")
    @TableField("COORDINATION")
  private String coordination;

    @Schema(name = "贵安纠纷登记-案由")
    @TableField("CASECAUSE")
  private String casecause;

    @Schema(name = "贵安纠纷登记-涉诉项目")
    @TableField("SSPROJECT")
  private String ssproject;

    @Schema(name = "贵安纠纷登记-管辖法院（仲裁机构）")
    @TableField("COURTFIRST")
  private String courtfirst;

    @Schema(name = "贵安纠纷登记-诉讼金额")
    @TableField("LITIGATIONAMOUNT")
  private BigDecimal litigationamount;

    @Schema(name = "贵安纠纷登记-发生时间")
    @TableField("OCCURRENCETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
  private Date occurrencetime;

    @Schema(name = "贵安纠纷登记-涉及子公司")
    @TableField("SUBSIDIARIES")
  private String subsidiaries;

	@Schema(name = "业务发生时间 格式：yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	@TableField("BUSINESSDATE")
	private Date businessdate;
    
    @TableField("ISCLASSICCASE")//是否典型案例库
    private BigDecimal isclassiccase;
    
   //纠纷承办人TblStaff-username
    @TableField(exist = false)
    private String zxstaffname;

  //纠纷承办人TblStaff
    @TableField(exist = false)
  private String username;

  //合同执行人呢
    @TableField(exist = false)
  private String realname;

//  
//  private String contractName;
  @TableField(exist = false,select = false,fill = FieldFill.DEFAULT)
  private TblCyhwUnit contract;
  private Integer uniqueResult;
  @TableField(exist = false,select = false,fill = FieldFill.DEFAULT)
  private TblStaff disputeUnder;
  @TableField(exist = false,select = false,fill = FieldFill.DEFAULT)
  private List<TblAttachment> attList;

  //映射TblCyhwUnit

  @TableField(exist = false)
  private BigDecimal contractid;

  @Schema(name = "隶属流程id 关联Tbl_flow flowid")
  @TableField(exist = false)
  private BigDecimal flowid;

  @Schema(name = "单位名称")
  @TableField(exist = false)
  private String unitname;

  @Schema(name = "合同名称")
  @TableField(exist = false)
  private String contractname;

  @Schema(name = "合同编号")
  @TableField(exist = false)
  private String contractno;

  @Schema(name = "承办部门")
  @TableField(exist = false)
  private BigDecimal contractdept;

  @Schema(name = "合同对方")
  @TableField(exist = false)
  private String contractlink;

  @Schema(name = "合同金额")
  @TableField(exist = false)
  private BigDecimal contractmoney;

  @Schema(name = "合同状态 1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、7-执行中、		8-已归档、		9-已暂停、		10-已变更、		11-已终止、		12-纠纷中、		13-协商中、		14-诉讼中、		15-仲裁中、		16-已结案")
  @TableField(exist = false)
  private Integer contractstatus;

  @Schema(name = "关联科室")
  @TableField(exist = false)
  private String linkdept;

  @Schema(name = "隶属组织")
  @TableField(exist = false)
  private BigDecimal orgid;

  @Schema(name = "创建人")
  @TableField(exist = false)
  private BigDecimal createuser;

  @Schema(name = "合同简要说明")
  @TableField(exist = false)
  private String momoconcat;

  @Schema(name = "关联的风险控制")
  @TableField(exist = false)
  private String riskcontrol;

  @Schema(name = "合同内容")
  @TableField(exist = false)
  private String describe;

  @Schema(name = "承办人")
  @TableField(exist = false)
  private BigDecimal contractstaff;

  @Schema(name = "保密等级")
  @TableField(exist = false)
  private String scrilevel;

  @Schema(name = "合同开始时间")
  @TableField(exist = false)
  @JSONField(format = "yyyy-MM-dd")
  private Date startdate;

  @Schema(name = "合同结束时间")
  @TableField(exist = false)
  @JSONField(format = "yyyy-MM-dd")
  private Date enddate;

  @Schema(name = "合同项目")
  @TableField(exist = false)
  private String contractitem;

  @Schema(name = "合同类型")
  @TableField(exist = false)
  private String contracttype;

  @Schema(name = "合同期限类型")
  @TableField(exist = false)
  private String contractdatetype;

  @Schema(name = "合同性质")
  @TableField(exist = false)
  private String contractxz;

  @Schema(name = "合同相对方主键ID")
  @TableField(exist = false)
  private BigDecimal contractxdfxinfo;

  @Schema(name = "合同类型 HTGL001 - 相对方、HTGL002-合同订立、HTGL003-合同用印、HTGL004-合同履行、HTGL005-变更、HTGL006-合同归档、HTGL007-合同范本")
  @TableField(exist = false)
  private String recordtype;

  @Schema(name = "隶属合同 cyhwunitId")
  @TableField(exist = false)
  private BigDecimal recordparent;

  @Schema(name = "合同金额汉字描述")
  @TableField(exist = false)
  private String hzsumowing;

  @Schema(name = "计价方式")
  @TableField(exist = false)
  private String jijiatype;

  @Schema(name = "币种")
  @TableField(exist = false)
  private String moneytype;

  @Schema(name = "首付方向")
  @TableField(exist = false)
  private String dctype;

  @Schema(name = "合同标的")
  @TableField(exist = false)
  private String contractbd;

  @Schema(name = "是否重大合同")
  @TableField(exist = false)
  private String contractzd;

  @Schema(name = "经办人")
  @TableField(exist = false)
  private BigDecimal jbstaff;

  @Schema(name = "经办部门")
  @TableField(exist = false)
  private BigDecimal jbdept;

  @Schema(name = "经办公司")
  @TableField(exist = false)
  private BigDecimal jbunit;

  @Schema(name = "执行公司")
  @TableField(exist = false)
  private BigDecimal zxunit;

  @Schema(name = "是否关联主合同")
  @TableField(exist = false)
  private String contractchildren;

  @Schema(name = "是否有履行计划")
  @TableField(exist = false)
  private String contractplan;

  @Schema(name = "合同立项信息")
  @TableField(exist = false)
  private String topicid;

  @Schema(name = "合同立项名称")
  @TableField(exist = false)
  private String topicname;

  @Schema(name = "历史状态")
  @TableField(exist = false)
  private BigDecimal hiscontractstatus;

  @Schema(name = "变更类型")
  @TableField(exist = false)
  private String changetype;

  @Schema(name = "变更时间")
  @TableField(exist = false)
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private LocalDateTime changedate;

  @Schema(name = "变更内容")
  @TableField(exist = false)
  private String changedesc;

  @TableField(exist = false)
  private BigDecimal counterpartbank;
  //协商过程id
  @TableField(exist = false)
  private BigDecimal negotiaid;
  //诉讼过程记录id
  @TableField(exist = false)
  private BigDecimal proceedid;
  //诉讼过程id
  @TableField(exist = false)
  private BigDecimal litigationid;
  //仲裁过程id
  @TableField(exist = false)
  private BigDecimal arbitraid;
  //资质保全id
  @TableField(exist = false)
  private BigDecimal qualid;
  //账户冻结id
  @TableField(exist = false)
  private BigDecimal inforid;
  //结案id
  @TableField(exist = false)
  private BigDecimal closeid;

  @TableField(exist = false)
  private Integer CLOSECOUNT;
  
  @TableField(exist = false)
  private Integer ZCCOUNT;
  
  @TableField(exist = false)
  private Integer SSCOUNT;
  
  @TableField(exist = false)
  private Integer XSCOUNT;

//											<c:when test="${pageScope.t.clCount > 0}">已结案</c:when>
//											<c:when test="${pageScope.t.zcCount > 0}">仲裁中</c:when>
//											<c:when test="${pageScope.t.ssCount > 0}">诉讼中</c:when>
//											<c:when test="${pageScope.t.xcCount > 0}">协商中</c:when>
//											<c:otherwise>纠纷中</c:otherwise>

      //TBL_ORGANIZATION
 
  @Schema(name = "公司名称")
  @TableField(exist = false)
  private String orgname;
  
  @TableField(exist = false)
  private BigDecimal fatherorgid;
  
  
  @Schema(name = "公司编号")
  @TableField(exist = false)
  private String orgnumber;
  
  @Schema(name = "公司简介")
  @TableField(exist = false)
  private String orgmeno;
  
  @Schema(name = "备注")
  @TableField(exist = false)
  private String memo;
  
  @TableField(exist = false)
  private String icode;
  
  @TableField(exist = false)
  private Integer orgtype;
  
  @TableField(exist = false)
  private Integer auditType;
  
  @TableField(exist = false)
  private Integer status;
  
  @TableField(exist = false)
  private String iszy;
  
  @TableField(exist = false)
  private String hyzsktype;
  
  @TableField(exist = false)
  private Integer orderid;
  
  @TableField(exist = false)
  private Integer outsideid;
  
  @TableField(exist = false)
  private String outsideopendid;
  @Schema(name = "是否使用自动编号 0 不使用；1 使用")
  @TableField(exist = false)
  private Integer isautonumber;
  
  @TableField(exist = false)
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @JSONField(format = "yyyy-MM-dd")
  private Date orgcreate;

  @TableField(exist = false)
  private Integer isinitialization;
  @Schema(name = "职务")
  @TableField(exist = false)
  private String duties;
  @Schema(name = "行业编号")
  @TableField(exist = false)
  private BigDecimal industryid;
  
  @Schema(name = "新增来源于微信 1为微信 0为pc")
  @TableField(exist = false)
  private String bywx;
  
  @TableField(exist = false)
  private String datasource;
  
  @TableField(exist = false)
  private String historycode;
  
  @TableField(exist = false)
  private String historydepartmentid;

	
	@Schema(name="是否显示删除按钮 false-不显示 true-显示")
	@TableField(exist = false)
	private Boolean isDeleteDisplay;
}