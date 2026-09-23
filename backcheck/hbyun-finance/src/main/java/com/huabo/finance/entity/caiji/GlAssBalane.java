package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 辅助账余额
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("GL_ASS_BALANE")
@Schema(name="GlAssBalane对象", description="辅助账余额")
public class GlAssBalane implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "辅助凭证主键")
        @TableId("PK_ASSBALANCE")
      private String pkAssbalance;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "所属公司")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "科目主键")
      @TableField("PK_ACCASOA")
    private String pkAccasoa;

      @Schema(name = "会计年度")
      @TableField("YEAR")
    private String year;

      @Schema(name = "会计期间")
      @TableField("PERIOD")
    private String period;

      @Schema(name = "辅助核算标识")
      @TableField("PK_ACCASS")
    private String pkAccass;

      @Schema(name = "原币借发生额")
      @TableField("DEBITAMOUNT")
    private BigDecimal debitamount;

      @Schema(name = "原币贷发生额")
      @TableField("CREDITMOUNT")
    private BigDecimal creditmount;

      @Schema(name = "初始余额")
      @TableField("BEGINBALANCEMOUNT")
    private BigDecimal beginbalancemount;

      @Schema(name = "期末余额")
      @TableField("ENDBALANCEMOUNT")
    private BigDecimal endbalancemount;

      @Schema(name = "本年累计借发生额")
      @TableField("YEARDEBITAMOUNT")
    private BigDecimal yeardebitamount;

      @Schema(name = "本年累计贷发生额")
      @TableField("YEARCREDITMOUNT")
    private BigDecimal yearcreditmount;

      @Schema(name = "数据来源 -2系统同步 ")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
