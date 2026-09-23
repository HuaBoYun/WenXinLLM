package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证类别
 * </p>
 *
 * @author L
 * @since 2025-03-17
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_VOUCHERTYPE")
@Schema(name="BdVouchertype对象", description="凭证类别")
public class BdVouchertype implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "凭证类别编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private BigDecimal creator;

      @Schema(name = "数据来源 -2系统同步 ;0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private Integer dataoriginflag;

      @Schema(name = "凭证类别描述")
      @TableField("DESCRIPTION")
    private String description;

      @Schema(name = "启用状态 1=未启用;2=已启用;3=已停用;")
      @TableField("ENABLESTATE")
    private Integer enablestate;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedtime;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private BigDecimal modifier;

      @Schema(name = "凭证类别名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "默认币种")
      @TableField("PK_CURRTYPE")
    private String pkCurrtype;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "凭证类别主键")
      @TableId("PK_VOUCHERTYPE")
    private String pkVouchertype;

      @Schema(name = "凭证类别简称")
      @TableField("SHORTNAME")
    private String shortname;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "所属财务账套")
      @TableField("FINACCOUNT")
    private String finaccount;


}
