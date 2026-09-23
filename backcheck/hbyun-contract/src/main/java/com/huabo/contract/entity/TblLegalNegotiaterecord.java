package com.huabo.contract.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-03-18
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LEGAL_NEGOTIATERECORD")
@Schema(name="TblLegalNegotiaterecord对象")
public class TblLegalNegotiaterecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "RECORDID",type=IdType.INPUT)
      private BigDecimal recordid;

    @TableField("NEGOTIATIONTIME")//谈判时间
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date negotiationtime;

    @TableField("RECORDCOUNTERPART")//对方谈判人
    private String recordcounterpart;

    @TableField("OURNEGOTIATOR")
    private BigDecimal ournegotiator;//我方谈判人

    @TableField("NEGOTIATIONMODE")//谈判方式
    private String negotiationmode;

    @TableField("NEGOTIATIONRECORD")//协商记录
    private String negotiationrecord;

    @TableField("NEGETIATIONMEMOE")//备注
    private String negetiationmemoe;

    @TableField("NEGOTIATEINFO")
    private BigDecimal negotiateinfo;

    @TableField("LINKORG")
    private BigDecimal linkorg;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;//录入人

    @TableField("CREATETIME")//录入时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;

    @TableField("COURTNAME")//法院名称
    private String courtname;

    @TableField("COURTPARTER")//法院承办人
    private String courtparter;

    @TableField("COURTLINK")//法院联系方式
    private String courtlink;

      @Schema(name = "参与人员id")
      @TableField("STAFFIDS")
    private String staffids;

      @Schema(name = "参与人员名字")
      @TableField("STAFFNAMES")
    private String staffnames;


    //我方谈判人  TblStaff-username
    private String zxstaffname;

    //录入人 TblStaff-realname
    private String createname;

    //录入人（小新增）
    private String LRR;
    //我方谈判人（小新增）
    private String WFTPR;
}
