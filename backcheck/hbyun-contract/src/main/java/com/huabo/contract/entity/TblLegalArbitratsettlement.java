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
import java.util.List;

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
 * @since 2022-03-21
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_ARBITRATSETTLEMENT")
@Schema(name="仲裁过程")
public class TblLegalArbitratsettlement implements Serializable {
//法务管理-仲裁过程
    private static final long serialVersionUID = 1L;


      @TableId(value = "ARBITRAID",type=IdType.INPUT)
      private BigDecimal arbitraid;

    @TableField("COURTFIRST")//一审法院新增必填//案件名称
    private String courtfirst;

    @TableField("ASDEALDATE")//
    @JSONField(format = "yyyy-MM-dd")//仲裁受理日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date asdealdate;

    @TableField("ASFIRSTHEARINGDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date asfirsthearingdate;//仲裁首次开庭日期

    @TableField("ARBITRATIONAMOUNT")//仲裁金额
    private BigDecimal arbitrationamount;

    @TableField("ARBITRATIONENDDATE")
    @JSONField(format = "yyyy-MM-dd")//仲裁结案日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date arbitrationenddate;

    @TableField("ARBITRATIONRESULT")//仲裁结果
    private String arbitrationresult;
    
    @TableField("ARBITRAORG")//仲裁机构
    private String arbitraorg;
    
    @TableField("ARBITRALINKMAN")//仲裁机构联系人
    private String arbitralinkman;
    
    @TableField("ENTERINGPERSON")//录入人
    private String enteringperson;
    
    @TableField("ARBITRATIME")//时间
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date arbitratime;
    
    @TableField("ISEXTERNAL")//是否外聘
    private String isexternal;
    
    @TableField("AGENCYPERSON")//代理人
    private String agencyperson;
    
    @TableField("CONTACTPHONE")//联系方式
    private String contactphone;
    

    @TableField("NEGOTIATEINFO")
    private BigDecimal negotiateinfo;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    private Date createtime;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("ARBITRASTATUS")
    private BigDecimal arbitrastatus;

      @Schema(name = "币种")
      @TableField("CURRENCY")
    private String currency;

      @Schema(name = "隶属纠纷")
      @TableField("DISPUTEID")
    private BigDecimal disputeid;
      
      @Schema(name = "隶属纠纷")
      @TableField("DISPUTEITEM")
    private String disputeitem;

      @TableField(exist=false)
    private String startdate;
    @TableField(exist=false)
    private String enddate;

    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private List<TblAttachment> attList;

    @TableField(exist=false,select=false,fill=FieldFill.DEFAULT)
    private TblLegalNegotiatedsettlemen negotiate;

//      @Transient
//    private String disputeitem;//协商信息
}
