package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 账簿类型
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ORG_SETOFBOOK")
@Schema(name="OrgSetofbook对象", description="账簿类型")
public class OrgSetofbook implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "账簿类型主键")
        @TableId("PK_SETOFBOOK")
      private String pkSetofbook;

      @Schema(name = "编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "简称")
      @TableField("SHORTNAME")
    private String shortname;

      @Schema(name = "助记码")
      @TableField("MNECODE")
    private String mnecode;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "科目体系")
      @TableField("PK_ACCSYSTEM")
    private String pkAccsystem;

      @Schema(name = "本位币")
      @TableField("PK_STANDARDCURR")
    private String pkStandardcurr;

      @Schema(name = "会计期间方案")
      @TableField("PK_ACCPERIODSCHEME")
    private String pkAccperiodscheme;

      @Schema(name = "分布式  	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "财务核算账簿")
      @TableField("ISACCOUNTBOOK")
    private String isaccountbook;

      @Schema(name = "责任核算账簿")
      @TableField("ISLIABILITYBOOK")
    private String isliabilitybook;

      @Schema(name = "核算要素体系")
      @TableField("PK_CHECKELEMSYSTEM")
    private String pkCheckelemsystem;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "核算目的")
      @TableField("CHECKAIM")
    private String checkaim;

      @Schema(name = "外币汇率方案")
      @TableField("PK_EXRATESCHEME")
    private String pkExratescheme;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "最后修改时间")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;


}
