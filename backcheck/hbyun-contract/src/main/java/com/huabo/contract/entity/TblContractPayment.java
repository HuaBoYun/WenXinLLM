package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hbfk.entity.TblStaffUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.collections.functors.FalsePredicate;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-03-15
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CONTRACT_PAYMENT")
@Schema(name="TblContractPayment对象")
public class TblContractPayment implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Long STATE_SP = Long.valueOf(1);
    public final static Long STATE_TZ = 2L;//调整
    public final static Long STATE_TG = 3L;//已通过
    public final static Long STATE_ZZ = 4L;//已终止
    public final static Long STATE_GZ = 5L;//已跟踪
    public final static Long STATE_WC = 6L;//已完成

    @TableId(value = "PAYMENTID" , type = IdType.INPUT)
    private BigDecimal paymentid;

    @TableField("NODEID")
    private BigDecimal nodeid;

    //TblCyhwProjectbudget
    @TableField("BUDGETID")
    private BigDecimal budgetid;

    @TableField("CONTRACTID")
    private BigDecimal contractid;//合同id

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("PAYMENMONEY")//已付款金额//开票金额//本次付款金额
    private BigDecimal paymenmoney;

    @TableField("NOPAYMONEY")
    private BigDecimal noPaymoney;

    @TableField("ACCUMULATEDPAYMENTS")
    private BigDecimal accumulatedpayments;
    
    @TableField("PAYMENTTITLE")//标题
    private String paymenttitle;

    @TableField("APPLYSTAFF")
    private BigDecimal applystaff;

    @TableField("APPLYORG")
    private BigDecimal applyorg;
    @TableField(exist = false)
    private BigDecimal applyOrgId;

    @TableField("PAYMENTRECORD")//款项类别
    private String paymentrecord;

    @TableField("PAYMENTTYPE")//付款方式
    private String paymenttype;

    @TableField("PAYMENTLATEDATE")//开票日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date paymentlatedate;

    @TableField("PAYMENTMEMO")
    private String paymentmemo;//备注

    @TableField("PAYMENTSTATUS")
    private Long paymentstatus;

    @TableField("APPLYDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date applydate;//申请日期

    @TableField("COUNTERBANK")
    private BigDecimal counterbank;//收款银行账号

    @TableField("ORGBANK")
    private BigDecimal orgbank;//付款银行账号
    
    @TableField("INVOICEID")
    private BigDecimal invoiceid;
    
    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblStaffUtil staff;

    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblCyhwUnit contract;

    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblCyhwProjectbudget budget;

    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblContractPlannode node;

    //执行人
    @TableField(exist = false)
    private String executestaff;//TblFormControllog

    //映射TblCyhwUnit与TblCyhwProjectbudget与TblStaff与TblOrganization与TblContractPlannode与TblCyhwProjectbudget

    @TableField(exist = false)
    @Schema(name = "隶属流程id 关联Tbl_flow flowid")
    private BigDecimal flowid;

    @TableField(exist = false)
    @Schema(name = "单位名称")
    private String unitname;

    @Schema(name = "合同名称")
    @TableField(exist = false)
    private String contractname;

    @Schema(name = "合同编号")
    @TableField(exist = false)
    private String contractno;

    @TableField(exist = false)
    @Schema(name = "承办部门")
    private BigDecimal contractdept;

    @TableField(exist = false)
    @Schema(name = "合同对方")
    private String contractlink;

    @Schema(name = "合同金额")
    @TableField(exist = false)
    private BigDecimal contractmoney;//合同总金额

    @TableField(exist = false)
    @Schema(name = "合同状态 1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、7-执行中、		8-已归档、		9-已暂停、		10-已变更、		11-已终止、		12-纠纷中、		13-协商中、		14-诉讼中、		15-仲裁中、		16-已结案")
    private Integer contractstatus;

    @TableField(exist = false)
    @Schema(name = "关联科室")
    private String linkdept;

    @Schema(name = "隶属组织")
    @TableField(exist = false)
    private BigDecimal orgid;
    @TableField(exist = false)
    @Schema(name = "创建人")
    private BigDecimal createuser;

    @TableField(exist = false)
    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createtime;

    @TableField(exist = false)
    @Schema(name = "合同简要说明")
    private String momoconcat;

    @TableField(exist = false)
    @Schema(name = "关联的风险控制")
    private String riskcontrol;

    @TableField(exist = false)
    @Schema(name = "合同内容")
    private String describe;

    @TableField(exist = false)
    @Schema(name = "承办人")
    private BigDecimal contractstaff;

    @TableField(exist = false)
    @Schema(name = "保密等级")
    private String scrilevel;

    @TableField(exist = false)
    @Schema(name = "合同开始时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date startdate;

    @TableField(exist = false)
    @Schema(name = "合同结束时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date enddate;

    @TableField(exist = false)
    @Schema(name = "合同项目")
    private String contractitem;

    @TableField(exist = false)
    @Schema(name = "合同类型")
    private String contracttype;

    @TableField(exist = false)
    @Schema(name = "合同期限类型")
    private String contractdatetype;

    @TableField(exist = false)
    @Schema(name = "合同性质")
    private String contractxz;

    @TableField(exist = false)
    @Schema(name = "合同相对方主键ID")
    private BigDecimal contractxdfxinfo;

    @TableField(exist = false)
    @Schema(name = "合同类型 HTGL001 - 相对方、HTGL002-合同订立、HTGL003-合同用印、HTGL004-合同履行、HTGL005-变更、HTGL006-合同归档、HTGL007-合同范本")
    private String recordtype;

    @TableField(exist = false)
    @Schema(name = "隶属合同 cyhwunitId")
    private BigDecimal recordparent;

    @TableField(exist = false)
    @Schema(name = "合同金额汉字描述")
    private String hzsumowing;

    @TableField(exist = false)
    @Schema(name = "计价方式")
    private String jijiatype;

    @TableField(exist = false)
    @Schema(name = "币种")
    private String moneytype;

    @TableField(exist = false)
    @Schema(name = "首付方向")
    private String dctype;

    @TableField(exist = false)
    @Schema(name = "合同标的")
    private String contractbd;

    @TableField(exist = false)
    @Schema(name = "是否重大合同")
    private String contractzd;

    @TableField(exist = false)
    @Schema(name = "经办人")
    private BigDecimal jbstaff;

    @TableField(exist = false)
    @Schema(name = "经办部门")
    private BigDecimal jbdept;

    @TableField(exist = false)
    @Schema(name = "经办公司")
    private BigDecimal jbunit;

    @TableField(exist = false)
    @Schema(name = "执行公司")
    private BigDecimal zxunit;

    @TableField(exist = false)
    @Schema(name = "是否关联主合同")
    private String contractchildren;

    @TableField(exist = false)
    @Schema(name = "是否有履行计划")
    private String contractplan;

    @TableField(exist = false)
    @Schema(name = "合同立项信息")
    private String topicid;

    @TableField(exist = false)
    @Schema(name = "合同立项名称")
    private String topicname;

    @TableField(exist = false)
    @Schema(name = "历史状态")
    private BigDecimal hiscontractstatus;

    @TableField(exist = false)
    @Schema(name = "变更类型")
    private String changetype;

    @TableField(exist = false)
    @Schema(name = "变更时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private LocalDateTime changedate;

    @TableField(exist = false)
    @Schema(name = "变更内容")
    private String changedesc;

    @TableField(exist = false)
    private BigDecimal counterpartbank;


//TblCyhwProjectbudget
    @Schema(name = "相对方名称")
    @TableField(exist = false)
    private String budgetname;

    @TableField(exist = false)
    @Schema(name = "注册资本")
    private String totaltmoney;

    @TableField(exist = false)
    @Schema(name = "所属行业")
    private String financemoney;

    @TableField(exist = false)
    @Schema(name = "第三方编号")
    private String othermoney;

    @TableField(exist = false)
    @Schema(name = "货物类外键")
    private Integer goodstype;

    @TableField(exist = false)
    @Schema(name = "服务类 内外部")
    private String servicetype;

    @TableField(exist = false)
    @Schema(name = "相对方性质")
    private String projecttype;

    @TableField(exist = false)
    @Schema(name = "相对方类型")
    private String orthertype;

    @TableField(exist = false)
    @Schema(name = "是否是政府采购 1是 2否")
    private Integer isgovernment;

    @TableField(exist = false)
    @Schema(name = "是否是直接支付1是 2否")
    private Integer isdirect;

    @TableField(exist = false)
    @Schema(name = "支出项目类别 1.经常性 2.一次性")
    private Integer itembudgettype;

    @TableField(exist = false)
    @Schema(name = "统一社会信用代码")
    private String projecteason;

    @TableField(exist = false)
    @Schema(name = "项目总体目标 自编号")
    private String projectgoal;

    @Schema(name = "法定代表人姓名")
    @TableField(exist = false)
    private String projectstagegoal;

    @TableField(exist = false)
    @Schema(name = "法定代表人身份证")
    private String projectcondition;

    @TableField(exist = false)
    @Schema(name = "证件类型")
    private String projectrisk;

    @TableField(exist = false)
    @Schema(name = "关联部门")
    private Integer linkdepr;

    @TableField(exist = false)
    @Schema(name = "报送单位部门")
    private Integer reporttodept;

    @TableField(exist = false)
    @Schema(name = "审批状态  1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、else-未审批 ")
    private Integer inspectionstatus;

    @TableField(exist = false)
    @Schema(name = "货物单价")
    private BigDecimal goodsprice;

    @TableField(exist = false)
    @Schema(name = "货物数量")
    private BigDecimal goodsamount;

    @TableField(exist = false)
    @Schema(name = "货物型号")
    private String goodsmodel;

    @Schema(name = "相对方编号")//合同向对方
    @TableField(exist = false)
    private String counterpartno;

    @TableField(exist = false)
    @Schema(name = "相对方地址")
    private String counterpartaddress;

    @TableField(exist = false)
    @Schema(name = "邮编")//
    private String counterpartcode;

    @TableField(exist = false)
    @Schema(name = "开户银行")
    private String counterparthank;

    @TableField(exist = false)
    @Schema(name = "开户银行账号")
    private String counterparthankaccount;

    @TableField(exist = false)
    @Schema(name = "网址")
    private String counterpartnetaddress;

    @TableField(exist = false)
    @Schema(name = "相对方电话")
    private String counterpartphone;

    @TableField(exist = false)
    @Schema(name = "关联的相对方信息")
    private Integer recordconcat;

    @TableField(exist = false)
    @Schema(name = "地址")
    private String action;

    @TableField(exist = false)
    @Schema(name = "e签宝流程Id")
    private String eqbflowid;

    @TableField(exist = false)
    private String bizno;

    @TableField(exist = false)
    @Schema(name = "纳税人识别号")
    private String resultdescription;

    @TableField(exist = false)
    private Integer eqbstatus;

    @TableField(exist = false)
    private String flowtype;

    @TableField(exist = false)
    @Schema(name = "附件查看地址")
    private String eabfileck;

    @TableField(exist = false)
    @Schema(name = "附件下载地址")
    private String fileurl;

    @TableField(exist = false)
    @Schema(name = "相对方性质")
    private String oppositenature;

    @TableField(exist = false)
    @Schema(name = "相对方类型")
    private String counterparttype;

    @TableField(exist = false)
    @Schema(name = "是否有效")
    private String iseffect;

    @TableField(exist = false)
    @Schema(name = "相对方简介")
    private String counterpartdesc;

    @TableField(exist = false)
    @Schema(name = "保管部门")
    private Integer safeorg;

    @TableField(exist = false)
    @Schema(name = "保管员")
    private Integer safestaff;

    @TableField(exist = false)
    @Schema(name = "档案有效期、黑明单有效期")
    private Date effectdate;

    @TableField(exist = false)
    @Schema(name = "归档日期")
    private Date safedate;

    @TableField(exist = false)
    @Schema(name = "负责人")
    private String director;

    @TableField(exist = false)
    @Schema(name = "联系人")
    private String contacts;

    @TableField(exist = false)
    @Schema(name = "联系人电话")
    private String contactsphone;

    @TableField(exist = false)
    @Schema(name = "联系地址")
    private String contactsadress;

    @TableField(exist = false)
    @Schema(name = "电子邮箱")
    private String contactsemail;

    @TableField(exist = false)
    @Schema(name = "岗位")
    private String station;

    @TableField(exist = false)
    @Schema(name = "称呼")
    private String callname;

    @TableField(exist = false)
    @Schema(name = "备注")
    private String remarks;

    @TableField(exist = false)
    @Schema(name = "是否加入黑名单 1是 2否")
    private Integer isblack;

    @TableField(exist = false)
    @Schema(name = "黑名单类型   1 短期   2长期")
    private Integer blacktype;

    @TableField(exist = false)
    @Schema(name = "证件有效期")
    @JSONField(format = "yyyy-MM-dd")
    private Date pstartdate;

    @TableField(exist = false)
    @Schema(name = "证件有效期")
    @JSONField(format = "yyyy-MM-dd")
    private Date penddate;

    @TableField(exist = false)
    @Schema(name = "证件编号")
    private String cretificateno;


    //TblStaff
    @TableField(exist = false)
    private BigDecimal staffid;
    @TableField(exist = false)
    private String realname;//真实名字或经办人


    @Schema(name = "公司名称")
    @TableField(exist = false)
    private String orgname;
    @TableField(exist = false)
    private String nodecontent;//付款计划
    @TableField(exist = false)
    private String nodepost;

    @TableField(exist = false)
    private String bankaccname;//银行账户
    @TableField(exist = false)
    private BigDecimal bankbankid;
    @TableField(exist = false)
    private BigDecimal bankid;
    @TableField(exist = false)
    private BigDecimal nodemoney;//付款金额
    @TableField(exist = false)
    private  BigDecimal paymentmoney;//
    @TableField(exist = false)
    private String invoicekporg;

    @TableField(exist = false)
    private String invoicetype;
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date invoicedate;
    @TableField(exist = false)
    private String invoiceno;
    @TableField(exist = false)
    private BigDecimal invoicestatus;
    @TableField(exist = false)
    private BigDecimal invoicemoney;
    @TableField(exist = false)
    private String invoiceheadtext;


    //TblOrganization
    @TableField(exist = false)
    private BigDecimal fatherorgid;
    @TableField(exist = false)
    @Schema(name = "公司编号")
    private String orgnumber;
    @TableField(exist = false)
    @Schema(name = "公司简介")
    private String orgmeno;//申请部门
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
    @TableField(exist = false)
    @Schema(name = "是否使用自动编号 0 不使用；1 使用")
    private Integer isautonumber;
    @TableField(exist = false)
    @JSONField(format = "yyyy-MM-dd")
    private Date orgcreate;
    @TableField(exist = false)
    private Integer isinitialization;
    @Schema(name = "职务")
    @TableField(exist = false)
    private String duties;
    @Schema(name = "行业编号")
    @TableField(exist = false)
    private Integer industryid;
    @TableField(exist = false)
    @Schema(name = "新增来源于微信 1为微信 0为pc")
    private String bywx;
    private String datasource;
    @TableField(exist = false)
    private String historycode;
    @TableField(exist = false)
    private String historydepartmentid;

    //TblOrgBankaccount
    //付款银行账户
    @TableField(exist = false)
    private String bankaccnum;
    //收款银行账户
    @TableField(exist = false)
    private String bankaccount;
    @TableField(exist = false)
    private BigDecimal APSSTAFFID;
    @TableField(exist = false)
    private String APSREALNAME;
    @TableField(exist = false)
    private BigDecimal APOORGID;
    @TableField(exist = false)
    private String APOORGNAME;
    @TableField(exist = false)
    private BigDecimal PAYMONEY;
    @TableField(exist = false)
    private BigDecimal FKMONEY;
}
