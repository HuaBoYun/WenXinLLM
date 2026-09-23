package com.huabo.system.entity;


import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 资产负债结果表 TBL_CUBE_BBMX
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CUBE_BBMX")
@Schema(name="TBL_CUBE_BBMX", description="")
public class TblCubeBbmx {

//    @TableId(value="TCBID",type = IdType.INPUT)
    @TableField("TCBID")
    @Schema(name= "主键")
    private BigDecimal tcbId;

    @TableField("CODEBMMEATYPE")
    @Schema(name= "介质类型")
    private String codebmmeatype;

    @TableField("CODEENTITY")
    @Schema(name= "实体代码")
    private String codeentity;

    @TableField("CODEVERSION")
    @Schema(name= "版本")
    private String codeversion;

    @TableField("CODEMEASURE")
    @Schema(name= "CODE_MEASURE IN (BB01111,BB1048)")
    private String codemeasure;

    @TableField("CODEMVTYPE")
    @Schema(name= "CODE_MVTYPE=Actual")
    private String codemvtype;

    @TableField("CODEBMTRAIL")
    @Schema(name= "CODE_BM_TRAIL=OriginalSingle")
    private String codebmtrail;

    @TableField("PKACCP")
    @Schema(name= "ACCP主键  用于查询集合的排序")
    private BigDecimal pkaccp;

    @TableField("PKACCM")
    @Schema(name= "ACCM主键  用于查询集合的排序")
    private BigDecimal pkaccm;

    @TableField("VALUE")
    @Schema(name= "数值")
    private Float value;

    @TableField("FZHJ")
    @Schema(name= "负债合计")
    private Float fzhj;

    @TableField("ZCHJ")
    @Schema(name= "资产合计")
    private Float zchj;


}
