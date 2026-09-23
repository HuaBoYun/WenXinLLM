package com.huabo.finance.vr;

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
@Schema(name="GlAssBalane对象", description="辅助账余额")
public class GlAssBalaneVr implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "辅助凭证主键")
      private String pkAssbalance;

      @Schema(name = "所属集团")
    private String pkGroup;

      @Schema(name = "所属公司 --必传")
    private String pkOrg;

      @Schema(name = "科目主键")
    private String pkAccasoa;

      @Schema(name = "会计年度")
    private String year;

      @Schema(name = "会计期间")
    private String period;

      @Schema(name = "辅助核算标识")
    private String pkAccass;

      @Schema(name = "原币借发生额")
    private BigDecimal debitamount;

      @Schema(name = "原币贷发生额")
    private BigDecimal creditmount;

      @Schema(name = "初始余额")
    private BigDecimal beginbalancemount;

      @Schema(name = "期末余额")
    private BigDecimal endbalancemount;

      @Schema(name = "本年累计借发生额")
    private BigDecimal yeardebitamount;

      @Schema(name = "本年累计贷发生额")
    private BigDecimal yearcreditmount;

      @Schema(name = "数据来源 -2系统同步 ")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
    private String fplanid;
      
      
      @Schema(name = "辅助类型")
    private String asstype;

      @Schema(name = "辅助信息主键")
    private String pkBunessies;

      @Schema(name = "辅助名称")
    private String assname;

      @Schema(name = "描述")
    private String assdes;

    private String asslevel;

      @Schema(name = "辅助核算项主键  -- 必传")
    private String pkAccassitem;

      @Schema(name = "描述")
    private String assdd;
      
      @Schema(name = "科目编号")
    private String code;
    
      @Schema(name = "科目名称")
    private String name;
    
      @Schema(name = "科目方向  0=借方;1=贷方;")
    private Integer balanorient;
      
      
      @Schema(name = "期末方向  0=借方;1=贷方;")
      private Integer endBalanorient;
      
      
      @Schema(name = "期初方向  0=借方;1=贷方;")
      private Integer beginBalanorient;
      
}
