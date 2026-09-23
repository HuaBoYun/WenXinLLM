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
 * 会计季度
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCPERIODQUART")
@Schema(name="BdAccperiodquart对象", description="会计季度")
public class BdAccperiodquart implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "会计季度主键")
      @TableField("PK_ACCPERIODQUART")
    private String pkAccperiodquart;

      @Schema(name = "所属组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "季度")
      @TableField("QUARTER")
    private String quarter;

      @Schema(name = "开始月份")
      @TableField("BEGINMONTH")
    private String beginmonth;

      @Schema(name = "结束月份")
      @TableField("ENDMONTH")
    private String endmonth;

      @Schema(name = "会计期间方案")
      @TableField("PK_ACCPERIODSCHEME")
    private String pkAccperiodscheme;

      @Schema(name = "所属会计期间")
      @TableField("PK_ACCPERIOD")
    private String pkAccperiod;

      @Schema(name = "年季度组合")
      @TableField("YEARQUARTER")
    private String yearquarter;

      @Schema(name = "数据来源 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
