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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CONTRACT_INVOICESMANAGEMEN")
@Schema(name="TblContractInvoicesmanagemen对象")
public class TblContractInvoicesmanagemen implements Serializable {
//发票号
    //发票管理
    private static final long serialVersionUID = 1L;

    @TableId(value = "INVOICEID" , type = IdType.INPUT)
    private BigDecimal invoiceid;

    @TableField("BUDGETID")
    private BigDecimal budgetId;

    @TableField("INVOICENO")//发票号
    private String invoiceno;

    @TableField("INVOICEMONEY")//发票金额
    private BigDecimal invoicemoney;

    @TableField("INVOICEDATE")//发票开具日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date invoicedate;

    @TableField("INVOICEHEADTEXT")//发票抬头
    private String invoiceheadtext;

    @TableField("INVOICEPOST")//税率
    private BigDecimal invoicepost;

    @TableField("INVOICECONTENT")//发票内容
    private String invoicecontent;

    @TableField("INVOICETYPE")//发票类型：增值税专用发票、增值税普通发票
    private String invoicetype;

    @TableField("INVOICESTATUS")//发票状态1-未开票，2-已开票，3-未收款，4-已收款，5-已退票
    private BigDecimal invoicestatus;

    @TableField("INVOICESPDATE")//收票日期
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date invoicespdate;

    @TableField("INVOICESPORG")//收票单位
    private String invoicesporg;

    @TableField("INVOICEKPORG")//开票单位
    private String invoicekporg;

    @TableField("INVOICEOGR")
    private BigDecimal invoiceogr;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("TINUMBER")
    private String tinumber;//纳税人识别号

    @TableField("OUTSIDEID")
    private String outsideid;

//    @TableField("TINUMBER")
//    private String taiNumber;
    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblOrganization invoiceOrg;

    //无税金额
    @TableField(exist = false)
    private String wushuijine;

    //税额
    @TableField(exist = false)
    private String shuie;

    //票据向对方
    @TableField(exist = false)
    private String budgetName;

    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblCyhwProjectbudget budget;

    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblStaff create;

    //映射TblStaff
    @TableField(exist = false , select = false , fill = FieldFill.DEFAULT)
    private TblRole  trole;
    @TableField(exist = false)
    private BigDecimal staffid;//主键ID
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
    private Date createDate;
    @TableField(exist = false)
    private Integer status;//状态（1启用，0弃用）
    @TableField(exist = false)
    private BigDecimal orgid;//组织Id
    @TableField(exist = false)
    private Integer outSideId; //标识用户来源 为null是本系统，1：蜂信，2,蜂信购买后的用户   3：华博云系统注册用户管理员  以后可能为2,3...来表示其它来源
    @TableField(exist = false)
    private String outSideOpenId; //外部同步企业来源Id
}
