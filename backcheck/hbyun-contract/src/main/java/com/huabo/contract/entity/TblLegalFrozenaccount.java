package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2022-03-23
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_FROZENACCOUNT")
@Schema(name="TblLegalFrozenaccount对象")
public class TblLegalFrozenaccount implements Serializable {
//账户冻结
    private static final long serialVersionUID = 1L;

      @TableId(value="INFORID",type=IdType.INPUT)
      private BigDecimal inforid;

    @TableField("PROCEEDINFO")
    private BigDecimal proceedinfo;

    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("FROZENBLANK")//被冻结账户开户行：
    private String frozenblank;

    @TableField("FROZENACCOUNT")//被冻结账户账户
    private String frozenaccount;

    @TableField("APPLYAMOUNT")//申请冻结金额（元）
    private BigDecimal applyamount;

    @TableField("FROZENAMOUNT")//实际被冻结金额（元）
    private BigDecimal frozenamount;

    @TableField("KOUHUAAMOUNT")//，被扣划金额（元）
    private BigDecimal kouhuaamount;

    @TableField("ACCOUNTNATURE")//账户性质
    private String accountnature;

    @TableField("STARTDATE")
    @JSONField(format = "yyyy-MM-dd")//冻结起始日
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startdate;

    @TableField("ENDDATE")
    @JSONField(format = "yyyy-MM-dd")//冻结期届满日
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date enddate;

    @TableField("FROZENMEMO")//备注
    private String frozenmemo;
    //对应TBL_STAFF的realname
    private String realname;
    //映射TblLegalProceedingsrecord
    private BigDecimal maxApplyAmount;
    private BigDecimal minFrozenAmount;
    private BigDecimal minKouhuaAmount;



    private String fixedphone;//固定电话
    private String address;//地址
    private String email;//邮箱
    private String miblephone;//手机号码
    private String memo;//备注
    private String username;//用户名（登录名）
    private String password;//密码
    private BigDecimal jobid;//岗位ID
    @JSONField(format = "yyyy-MM-dd")
    private Date createDate;
    private Integer status;//状态（1启用，0弃用）

    private BigDecimal orgid;//组织Id
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    private String outSideOpenId; //外部同步企业来源Id
    private String orgName;
    private String jobName;
    private String orgFatherName;
    private String rn;
    private String checked;


    private BigDecimal proceedid;
    private String proceedno;//案号
    private String porceedstage;//诉讼阶段
    private String court;//审理法院
    private String courtlink;
    private String courtcontact;
    @JSONField(format = "yyyy-MM-dd")
    private Date filingtime;
    @JSONField(format = "yyyy-MM-dd")
    private Date paymentremindtime;
    @JSONField(format = "yyyy-MM-dd")
    private Date openingtime;
    @JSONField(format = "yyyy-MM-dd")
    private Date judgetiem;
    private BigDecimal isexternallawyer;
    private String lawyearword;
    private String lawyearname;
    private String lawyearlink;
    private BigDecimal litigationinfo;
    private String negotiator;
    private String negotiatorlink;
    private String presedingjudge;
    private String casepromotion;
    private String existingdifficulties;
    private String measurespromote;


    //映射TblLegalDisputregistration
    private BigDecimal disputeid;
    private String disputeno;
    private BigDecimal disputestatus;
    private String disputetype;
    private BigDecimal contractinfo;
    private String disputecours;
    private BigDecimal isuegent;
    private BigDecimal whethersued;
    private BigDecimal disputeundertaker;
    private LocalDateTime lastdealdate;
    private String solutionsuggestions;
    private String plaintiff;//原告
    private String defendant;//被告
    private String attorney;
    private String attorneyphont;
    private BigDecimal dispuinfo;
    private String disputeitem;//纠纷主题
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
    private LocalDateTime occurrencetime;
    @Schema(name = "贵安纠纷登记-涉及子公司")
    private String subsidiaries;
}
