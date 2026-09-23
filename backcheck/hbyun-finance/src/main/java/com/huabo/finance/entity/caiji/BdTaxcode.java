package com.huabo.finance.entity.caiji;

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
    @TableName("BD_TAXCODE")
@Schema(name="BdTaxcode对象", description="增值税税码税率")
public class BdTaxcode implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "税码主键")
      @TableField("PK_TAXCODE")
    private String pkTaxcode;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "报税国")
      @TableField("REPTAXCOUNTRY")
    private String reptaxcountry;

      @Schema(name = "税码")
      @TableField("CODE")
    private String code;

    @TableField("DESCRIPTION")
    private String description;

      @Schema(name = "购销类型 默认5 1=国内销售;2=国内采购;3=出口;4=进口;5=不区分;")
      @TableField("PURSALETYPE")
    private String pursaletype;

      @Schema(name = "物料税类")
      @TableField("MATTAXES")
    private String mattaxes;

      @Schema(name = "发货国家")
      @TableField("SUPCOUNTRY")
    private String supcountry;

      @Schema(name = "供应商税类")
      @TableField("SUPTAXES")
    private String suptaxes;

      @Schema(name = "收货国家")
      @TableField("CUSCOUNTRY")
    private String cuscountry;

      @Schema(name = "客户税类")
      @TableField("CUSTAXES")
    private String custaxes;

      @Schema(name = "客户是否有VAT注册码 1=是;2=否;3=不区分;")
      @TableField("ISCUSVAT")
    private String iscusvat;

      @Schema(name = "是否三角贸易 1=是;2=否;3=不区分;")
      @TableField("ISTRIANGLETRADE")
    private String istriangletrade;

      @Schema(name = "税率信息")
      @TableField("TAXRATE")
    private String taxrate;

      @Schema(name = "启用状态 	1=未启用;2=已启用;3=已停用;")
      @TableField("ENABLESTATE")
    private String enablestate;

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


}
