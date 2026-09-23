package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @since 2022-03-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_ARBITRATIONRECORD")
@Schema(name="TblLegalArbitrationrecord对象")
public class TblLegalArbitrationrecord implements Serializable {
//法务管理-仲裁过程-新建-仲裁过程信息
    private static final long serialVersionUID = 1L;

      @TableId(value = "ARRECORDID",type=IdType.INPUT)
      private BigDecimal arrecordid;

    @TableField("ARSTAGE")//仲裁阶段
    private String arstage;

    @TableField("ARCONTACTPERSON")//仲裁机构联系人
    private String arcontactperson;

    @TableField("OURCONTRACTPERSON")//我放代理人
    private String ourcontractperson;

    @TableField("COMMUNICATIONMODE")
    private String communicationmode;

    @TableField("ARRECORDMODE")//过程纪要
    private String arrecordmode;

    @TableField("ARRECORDMEMO")//备注
    private String arrecordmemo;

    @TableField("ARBITRATIONINFO")
    private BigDecimal arbitrationinfo;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")//录入时间
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("DEALDATE")//日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dealdate;

      @Schema(name = "贵安配合人员")
      @TableField("ATTORNEY")
    private String attorney;

      @Schema(name = "贵安所属单位")
      @TableField("COMPANY")
    private String company;

      @Schema(name = "贵安联系方式")
      @TableField("PHONE")
    private BigDecimal phone;

    private String createname;


      //TblLegalArbitratsettlement

    private BigDecimal arbitraid;
    private String courtfirst;
    @JSONField(format = "yyyy-MM-dd")//仲裁受理日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date asdealdate;
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date asfirsthearingdate;//仲裁首次开庭日期
    private BigDecimal arbitrationamount;
    @JSONField(format = "yyyy-MM-dd")//仲裁结案日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date arbitrationenddate;
    private String arbitrationresult;
    private BigDecimal negotiateinfo;
    private BigDecimal arbitrastatus;
    @Schema(name = "币种")
    private String currency;
    @Schema(name = "隶属纠纷")
    private BigDecimal disputeid;

    //TblStaff

    private BigDecimal staffid;//主键ID,自动增长
    private String realname;//真实名字
    private String fixedphone;//固定电话
    private String address;//地址
    private String email;//邮箱
    private String miblephone;//手机号码
    private String memo;//备注
    private String username;//用户名（登录名）
    private String password;//密码
    private BigDecimal jobid;//岗位ID
    private Integer status;//状态（1启用，0弃用）
    
    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private TblRole  trole;
    //private TblOrganization tblOrganization;//组织ID
    private BigDecimal orgid;//组织Id
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    private String outSideOpenId; //外部同步企业来源Id
}
