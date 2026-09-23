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
 * 平均汇率
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_AVGRATE")
@Schema(name="BdAvgrate对象", description="平均汇率")
public class BdAvgrate implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "平均汇率主键")
      @TableField("PK_AVGRATE")
    private String pkAvgrate;

      @Schema(name = "会计月平均汇率 0=(偏移各期间汇率之和+当前期间汇率)/(偏移期间个数+1);1=季度内各期间汇率之和/季度内期间个数;2=半年内各期间汇率之和/半年内期间个数;3=年内各期间汇率之和/年内期间个数")
      @TableField("AVGRATE_MONTH")
    private String avgrateMonth;

      @Schema(name = "偏移月")
      @TableField("OFFSETMONTH")
    private String offsetmonth;

      @Schema(name = "会计季度平均汇率 0=(偏移各期间汇率之和+当前期间汇率)/(偏移期间个数+1);1=季度内各期间汇率之和/季度内期间个数;2=半年内各期间汇率之和/半年内期间个数;3=年内各期间汇率之和/年内期间个数;")
      @TableField("AVGRATE_QUARTER")
    private String avgrateQuarter;

      @Schema(name = "会计半年平均汇率 0=(偏移各期间汇率之和+当前期间汇率)/(偏移期间个数+1);1=季度内各期间汇率之和/季度内期间个数;2=半年内各期间汇率之和/半年内期间个数;3=年内各期间汇率之和/年内期间个数 ")
      @TableField("AVGRATE_HALFYEAR")
    private String avgrateHalfyear;

      @Schema(name = "会计年平均汇率 0=(偏移各期间汇率之和+当前期间汇率)/(偏移期间个数+1);1=季度内各期间汇率之和/季度内期间个数;2=半年内各期间汇率之和/半年内期间个数;3=年内各期间汇率之和/年内期间个数;")
      @TableField("AVGRATE_YEAR")
    private String avgrateYear;

      @Schema(name = "币种信息主键")
      @TableField("PK_CURRINFO")
    private String pkCurrinfo;


}
