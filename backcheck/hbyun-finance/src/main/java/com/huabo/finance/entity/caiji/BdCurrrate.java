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
 * 日汇率
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_CURRRATE")
@Schema(name="BdCurrrate对象", description="日汇率")
public class BdCurrrate implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "日汇率主键")
      @TableField("PK_CURRRATE")
    private String pkCurrrate;

      @Schema(name = "汇率日期")
      @TableField("RATEDATE")
    private Date ratedate;

      @Schema(name = "中间价")
      @TableField("RATE")
    private String rate;

      @Schema(name = "分布式 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "买入价")
      @TableField("BUYRATE")
    private String buyrate;

      @Schema(name = "卖出价")
      @TableField("SELLRATE")
    private String sellrate;

      @Schema(name = "币种信息主键")
      @TableField("PK_CURRINFO")
    private String pkCurrinfo;


}
