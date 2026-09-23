package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @since 2022-03-21
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_PROCEEDINGSRECORD")
@Schema(name="TblLegalProceedingsrecord对象")
public class TblLegalProceedingsrecord implements Serializable {
//法务管理-诉讼过程-新建-诉讼过程记录-列表
    private static final long serialVersionUID = 1L;

      @TableId(value = "PROCEEDID" , type = IdType.INPUT)
      private BigDecimal proceedid;

    @TableField("PROCEEDNO")//案号
    private String proceedno;

    @TableField("PORCEEDSTAGE")//诉讼阶段
    private String porceedstage;

    @TableField("COURT")//审理法院
    private String court;

    @TableField("COURTLINK")//法院联系人
    private String courtlink;

    @TableField("COURTCONTACT")//法院联系方式
    private String courtcontact;

    @TableField("FILINGTIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date filingtime;

    @TableField("PAYMENTREMINDTIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date paymentremindtime;//缴费提醒时间

    @TableField("OPENINGTIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date openingtime;//开庭时间：

    @TableField("JUDGETIEM")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date judgetiem;

    @TableField("ISEXTERNALLAWYER")
    private BigDecimal isexternallawyer;//是否聘请外师：1是，2否
    
    @TableField("STATE")
    private Integer state;//审批状态字段"状态0-未审批、1-审批中、2-需调整、3-已通过、4-已终止、5-已跟踪、6-已完成"

    @TableField("LAWYEARWORD")
    private String lawyearword;//律所

    @TableField("LAWYEARNAME")//律所
    private String lawyearname;

    @TableField("LAWYEARLINK")//律师联系方式
    private String lawyearlink;

    @TableField("LITIGATIONINFO")
    private BigDecimal litigationinfo;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")//录入时间
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("NEGOTIATOR")
    private String negotiator;//代理人

    @TableField("NEGOTIATORLINK")
    private String negotiatorlink;//代理人联系方式

    @TableField("PRESEDINGJUDGE")
    private String presedingjudge;

    @TableField("CASEPROMOTION")//发现的问题
    private String casepromotion;

    @TableField("EXISTINGDIFFICULTIES")
    private String existingdifficulties;//存在的困难

    @TableField("MEASURESPROMOTE")//推进的措施
    private String measurespromote;


    //TblStaff
    private String username;//用户名（登录名）录入人
    
    private BigDecimal staffid;//主键ID,自动增长
    
    private String realname;//真实名字
    
    private String fixedphone;//固定电话
    
    private String address;//地址
    
    private String email;//邮箱
    
    private String miblephone;//手机号码
    
    private String memo;//备注
    
    private String password;//密码
    
    private BigDecimal jobid;//岗位ID
    
    private Integer status;//状态（1启用，0弃用）
    
    private TblRole  trole;
    
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    
    private String outSideOpenId; //外部同步企业来源Id

    //TblOrganization
    
    private BigDecimal orgid;

    
    @Schema(name = "公司名称")
    private String orgname;

    
    private BigDecimal fatherorgid;

    
    @Schema(name = "公司编号")
    private String orgnumber;

    
    @Schema(name = "公司简介")
    private String orgmeno;


    
    private String icode;

    
    private Integer orgtype;

    
    private Integer auditType;


    
    private String iszy;

    
    private String hyzsktype;

    
    private Integer orderid;

    
    private Integer outsideid;

    
    private String outsideopendid;

    @Schema(name = "是否使用自动编号 0 不使用；1 使用")
    private Integer isautonumber;

    
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date orgcreate;

    
    private BigDecimal isinitialization;

    @Schema(name = "职务")
    
    private String duties;

    @Schema(name = "行业编号")
    
    private BigDecimal industryid;

    @Schema(name = "新增来源于微信 1为微信 0为pc")
    
    private String bywx;

    
    private String datasource;

    
    private String historycode;

    
    private String historydepartmentid;


    
    private BigDecimal litigationid;

    //案件名称。必填
    private String firstcourt;

    //审判长
    private String presidingjudge;

    //合议庭组成
    private String collegialpanel;

    //诉讼金额
    private BigDecimal litigationamount;

    //诉讼受理日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dealdate;

    
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date firsthearingdate;

    
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date litigationenddate;

    //谈判结果
    private String litigationresult;

    
    private BigDecimal litistatus;

    //诉讼标的物
    private String actionobject;

    //判决是否生效：1是，2否
    private BigDecimal iseffect;

    //判决金额
    private BigDecimal judgemoney;

    
    private BigDecimal disputeinfo;

//TblLegalDisputregistration

    
    private BigDecimal disputeid;
    
    private String disputeno;//登记编号
    
    private BigDecimal disputestatus;
    //纠纷类型
    private String disputetype;
    
    private BigDecimal contractinfo;
    //争议焦点
    private String disputecours;
    //是否紧急：1是，其他否
    private BigDecimal isuegent;
    //起诉类型：1起诉，其他被诉
    private BigDecimal whethersued;
    
    private BigDecimal disputeundertaker;
    //办结时间
    @JSONField(format = "yyyy-MM-dd")
    private Date lastdealdate;
    
    private String solutionsuggestions;//初步解决建议
    //原告
    private String plaintiff;
    //被告
    private String defendant;//录入人
    //代理律师
    private String attorney;
    //代理律师联系电话
    private String attorneyphont;
    
    private BigDecimal dispuinfo;
    
    private String disputeitem;//纠纷主题
    //是否聘请律师：1是，2否
    private BigDecimal isattorney;

    @Schema(name = "贵安配合人员")
    private String coordination;

    @Schema(name = "贵安纠纷登记-案由")
    private String casecause;

    @Schema(name = "贵安纠纷登记-涉诉项目")
    private String ssproject;

    @Schema(name = "贵安纠纷登记-管辖法院（仲裁机构）")
    private String courtfirst;
    @Schema(name = "贵安纠纷登记-发生时间")
    private Date occurrencetime;

    @Schema(name = "贵安纠纷登记-涉及子公司")
    private String subsidiaries;
}
