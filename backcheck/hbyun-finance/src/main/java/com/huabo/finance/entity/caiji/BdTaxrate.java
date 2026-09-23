package com.huabo.finance.entity.caiji;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 增值税税码税率
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_TAXRATE")
@Schema(name="BdTaxrate对象", description="增值税税码税率")
public class BdTaxrate implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "税率主键")
      @TableField("PK_TAXRATE")
    private String pkTaxrate;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "税码主键")
      @TableField("PK_TAXCODE")
    private String pkTaxcode;

      @Schema(name = "扣税类别 1=应税外加;0=应税内含; 默认1")
      @TableField("TAXTYPE")
    private String taxtype;

      @Schema(name = "税率")
      @TableField("TAXRATE")
    private BigDecimal taxrate;

      @Schema(name = "不可抵扣税率")
      @TableField("REALTAXRATE")
    private BigDecimal realtaxrate;

      @Schema(name = "起始日期")
      @TableField("BEGINDATE")
    private Date begindate;

      @Schema(name = "终止日期")
      @TableField("ENDDATE")
    private Date enddate;

      @Schema(name = "备注")
      @TableField("NOTE")
    private String note;

      @Schema(name = "分布式 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
