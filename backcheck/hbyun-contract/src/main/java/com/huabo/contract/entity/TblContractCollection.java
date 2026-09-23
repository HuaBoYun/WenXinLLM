package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTRACT_COLLECTION")
@Schema(name="TblContractCollection对象")
public class TblContractCollection implements Serializable {
//收款管理
    private static final long serialVersionUID = 1L;

    public final static Long STATE_SP = 1L;//审批中
    public final static Long STATE_TZ = 2L;//调整
    public final static Long STATE_TG = 3L;//已通过
    public final static Long STATE_ZZ = 4L;//已终止
    public final static Long STATE_GZ = 5L;//已跟踪
    public final static Long STATE_WC = 6L;//已完成

    @TableId(value = "COLLECTIONID" , type = IdType.INPUT)
    private BigDecimal collectionid;

    @TableField("INVOICEID")
    private BigDecimal invoiceid;

    @TableField("COLLECTIONBANK")
    private String collectionbank;

    @TableField("COLLECTIONSKDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date collectionskdate;//收款日期

    @TableField("COLLECTIONACCOUNT")
    private String collectionaccount;

    @TableField("COLLECTIONORGNAME")//付款单位
    private String collectionorgname;

    @TableField("CONTRACTID")
    private BigDecimal contractid;

    @TableField("NODEID")
    private BigDecimal nodeid;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("COLLECTIONSTATUS")//审批状态：1-审批中，2-需调整，3-已通过，4-已终止，5-已跟踪，6-已完成，7-未审批
    private Long collectionstatus;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("CREATEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdate;

//    @TableField("NODEMONEY")//对应收款金额
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal nodemoney;

    @TableField("COUNTERBANK")//财务管理-收款管理-新增-付款银行账号
    private BigDecimal counterbank;

    @TableField("ORGBANK")//财务管理-收款管理-新增-收款银行账号
    private BigDecimal orgbank;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal bankbankid;
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal bankid;
    
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private TblStaff createStaff;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private TblCyhwUnit contract;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private TblOrganization linkOrg;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private TblContractPlannode node;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private TblContractInvoicesmanagemen invoice;


    //映射TblContractInvoicesmanagemen与TblCyhwUnit

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal budgetid;
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private String invoiceno;
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal invoicemoney;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date invoicedate;//开票日期

    private String invoiceheadtext;

    private BigDecimal invoicepost;

    //发票内容
    private String invoicecontent;

    //发票类型
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private String invoicetype;

    //发票状态
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal invoicestatus;


    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    @JSONField(format = "yyyy-MM-dd")
    private Date invoicespdate;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private String invoicesporg;

    //开票单位
    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private String invoicekporg;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private BigDecimal invoiceogr;

    private String tinumber;

    private String outsideid;



    @Schema(name = "隶属流程id 关联Tbl_flow flowid")
    private BigDecimal flowid;

    @Schema(name = "单位名称")
    private String unitname;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    @Schema(name = "合同名称")
    private String contractname;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    @Schema(name = "合同编号")
    private String contractno;

    @Schema(name = "承办部门")
    private BigDecimal contractdept;

    @Schema(name = "合同对方")
    private String contractlink;

    @Schema(name = "合同金额")
    private BigDecimal contractmoney;

    @Schema(name = "合同状态 1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、7-执行中、		8-已归档、		9-已暂停、		10-已变更、		11-已终止、		12-纠纷中、		13-协商中、		14-诉讼中、		15-仲裁中、		16-已结案")
    private Integer contractstatus;

    @Schema(name = "关联科室")
    private String linkdept;

    @Schema(name = "隶属组织")
    private BigDecimal orgid;

    @Schema(name = "创建人")
    private BigDecimal createuser;

    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    @Schema(name = "合同简要说明")
    private String momoconcat;

    @Schema(name = "关联的风险控制")
    private String riskcontrol;

    @Schema(name = "合同内容")
    private String describe;

    @Schema(name = "承办人")
    private BigDecimal contractstaff;

    @Schema(name = "保密等级")
    private String scrilevel;

    @Schema(name = "合同开始时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date startdate;

    @Schema(name = "合同结束时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date enddate;

    @Schema(name = "合同项目")
    private String contractitem;

    @Schema(name = "合同类型")
    private String contracttype;

    @Schema(name = "合同期限类型")
    private String contractdatetype;

    @Schema(name = "合同性质")
    private String contractxz;

    @Schema(name = "合同相对方主键ID")
    private BigDecimal contractxdfxinfo;

    @Schema(name = "合同类型 HTGL001 - 相对方、HTGL002-合同订立、HTGL003-合同用印、HTGL004-合同履行、HTGL005-变更、HTGL006-合同归档、HTGL007-合同范本")
    private String recordtype;

    @Schema(name = "隶属合同 cyhwunitId")
    private BigDecimal recordparent;

    @Schema(name = "合同金额汉字描述")
    private String hzsumowing;

    @Schema(name = "计价方式")
    private String jijiatype;

    @Schema(name = "币种")
    private String moneytype;

    @Schema(name = "首付方向")
    private String dctype;

    @Schema(name = "合同标的")
    private String contractbd;

    @Schema(name = "是否重大合同")
    private String contractzd;

    @Schema(name = "经办人")
    private BigDecimal jbstaff;

    @Schema(name = "经办部门")
    private BigDecimal jbdept;

    @Schema(name = "经办公司")
    private BigDecimal jbunit;

    @Schema(name = "执行公司")
    private BigDecimal zxunit;

    @Schema(name = "是否关联主合同")
    private String contractchildren;

    @Schema(name = "是否有履行计划")
    private String contractplan;

    @Schema(name = "合同立项信息")
    private String topicid;

    @Schema(name = "合同立项名称")
    private String topicname;

    @Schema(name = "历史状态")
    private BigDecimal hiscontractstatus;

    @Schema(name = "变更类型")
    private String changetype;

    @Schema(name = "变更时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime changedate;

    @Schema(name = "变更内容")
    private String changedesc;

    private BigDecimal counterpartbank;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    @Schema(name = "相对方名称")//付款单位
    private String budgetname;

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private String nodecontent;

    private String counterBankAccount;

    private String orgBankName;

    private String bankaccname;//收款银行名称

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    private String bankaccount;//收款银行账号

    @TableField(exist = false,select = false , fill = FieldFill.DEFAULT)
    //TblOrgBankaccount付款银行账号
    private String bankaccnum;

    //TblFlow
    @Schema(name = "流程编号")
    private String flownumber;
    @Schema(name = "流程名称")
    private String flowname;
    @Schema(name = "公司ID")
    private String company;
    @Schema(name = "部门ID")
    private String departincharge;
    @Schema(name = "使用范围")
    private String flowrange;
    @Schema(name = "流程状态")
    private String flowstatus;
    @Schema(name = "备注")
    private String memo;
    @Schema(name = "版本")
    private BigDecimal version;
    private String flowchart;
    @Schema(name = "业务参与部门")
    private String departassist;
    @Schema(name = "录入人")
    private String editor;
    @Schema(name = "更新时间")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDateTime updatetime;
    private String relatedrules;
    private String affectdegree;
    private String interfaced;
    @Schema(name = "父流程ID")
    private BigDecimal fatherflowid;
    @Schema(name = "最后更改时间")
    private String lastmodifiedtime;
    private Integer flowbysystem;
    private BigDecimal inflowdb;
    private BigDecimal position;
    private BigDecimal versiontype;
    @Schema(name = "审批状态")
    private BigDecimal status;
    @Schema(name = "系统流程Id")
    private String settingid;
    @Schema(name = "表单ID")
    private BigDecimal fromid;
    @Schema(name = "是否启用  1 启动 2弃用 null 未启动")
    private BigDecimal firingstatus;
    @Schema(name = "流程图Id")
    private String editmodule;
    @Schema(name = "后台访问地址")
    private String flowmappingurl;

    //TblContractPlannode
    private BigDecimal projectid;
    private BigDecimal blprojectid;
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date planstartdate;
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date planenddate;//预计结束时间
    private String nodepost;
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date nodeplanpaydate;//预计开始时间
    private BigDecimal dispatchstaff;
    private BigDecimal dispatchdept;
    private BigDecimal plannodestatus;
    @Schema(name = "反馈意见")
    private String feedback;


    @Schema(name = "taskId")
    private String taskid;




	@TableField(value = "RESERVEDSTRING1")
	@Schema(name = "预留字符串1")
	private String reservedstring1;

	@TableField(value = "RESERVEDSTRING2")
	@Schema(name = "预留字符串2")
	private String reservedstring2;

	@TableField(value = "RESERVEDSTRING3")
	@Schema(name = "预留字符串3")
	private String reservedstring3;

	@TableField(value = "RESERVEDSTRING4")
	@Schema(name = "预留字符串4")
	private String reservedstring4;

	@TableField(value = "RESERVEDSTRING5")
	@Schema(name = "预留字符串5")
	private String reservedstring5;

	@TableField(value = "RESERVEDSTRING6")
	@Schema(name = "预留字符串6")
	private String reservedstring6;

	@TableField(value = "RESERVEDSTRING7")
	@Schema(name = "预留字符串7")
	private String reservedstring7;

	@TableField(value = "RESERVEDSTRING8")
	@Schema(name = "预留字符串8")
	private String reservedstring8;

	@TableField(value = "RESERVEDSTRING9")
	@Schema(name = "预留字符串9")
	private String reservedstring9;

	@TableField(value = "RESERVEDSTRING10")
	@Schema(name = "预留字符串10")
	private String reservedstring10;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE1")
	@Schema(name = "预留下拉单选字符串1")
	private String reserveddropdownsinglechoice1;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE2")
	@Schema(name = "预留下拉单选字符串2")
	private String reserveddropdownsinglechoice2;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE3")
	@Schema(name = "预留下拉单选字符串3")
	private String reserveddropdownsinglechoice3;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE4")
	@Schema(name = "预留下拉单选字符串4")
	private String reserveddropdownsinglechoice4;

	@TableField(value = "RESERVEDDROPDOWNSINGLECHOICE5")
	@Schema(name = "预留下拉单选字符串5")
	private String reserveddropdownsinglechoice5;

	@TableField(value = "RESERVEDNUM1")
	@Schema(name = "预留数字1")
	private Integer reservednum1;

	@TableField(value = "RESERVEDNUM2")
	@Schema(name = "预留数字2")
	private Integer reservednum2;

	@TableField(value = "RESERVEDNUM3")
	@Schema(name = "预留数字3")
	private Integer reservednum3;

	@TableField(value = "RESERVEDNUM4")
	@Schema(name = "预留数字4")
	private Integer reservednum4;

	@TableField(value = "RESERVEDNUM5")
	@Schema(name = "预留数字5")
	private Integer reservednum5;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE1")
	@Schema(name = "预留下拉多选字符串1")
	private String reserveddropdownmultiple1;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE2")
	@Schema(name = "预留下拉多选字符串2")
	private String reserveddropdownmultiple2;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE3")
	@Schema(name = "预留下拉多选字符串3")
	private String reserveddropdownmultiple3;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE4")
	@Schema(name = "预留下拉多选字符串4")
	private String reserveddropdownmultiple4;

	@TableField(value = "RESERVEDDROPDOWNMULTIPLE5")
	@Schema(name = "预留下拉多选字符串5")
	private String reserveddropdownmultiple5;


	@TableField(value = "RESERVEDSINGLECHOICE1")
	@Schema(name = "预留单选字符串1")
	private String reservedsinglechoice1;

	@TableField(value = "RESERVEDSINGLECHOICE2")
	@Schema(name = "预留单选字符串2")
	private String reservedsinglechoice2;

	@TableField(value = "RESERVEDSINGLECHOICE3")
	@Schema(name = "预留单选字符串3")
	private String reservedsinglechoice3;

	@TableField(value = "RESERVEDSINGLECHOICE4")
	@Schema(name = "预留单选字符串4")
	private String reservedsinglechoice4;

	@TableField(value = "RESERVEDSINGLECHOICE5")
	@Schema(name = "预留单选字符串5")
	private String reservedsinglechoice5;


	@TableField(value = "RESERVEDMULTIPLECHOICE1")
	@Schema(name = "预留多选字符串1")
	private String reservedmultiplechoice1;

	@TableField(value = "RESERVEDMULTIPLECHOICE2")
	@Schema(name = "预留多选字符串2")
	private String reservedmultiplechoice2;

	@TableField(value = "RESERVEDMULTIPLECHOICE3")
	@Schema(name = "预留多选字符串3")
	private String reservedmultiplechoice3;

	@TableField(value = "RESERVEDMULTIPLECHOICE4")
	@Schema(name = "预留多选字符串4")
	private String reservedmultiplechoice4;

	@TableField(value = "RESERVEDMULTIPLECHOICE5")
	@Schema(name = "预留多选字符串5")
	private String reservedmultiplechoice5;


	@TableField(value = "RESERVEDTIME1")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间1(年月日)")
	private Date reservedtime1;

	@TableField(value = "RESERVEDTIME2")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间2(年月日)")
	private Date reservedtime2;

	@TableField(value = "RESERVEDTIME3")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间3(年月日)")
	private Date reservedtime3;

	@TableField(value = "RESERVEDTIME4")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间4(年月日)")
	private Date reservedtime4;

	@TableField(value = "RESERVEDTIME5")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "预留时间5(年月日)")
	private Date reservedtime5;


	@TableField(value = "RESERVEDYEARTIME1")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间1(年)")
	private Date reservedyeartime1;

	@TableField(value = "RESERVEDYEARTIME2")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间2(年)")
	private Date reservedyeartime2;

	@TableField(value = "RESERVEDYEARTIME3")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间3(年)")
	private Date reservedyeartime3;

	@TableField(value = "RESERVEDYEARTIME4")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间4(年)")
	private Date reservedyeartime4;

	@TableField(value = "RESERVEDYEARTIME5")
	@JsonFormat(pattern = "yyyy", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy")
	@Schema(name = "预留时间5(年)")
	private Date reservedyeartime5;


	@TableField(value = "RESERVEDYEARACCURATETIME1")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间1(年月日时分秒)")
	private Date reservedyearaccuratetime1;

	@TableField(value = "RESERVEDYEARACCURATETIME2")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间2(年月日时分秒)")
	private Date reservedyearaccuratetime2;

	@TableField(value = "RESERVEDYEARACCURATETIME3")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间3(年月日时分秒)")
	private Date reservedyearaccuratetime3;

	@TableField(value = "RESERVEDYEARACCURATETIME4")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间4(年月日时分秒)")
	private Date reservedyearaccuratetime4;

	@TableField(value = "RESERVEDYEARACCURATETIME5")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(name = "预留时间5(年月日时分秒)")
	private Date reservedyearaccuratetime5;


	@TableField(value = "RESERVEDCONTENT1")
	@Schema(name = "预留富文本1")
	private String reservedcontent1;

	@TableField(value = "RESERVEDCONTENT2")
	@Schema(name = "预留富文本2")
	private String reservedcontent2;

	@TableField(value = "RESERVEDCONTENT3")
	@Schema(name = "预留富文本3")
	private String reservedcontent3;

	@TableField(value = "RESERVEDCONTENT4")
	@Schema(name = "预留富文本4")
	private String reservedcontent4;

	@TableField(value = "RESERVEDCONTENT5")
	@Schema(name = "预留富文本5")
	private String reservedcontent5;

	@TableField(value = "STAFFID1")
	@Schema(name = "预留人员单选1")
	private Long staffid1;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称1")
	private String realname1;

	@TableField(value = "STAFFID2")
	@Schema(name = "预留人员单选2")
	private Long staffid2;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称2")
	private String realname2;

	@TableField(value = "STAFFID3")
	@Schema(name = "预留人员单选3")
	private Long staffid3;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称3")
	private String realname3;

	@TableField(value = "STAFFID4")
	@Schema(name = "预留人员单选4")
	private Long staffid4;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称4")
	private String realname4;

	@TableField(value = "STAFFID5")
	@Schema(name = "预留人员单选5")
	private Long staffid5;

	@TableField(exist = false)
	@Schema(name = "预留人员单选名称5")
	private String realname5;

	@TableField(value = "STAFFIDS1")
	@Schema(name = "预留人员多选1")
	private String staffids1;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称1")
	private String realnames1;

	@TableField(value = "STAFFIDS2")
	@Schema(name = "预留人员多选2")
	private String staffids2;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称2")
	private String realnames2;

	@TableField(value = "STAFFIDS3")
	@Schema(name = "预留人员多选3")
	private String staffids3;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称3")
	private String realnames3;

	@TableField(value = "STAFFIDS4")
	@Schema(name = "预留人员多选4")
	private String staffids4;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称4")
	private String realnames4;

	@TableField(value = "STAFFIDS5")
	@Schema(name = "预留人员多选5")
	private String staffids5;

	@TableField(exist = false)
	@Schema(name = "预留人员多选名称5")
	private String realnames5;

	@TableField(value = "ORGID1")
	@Schema(name = "预留组织单选1")
	private Long orgid1;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称1")
	private String orgname1;

	@TableField(value = "ORGID2")
	@Schema(name = "预留组织单选2")
	private Long orgid2;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称2")
	private String orgname2;

	@TableField(value = "ORGID3")
	@Schema(name = "预留组织单选3")
	private Long orgid3;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称3")
	private String orgname3;

	@TableField(value = "ORGID4")
	@Schema(name = "预留组织单选4")
	private Long orgid4;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称4")
	private String orgname4;

	@TableField(value = "ORGID5")
	@Schema(name = "预留组织单选5")
	private Long orgid5;

	@TableField(exist = false)
	@Schema(name = "预留组织单选名称5")
	private String orgname5;

	@TableField(value = "ORGIDS1")
	@Schema(name = "预留组织多选1")
	private String orgids1;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称1")
	private String orgnames1;

	@TableField(value = "ORGIDS2")
	@Schema(name = "预留组织多选2")
	private String orgids2;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称2")
	private String orgnames2;

	@TableField(value = "ORGIDS3")
	@Schema(name = "预留组织多选3")
	private String orgids3;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称3")
	private String orgnames3;

	@TableField(value = "ORGIDS4")
	@Schema(name = "预留组织多选4")
	private String orgids4;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称4")
	private String orgnames4;

	@TableField(value = "ORGIDS5")
	@Schema(name = "预留组织多选5")
	private String orgids5;

	@TableField(exist = false)
	@Schema(name = "预留组织多选名称5")
	private String orgnames5;

}
