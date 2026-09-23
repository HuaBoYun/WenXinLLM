package com.huabo.finance.vo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 凭证库表
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
@Data
  @EqualsAndHashCode(callSuper = false)
@Schema(name="GlVoucher对象", description="凭证库表")
public class GlVoucherVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "凭证号")
    private Integer num;

      @Schema(name = "会计期间 -- 开始")
    private String minperiod;
      
      @Schema(name = "会计期间 -- 结束")
    private String maxperiod;

      @Schema(name = "制单日期 --开始")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minprepareddate;
      
      @Schema(name = "制单日期 --结束")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxprepareddate;

      @Schema(name = "过账日期--开始")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date mintallydate;
      
      @Schema(name = "过账日期--结束")
      @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxtallydate;

      @Schema(name = "年份")
      @TableField("YEAR")
    private String year;
      
      @Schema(name = "组织主键")
    private String pkOrg;
      
      @Schema(name = "科目主键")
      private String pkAccAsoa;

      @Schema(name = "说明")
      private String explanation;
      
      
      @Schema(name = "总借方金额 - 最大值")
      private BigDecimal maxTotaldebit;
      
      @Schema(name = "总借方金额 - 最小值")
      private BigDecimal minTotaldebit;
      
      
      @Schema(name = "总贷方金额 - 最小值")
    private BigDecimal minTotalcredit;
      
      
      @Schema(name = "总贷方金额 - 最大值")
    private BigDecimal maxTotalcredit; 
}
