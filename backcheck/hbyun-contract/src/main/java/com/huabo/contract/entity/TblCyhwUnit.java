package com.huabo.contract.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 合同范本
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(name="TblCyhwUnit对象")
@TableName("TBL_CYHW_UNIT")
public class TblCyhwUnit implements Serializable {
    //收款合同、合同名称
    private static final long serialVersionUID = 1L;

    public static final String HTFB = "HTGL007";//合同范本

    public static final String HTDL = "HTGL002";//合同订立

    public static final String HTBG = "HTGL005";//合同变更

    public static final String HTGD = "HTGL006";//合同归档

    @Schema(name = "合同主键自增")
    @TableId(value = "CONTRACTID", type = IdType.INPUT)
    private BigDecimal contractid;

    @Schema(name = "隶属流程id 关联Tbl_flow flowid")
    @TableField("FLOWID")
    private BigDecimal flowid;

    @Schema(name = "单位名称")
    @TableField("UNITNAME")
    private String unitname;

    @Schema(name = "合同名称")
    @TableField("CONTRACTNAME")
    private String contractname;

    @Schema(name = "合同编号")
    @TableField("CONTRACTNO")
    private String contractno;

    @Schema(name = "承办部门")
    @TableField("CONTRACTDEPT")
    private BigDecimal contractdept;

    @Schema(name = "合同对方")
    @TableField("CONTRACTLINK")
    private String contractlink;

    @Schema(name = "合同金额")
    @TableField("CONTRACTMONEY")
    private BigDecimal contractmoney;

    @Schema(name = "合同状态 1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成、7-执行中、		8-已归档、		9-已暂停、		10-已变更、		11-已终止、		12-纠纷中、		13-协商中、		14-诉讼中、		15-仲裁中、		16-已结案      17-履行完毕")
    @TableField("CONTRACTSTATUS")
    private Integer contractstatus;

    @Schema(name = "关联科室")
    @TableField("LINKDEPT")
    private String linkdept;

    @Schema(name = "隶属组织")
    @TableField("ORGID")
    private BigDecimal orgid;

    @Schema(name = "创建人")
    @TableField("CREATEUSER")
    private BigDecimal createuser;

    @Schema(name = "创建时间")
    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;

    @Schema(name = "合同简要说明")
    @TableField("MOMOCONCAT")
    private String momoconcat;

    @Schema(name = "关联的风险控制")
    @TableField("RISKCONTROL")
    private String riskcontrol;

    @Schema(name = "合同内容")
    @TableField("DESCRIBE")
    private String describe;

    @Schema(name = "承办人")
    @TableField("CONTRACTSTAFF")
    private BigDecimal contractstaff;

    @Schema(name = "保密等级")
    @TableField("SCRILEVEL")
    private String scrilevel;

    @Schema(name = "合同开始时间")
    @TableField("STARTDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startdate;

    @Schema(name = "合同结束时间")
    @TableField("ENDDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date enddate;

    @Schema(name = "合同项目")
    @TableField("CONTRACTITEM")
    private String contractitem;

    @Schema(name = "合同类型")
    @TableField("CONTRACTTYPE")
    private String contracttype;

    @Schema(name = "合同期限类型")
    @TableField("CONTRACTDATETYPE")
    private String contractdatetype;

    @Schema(name = "合同性质")
    @TableField("CONTRACTXZ")
    private String contractxz;

    @Schema(name = "合同相对方主键ID")
    @TableField("CONTRACTXDFXINFO")
    private BigDecimal contractxdfxinfo;

    @Schema(name = "合同类型 HTGL001 - 相对方、HTGL002-合同订立、HTGL003-合同用印、HTGL004-合同履行、HTGL005-变更、HTGL006-合同归档、HTGL007-合同范本")
    @TableField("RECORDTYPE")
    private String recordtype;

    @Schema(name = "隶属合同 cyhwunitId")
    @TableField("RECORDPARENT")
    private BigDecimal recordparent;

    @Schema(name = "合同金额汉字描述")
    @TableField("HZSUMOWING")
    private String hzsumowing;

    @Schema(name = "计价方式")
    @TableField("JIJIATYPE")
    private String jijiatype;

    @Schema(name = "币种")
    @TableField("MONEYTYPE")
    private String moneytype;

    @Schema(name = "收付方向")
    @TableField("DCTYPE")
    private String dctype;

    @Schema(name = "合同标的")
    @TableField("CONTRACTBD")
    private String contractbd;

    @Schema(name = "是否重大合同")
    @TableField("CONTRACTZD")
    private String contractzd;

    @Schema(name = "经办人")
    @TableField("JBSTAFF")
    private BigDecimal jbstaff;

    @Schema(name = "经办部门")
    @TableField("JBDEPT")
    private BigDecimal jbdept;

    @Schema(name = "经办公司/（合同范本）所属公司")
    @TableField("JBUNIT")
    private BigDecimal jbunit;

    @Schema(name = "执行公司")
    @TableField("ZXUNIT")
    private BigDecimal zxunit;

    @Schema(name = "是否关联主合同")
    @TableField("CONTRACTCHILDREN")
    private String contractchildren;

    @Schema(name = "是否有履行计划")
    @TableField("CONTRACTPLAN")
    private String contractplan;

    @Schema(name = "合同立项信息")
    @TableField("TOPICID")
    private String topicid;

    @Schema(name = "合同立项名称")
    @TableField("TOPICNAME")
    private String topicname;

    @Schema(name = "历史状态")
    @TableField("HISCONTRACTSTATUS")
    private BigDecimal hiscontractstatus;

    @Schema(name = "变更类型")
    @TableField("CHANGETYPE")
    private String changetype;

    @Schema(name = "变更时间")
    @TableField("CHANGEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date changedate;

    @Schema(name = "变更内容")
    @TableField("CHANGEDESC")
    private String changedesc;


    @Schema(name = "变更前的合同ID")
    @TableField("PRECONTRACTID")
    private BigDecimal preContractId;

    @Schema(name = "变更前的合同名称")
    @TableField(exist = false)
    private String preContractName;

    @Schema(name = "纠纷前合同状态")
    @TableField("HISTORYSTATUS")
    private Integer historyStatus;


    @Schema(name = "银行账号ID")
    @TableField("COUNTERPARTBANK")
    private BigDecimal counterpartbank;

    @Schema(name = "事项审议机构")
    @TableField("MATTERORG")
    private String matterorg;

    @Schema(name = "是否三重一大事项")
    @TableField("ISBIGMATTER")
    private String isbigmatter;
    @TableField(exist = false)
    private BigDecimal paymenmoney;

    @TableField(exist = false)
    private String jborgName;

    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private List<TblCyhwProjectbudget> budgetList = new ArrayList<TblCyhwProjectbudget>(0);
    @TableField(exist = false)
    private BigDecimal budgetid;
    @TableField(exist = false)
    private String counterpartno;//相对方编号
    @TableField(exist = false)
    private String totaltmoney;//注册资本
    @TableField(exist = false)
    private String projectstagegoal;//法定代表人姓名
    @TableField(exist = false)
    private String counterpartaddress;//相对方地址
    @TableField(exist = false)
    private String counterpartphone;//相对方电话
    @TableField(exist = false)
    private String counterpartcode;//邮编
    @TableField(exist = false)
    private String counterparthank;
    @TableField(exist = false)
    private Integer inspectionstatus;
    @TableField(exist = false)
    private String budgetname;//付款单位
    @TableField(exist = false)
    private String counterparttype;//相对方类型
    @TableField(exist = false)
    private String orgmeno;
    @TableField(exist = false)
    private Integer plannum;

    @TableField(exist = false)
    private String orgname;
    @TableField(exist = false)
    private String parentname;
    @TableField(exist = false)
    private String bankid;
    @TableField(exist = false)
    private String bankaccount;

    //查询条件
    @TableField(exist = false)
    private BigDecimal minMoney;
    @TableField(exist = false)
    private BigDecimal maxMoney;
    @TableField(exist = false)
    private String choicejbunitid;//经办部门主键
    @TableField(exist = false)
    private String choicejbunitname;//经办部门名称
    @TableField(exist = false)
    private String zxunitid;//承办公司主键
    @TableField(exist = false)
    private String zxunitname;//承办公司名称
    @TableField(exist = false)
    private String zxstaffid;//执行人主键
    @TableField(exist = false)
    private String zxstaffname;//执行人名称
    @TableField(exist = false)
    private String choicecontractDeptId;//执行部门
    @TableField(exist = false)
    private String recordConcatname;//相对方信息名称
    @TableField(exist = false)
    private Integer yongyinnum;
    @TableField(exist = false)
    private Date yongyintime;
    @TableField(exist = false)
    private String topic;//立项Name
    @TableField(exist = false)
    private Integer nodeCount;//经办人需要执行的任务量
    @TableField(exist = false)
    private BigDecimal nodemoney;
    @TableField(exist = false)
    private BigDecimal singingid;

    @Schema(name = "是否违约 是/否 为空查询全部")
    @TableField(exist = false)
    private String isWy;

    @Schema(name = "父级信息")
    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private TblCyhwProjectbudget parentBudget;

    @Schema(name = "隶属流程")
    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private TblFlow flowInfo;

    @Schema(name = "银行账号对象")
    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private TblCounterpartBankinfo bankinfo;

    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private List<TblContractAppendixsigning> signList = new ArrayList<TblContractAppendixsigning>(0);

    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private List<TblContractPlannode> planNodeList = new ArrayList(0);

    @JSONField(serialize = false)
    @TableField(exist = false, select = false, fill = FieldFill.DEFAULT)
    private TblFlow flow;


    public TblCyhwUnit(BigDecimal dispuinfo) {

    }

    //映射TblStaff
    @TableField(exist = false)
    private BigDecimal staffid;//主键ID,自动增长
    @TableField(exist = false)
    private String realname;//真实名字
    @TableField(exist = false)
    private String fixedphone;//固定电话
    @TableField(exist = false)
    private String address;//地址
    @TableField(exist = false)
    private String email;//邮箱
    @TableField(exist = false)
    private String miblephone;//手机号码
    @TableField(exist = false)
    private String memo;//备注
    @TableField(exist = false)
    private String username;//用户名（登录名）
    @TableField(exist = false)
    private String password;//密码
    @TableField(exist = false)
    private BigDecimal jobid;//岗位ID
    @TableField(exist = false)
    private Integer status;//状态（1启用，0弃用）
    @TableField(exist = false)
    private TblRole trole;
    @TableField(exist = false)
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @TableField(exist = false)
    private String outSideOpenId; //外部同步企业来源Id
    @TableField(exist = false)
    private BigDecimal fatherOrgId;//主键ID,自动增长

    @Schema(name = "印章所属主体")
    @TableField("SEALORGID")
    private Integer sealorgid;

    @Schema(name = "印章所属主体")
    @TableField("SEALORGNAME")
    private String sealorgname;


    @Schema(name = "授权委托人")
    @TableField("ENTRUSTSTAFFID")
    private Integer entrustStaffId;

    @Schema(name = "授权委托人Name")
    @TableField("ENTRUSTSTAFFNAME")
    private String entrustStaffName;


    @Schema(name = "合同分类")
    @TableField("TYPEFL")
    private String typefl;

    @Schema(name = "是否为多个合同")
    @TableField("ISMANY")
    private String ismany;


    @Schema(name = "多个合同数量")
    @TableField("AGREEMENTCOUNT")
    private Integer agreementcount;


    @Schema(name = "OA历史合同历史数据对应id")
    @TableField("HTOAID")
    private String htoaid;

    //----------------------------------
    @Schema(name = "归档编号")
    @TableField("BINDNO")
    private String bindno;


    @Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    private BigDecimal secrectLevelId;

    @Schema(name = "密级名称")
    @TableField("SECRECTLEVELNAME")
    private String secrectLevelName;

    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    private String staffScopeIds;

    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    private String staffScopeNames;

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
