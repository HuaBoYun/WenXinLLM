package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CYHW_PROJECTBUDGET")
@Schema(name="TblCyhwProjectbudget对象")
public class TblCyhwProjectbudget implements Serializable {
	//相对方预警
    private static final long serialVersionUID = 1L;

    public static final String FLOWNUMBER = "HTGL001";
    
    public static final String HTYY = "HTGL003";
    
    public static final String XDFHMDGL = "HTGL008";//合同管理相对方黑名单管理
    
    @TableId(value = "BUDGETID",type = IdType.INPUT)
    @Schema(name = "相对方主键")
    private BigDecimal budgetid;

    @TableField("BUDGETNAME")
    @Schema(name = "相对方名称")//付款单位
    private String budgetname;

    @TableField("TOTALTMONEY")
    @Schema(name = "注册资本")
    private String totaltmoney;

    @TableField("FINANCEMONEY")
    @Schema(name = "所属行业")
    private String financemoney;

    @TableField("OTHERMONEY")
    @Schema(name = "第三方编号")
    private String othermoney;

    @TableField("GOODSTYPE")
    @Schema(name = "货物类外键")
    private Integer goodstype;

    @TableField("SERVICETYPE")
    @Schema(name = "服务类 内外部")
    private String servicetype;

    @TableField("PROJECTTYPE")
    @Schema(name = "相对方性质")
    private String projecttype;

    @TableField("ORTHERTYPE")
    @Schema(name = "相对方类型")
    private String orthertype;

    @TableField("ISGOVERNMENT")
    @Schema(name = "是否是政府采购 1是 2否")
    private Integer isgovernment;

    @TableField("ISDIRECT")
    @Schema(name = "是否是直接支付1是 2否")
    private Integer isdirect;

    @TableField("ITEMBUDGETTYPE")
    @Schema(name = "支出项目类别 1.经常性 2.一次性")
    private Integer itembudgettype;

    @TableField("PROJECTEASON")
    @Schema(name = "统一社会信用代码")
    private String projecteason;

    @TableField("PROJECTGOAL")
    @Schema(name = "项目总体目标 自编号")
    private String projectgoal;

    @TableField("PROJECTSTAGEGOAL")
    @Schema(name = "法定代表人姓名")
    private String projectstagegoal;

    @TableField("PROJECTCONDITION")
    @Schema(name = "法定代表人身份证")
    private String projectcondition;

    @TableField("PROJECTRISK")
    @Schema(name = "证件类型")
    private String projectrisk;

    @TableField("ORGID")
    @Schema(name = "隶属组织")
    private BigDecimal orgid;

    
    @TableField("CREATEUSER")
    @Schema(name = "创建用户")
    private BigDecimal createuser;

    @TableField("CREATETIME")
    @Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;

    @TableField("FLOWID")
    @Schema(name = "隶属流程Id Tbl_Flow")
    private BigDecimal flowid;

    @TableField("LINKDEPR")
    @Schema(name = "关联部门")
    private BigDecimal linkdepr;

    @TableField("REPORTTODEPT")
    @Schema(name = "报送单位部门")
    private BigDecimal reporttodept;

    @TableField("INSPECTIONSTATUS")
    @Schema(name = "审批状态  1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、else-未审批 ")
    private Integer inspectionstatus;

    @TableField("GOODSPRICE")
    @Schema(name = "货物单价")
    private BigDecimal goodsprice;

    @TableField("GOODSAMOUNT")
    @Schema(name = "货物数量")
    private BigDecimal goodsamount;

    @TableField("GOODSMODEL")
    @Schema(name = "货物型号")
    private String goodsmodel;

    @TableField("COUNTERPARTNO")
    @Schema(name = "相对方编号")
    private String counterpartno;//合同相对方

    @TableField("COUNTERPARTADDRESS")
    @Schema(name = "相对方地址")
    private String counterpartaddress;

    @TableField("COUNTERPARTCODE")
    @Schema(name = "邮编")
    private String counterpartcode;

    @TableField("COUNTERPARTHANK")
    @Schema(name = "开户银行")
    private String counterparthank;

    @TableField("COUNTERPARTHANKACCOUNT")
    @Schema(name = "开户银行账号")
    private String counterparthankaccount;

    @TableField("COUNTERPARTNETADDRESS")
    @Schema(name = "网址")
    private String counterpartnetaddress;

    @TableField("COUNTERPARTPHONE")
    @Schema(name = "相对方电话")
    private String counterpartphone;

    @TableField("RECORDTYPE")
   @Schema(name = "合同类型 HTGL001 - 相对方、HTGL003-合同用印")
    private String recordtype;

    @TableField("RECORDPARENT")
    @Schema(name = "该记录的父级")
    private BigDecimal recordparent;

    
    @TableField("RECORDCONCAT")
    @Schema(name = "关联的相对方信息")
    private BigDecimal recordconcat;

    @TableField("ACTION")
    @Schema(name = "地址")
    private String action;

    @TableField("EQBFLOWID")
    @Schema(name = "e签宝流程Id")
    private String eqbflowid;

    @TableField("BIZNO")
    private String bizno;

    @TableField("RESULTDESCRIPTION")
    @Schema(name = "纳税人识别号")
    private String resultdescription;

    @TableField("EQBSTATUS")
    private Integer eqbstatus;

    @TableField("FLOWTYPE")
    private String flowtype;

    @TableField("EABFILECK")
    @Schema(name = "附件查看地址")
    private String eabfileck;

    @TableField("FILEURL")
    @Schema(name = "附件下载地址")
    private String fileurl;

    @TableField("OPPOSITENATURE")
    @Schema(name = "相对方性质")
    private String oppositenature;

    @TableField("COUNTERPARTTYPE")
    @Schema(name = "相对方类型")
    private String counterparttype;

    @TableField("ISEFFECT")
    @Schema(name = "是否有效")
    private String iseffect;

    @TableField("COUNTERPARTDESC")
    @Schema(name = "相对方简介")
    private String counterpartdesc;

    @TableField("SAFEORG")
    @Schema(name = "保管部门")
    private BigDecimal safeorg;

    @TableField("SAFESTAFF")
    @Schema(name = "保管员")
    private BigDecimal safestaff;

    @TableField("EFFECTDATE")
    @Schema(name = "档案有效期、黑明单有效期")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date effectdate;

    @TableField("SAFEDATE")
    @Schema(name = "归档日期")
    @JSONField(format = "yyyy-MM-dd")
    private Date safedate;

      @Schema(name = "负责人")
      @TableField("DIRECTOR")
    private String director;

      @Schema(name = "联系人")
      @TableField("CONTACTS")
    private String contacts;

      @Schema(name = "联系人电话")
      @TableField("CONTACTSPHONE")
    private String contactsphone;

      @Schema(name = "联系地址")
      @TableField("CONTACTSADRESS")
    private String contactsadress;

      @Schema(name = "电子邮箱")
      @TableField("CONTACTSEMAIL")
    private String contactsemail;

      @Schema(name = "岗位")
      @TableField("STATION")
    private String station;

      @Schema(name = "称呼")
      @TableField("CALLNAME")
    private String callname;

      @Schema(name = "备注")
      @TableField("REMARKS")
    private String remarks;
      
    @TableField("ISBLACK")
    @Schema(name = "是否加入黑名单 1是 2否")
    private Integer isblack;

    @TableField("BLACKTYPE")//黑名单期限
    @Schema(name = "黑名单类型   1 短期   2长期")
    private Integer blacktype;

    @TableField("PSTARTDATE")
    @Schema(name = "证件有效期 开始时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date pstartdate;

    @TableField("PENDDATE")
    @Schema(name = "证件有效期 结束时间")
    @JSONField(format = "yyyy-MM-dd")
    private Date penddate;
    
    @TableField("CERTYPE")
    @Schema(name = "证件有效期类型 0- 短期 ，1-长期  ，默认0")
    private Integer cerType;

    @TableField("CRETIFICATENO")
    @Schema(name = "证件编号")
    private String cretificateno;
    
    @TableField("VERSIONNO")
    @Schema(name = "版本")
    private Integer versionNo;

    @TableField("CHANGESTATUS")
    @Schema(name = "相对方变更审批状态")
    private String changeStatus;
    

    @Schema(name = "相对方签署的合同数量")
    private Integer unitCount;
    
    //Hu改
    @Schema(name = "今年合同数量")
    private Integer nowyearCount;
    
    @Schema(name = "相对方违约合同连接段的数量")
    private Integer performanceCount;

	@TableField(exist = false)
    @Schema(name = "隶属组织")
    private TblOrganization linkOrg;

	@TableField(exist = false)
    @Schema(name = "创建用户")
    private TblStaff createStaff;

	@TableField(exist = false)
    @Schema(name = "隶属流程")
    private TblFlow tblFlow;
	
	
	@TableField(exist = false)
    @Schema(name = "关联部门")
    private TblOrganization relaDept;
	@TableField(exist = false)
    @Schema(name = "报送科室")
    private TblOrganization reportDept;

	@TableField(exist = false)
    @Schema(name = "关联合同")
    private TblCyhwUnit recordUnit;
	@TableField(exist = false)
    @Schema(name = "父级信息")
    private TblCyhwProjectbudget parentBudget;
	@TableField(exist = false)
    @Schema(name = "隶属流程")
    private TblFlow flowInfo;
	@TableField(exist = false)
    @Schema(name = "黑名单加入信息记录表")
    private TblOppblackRecord blackRecord;
	@TableField(exist = false)
    @Schema(name = "黑名单取消信息记录表")
    private TblOppblackRemove removeRecord;
    
    
//    @Transient
//    @TableField("附件列表")
//    private List<TblAttachment> attList = new ArrayList(0);
    
    @Schema(name = "银行列表")
    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private List<TblCounterpartBankinfo> bankInfoList = new ArrayList(0);
    private String budgettype;//相对方类型
    private String realname;//真实名字
    ///contract/counterpartManageList银行列表
    private BigDecimal bankid;
    @TableField("OUTSIDEID")
    @Schema(name = "外部来源主键")
	private String outsideId;//外部来源主键
    
    
    @TableField("BLACKAPRSTATUS")
    @Schema(name = "加入黑名单审批状态：1=审批中，2=需调整，6=已完成")
    private Integer blackAprStatus;



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
