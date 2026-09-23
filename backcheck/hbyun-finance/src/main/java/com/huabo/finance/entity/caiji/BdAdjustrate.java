package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 期间汇率
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ADJUSTRATE")
@Schema(name="BdAdjustrate对象", description="期间汇率")
public class BdAdjustrate implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "期间汇率主键")
      @TableField("PK_ADJUSTRATE")
    private String pkAdjustrate;

      @Schema(name = "期间汇率")
      @TableField("ADJUSTRATE")
    private String adjustrate;

      @Schema(name = "会计月份主键")
      @TableField("PK_ACCPERIODMONTH")
    private String pkAccperiodmonth;

      @Schema(name = "会计期间主键")
      @TableField("PK_ACCPERIOD")
    private String pkAccperiod;

      @Schema(name = "会计期间方案主键")
      @TableField("PK_ACCPERIODSCHEME")
    private String pkAccperiodscheme;

      @Schema(name = "会计月份")
      @TableField("RATEMONTH")
    private String ratemonth;

      @Schema(name = "分布式 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "币种信息主键")
      @TableField("PK_CURRINFO")
    private String pkCurrinfo;


}
