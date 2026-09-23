package com.huabo.finance.vo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证余额
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
@Schema(name="GlBalance对象", description="凭证余额")
public class GlBalanceVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "所属组织")
    private String pkOrg;

      @Schema(name = "科目主键")
    private String pkAccasoa;

      @Schema(name = "会计年度")
    private String year;

      @Schema(name = "会计期间")
    private String period;

      
      @Schema(name = "本期期初余额 - 最小值")
    private BigDecimal minfbeginBalanceLocal;

      
      @Schema(name = "本期期初余额 - 最大值")
      private BigDecimal maxfbeginBalanceLocal;

      
      @Schema(name = "本期期末余额-最小值")
    private BigDecimal minfendBalanceLocal;
      
      @Schema(name = "本期期末余额-最大值")
      private BigDecimal maxfendBalanceLocal;
      
      
      @Schema(name = "本币贷发生额 - 最小值")
    private BigDecimal minlocalcreditamount;
      
      @Schema(name = "本币贷发生额 - 最大值")
    private BigDecimal maxlocalcreditamount;
      
      @Schema(name = "本币借发生额 - 最小值")
    private BigDecimal minlocaldebitamount;
      
      @Schema(name = "本币借发生额 - 最大值")
    private BigDecimal maxlocaldebitamount;
      
}
