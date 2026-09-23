package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-04-29
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_NBSJ_SHEET")
@Schema(name="TblNbsjSheet对象", description="")
public class TblNbsjSheet implements Serializable {


    public static final Integer STATE1 = 1;
    public static final Integer STATE2 = 2;
    public static final Integer STATE3 = 3;
    public static final Integer STATE4 = 4;
    public static final Integer STATE5 = 5;
    private static final long serialVersionUID = 1L;

      @TableId(value="SHEETID",type = IdType.INPUT)
      private BigDecimal sheetid;

    @TableField("SHEETCODE")
    private String sheetcode;

    @TableField("SHEETNAME")
    private String sheetname;

    @TableField("SHEETTARGET")
    private String sheettarget;

    @TableField("AUDITORG")
    private BigDecimal auditorg;

    @TableField("CREATESTAFF")
    private BigDecimal createstaff;

    @TableField("CREATETIME")
    private LocalDateTime createtime;

    @TableField("PROJECTID")
    private BigDecimal projectid;

    @TableField("UPDATETIME")
    private LocalDateTime updatetime;

    @TableField("RISKATTRBUTION")
    private String riskattrbution;

    @TableField("BUSINESSAFFILIATION")
    private String businessaffiliation;

    @TableField("STATE")
    private Integer state;

    @TableField("AUDITDESC")
    private String auditdesc;

    @TableField("AUDITCOURSE")
    private String auditcourse;

    @TableField("AUDITDISCOVERABLE")
    private String auditdiscoverable;

    @TableField("STATUS")
    private BigDecimal status;

    @TableField("APPROVER")
    private String approver;

    @TableField("RISKLEVEL")
    private String risklevel;

    @TableField("PROGRAMID")
    private BigDecimal programid;

    @TableField("QUESTITLE")
    private String questitle;

    @TableField("COMPTIME")
    private Date comptime;

    @TableField("TARGETNAME")
    private String targetname;

    @TableField("BUSINESSTYPE")
    private String businesstype;

    @TableField("SUDITPROCESS")
    private String suditprocess;

    @TableField("SJBWL")
    private String sjbwl;

    @TableField("TARGETID")
    private BigDecimal targetid;

    @TableField("AUDITSTAFFID")
    private BigDecimal auditstaffid;

    @TableField("BELONGTYPE")
    private BigDecimal belongtype;

    @TableField("DETAILTYPE")
    private BigDecimal detailtype;

    @TableField("NOZGREASION")
    private String nozgreasion;

    @TableField("RELATEDMONEY")
    private BigDecimal relatedmoney;

      @TableField("HGDETAILTYPE")
    private Integer hgdetailtype;

      @TableField("PROCATEGORIES")
    private Integer procategories;

      @TableField("WORKLOAD")
    private BigDecimal workload;

      @TableField("VIOLATPROVID")
    private String violatprovid;

    @TableField("FSID")
    private String fsid;


}
