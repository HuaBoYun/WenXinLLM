package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Schema(name="TblNbsjSheetMySql对象")
public class TblNbsjSheetMySql implements Serializable {


    public static final Integer STATE1 = 1;
    public static final Integer STATE2 = 2;
    public static final Integer STATE3 = 3;
    public static final Integer STATE4 = 4;
    public static final Integer STATE5 = 5;
    private static final long serialVersionUID = 1L;

    @TableId("SHEETID")
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
    private LocalDateTime comptime;

    @TableField("TARGETNAME")
    private String targetname;

    @TableField("BUSINESSTYPE")
    private String businesstype;

    @TableField("SUDITPROCESS")
    private String suditprocess;

    @TableField("SJBWL")
    private String sjbwl;

    @TableField("TARGETID")
    private Long targetid;

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

    @Schema(name = "合规详细类型")
    @TableField("HGDETAILTYPE")
    private Integer hgdetailtype;

    @Schema(name = "国资委问题类别")
    @TableField("PROCATEGORIES")
    private Integer procategories;

    @Schema(name = "审计工作量")
    @TableField("WORKLOAD")
    private BigDecimal workload;

    @Schema(name = "违反的相关规定")
    @TableField("VIOLATPROVID")
    private String violatprovid;

    @TableField("FSID")
    private String fsid;


}
