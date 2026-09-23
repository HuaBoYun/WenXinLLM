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
    @TableName("TBL_CONTRACT_LEND")

@Schema(name="TblContractLend对象")
public class TblContractLend implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public final static String HTJY_TYPE="合同借阅";
    public final static String HTJY_URL="/contract/yszc/to_sphtjy_info?lendid=";//借阅审批页
    public static final String htjyProcessName="HTJY";
    public final static Integer STATE_SP = 1;//审批中
    public final static Integer STATE_TZ = 2;//调整
    public final static Integer STATE_TG = 3;//已通过
    public final static Integer STATE_ZZ = 4;//已终止
    public final static Integer STATE_GZ = 5;//已跟踪
    public final static Integer STATE_WC = 6;//已完成
    
      @TableId(value = "LENDID",type=IdType.INPUT)
      private BigDecimal lendid;

    @TableField("LENDDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern ="yyyy-MM-dd" )
    private Date lenddate;

    @TableField("RETURNDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern ="yyyy-MM-dd" )
    private Date returndate;

    @TableField("LENDSTATUS")
    private Integer lendstatus;

    @TableField("MEMO")
    private String memo;

    @TableField("USERID")
    private BigDecimal userid;

    @TableField("CONTRACTID")
    private BigDecimal contractid;

    private String realname;
    private String contractname;
    private String contractno;
    private String contracttype;



}
