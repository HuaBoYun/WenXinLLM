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
 * @since 2022-04-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CYHW_BASICUNINSPECTION")
@Schema(name="TblCyhwBasicuninspection对象")
public class TblCyhwBasicuninspection implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "INSPECTIONID",type=IdType.INPUT)
      private BigDecimal inspectionid;

    @TableField("INSPECTIONNAME")
    private String inspectionname;

    @TableField("INSPECTIONFUNC")
    private String inspectionfunc;

    @TableField("FILENAME")
    private String filename;

    @TableField("FILENO")
    private String fileno;

    @TableField("INSPECTIONMONEY")
    private BigDecimal inspectionmoney;

    @TableField("INSPECTIONDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date inspectiondate;

    @TableField("ORGID")
    private BigDecimal orgid;

    @TableField("CREATEUSER")
    private BigDecimal createuser;

    @TableField("CREATETIME")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    @TableField("FLOWID")
    private BigDecimal flowid;

    @TableField("LINKDEPR")
    private BigDecimal linkdepr;

    @TableField("INSPECTIONSTATUS")
    private BigDecimal inspectionstatus;

    @TableField("INSPECTIONDEPT")
    private BigDecimal inspectiondept;


}
