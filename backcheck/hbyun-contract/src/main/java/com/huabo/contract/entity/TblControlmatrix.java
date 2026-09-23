package com.huabo.contract.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-16
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_CONTROLMATRIX")
@Schema(name="TblControlmatrix对象")
public class TblControlmatrix implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "CONMATID",type = IdType.INPUT)
      private BigDecimal conmatid;

    @TableField("FLOWNAME")
    private String flowname;

    @TableField("CONTROLTARGET")
    private String controltarget;

    @TableField("CONTROLNUMBER")
    private String controlnumber;

    @TableField("CONTROLNAME")
    private String controlname;

    @TableField("CONTROLMANAGER")
    private String controlmanager;

    @TableField("CONTROLFREQUENCY")
    private String controlfrequency;

    @TableField("CONTROLTYPE")
    private String controltype;

    @TableField("CONTROLMETHOD")
    private String controlmethod;

    @TableField("MEMO")
    private String memo;

    @TableField("CONTROLDES")
    private String controldes;

    @TableField("INSIDECONTROLTARGET")
    private String insidecontroltarget;

    @TableField("KEYCONTROL")
    private String keycontrol;

    @TableField("EFFECTIVE")
    private String effective;

    @TableField("CONTROLTEST")
    private String controltest;

    @TableField("FINANCIALREPORTIDENTIFY")
    private String financialreportidentify;

    @TableField("RELATEDDEPART")
    private String relateddepart;

    @TableField("CONTROLDOCUMENT")
    private String controldocument;

    @TableField("FLOWCODE")
    private String flowcode;

    @TableField("TOPLEVELFLOWCAT")
    private String toplevelflowcat;

    @TableField("VERSION")
    private String version;

    @TableField("CREATEDTIME")
    private String createdtime;

    @TableField("LASTMODIFIEDTIME")
    private String lastmodifiedtime;

    @TableField("SUBSYSTEM")
    private String subsystem;

    @TableField("CONKZCS")
    private String conkzcs;

    @TableField("VERSIONTYPE")
    private BigDecimal versiontype;


}
