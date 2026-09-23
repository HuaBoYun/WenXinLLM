package com.huabo.finance.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Schema(name="GlDetail对象", description="凭证明细")
public class GlDetailVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

	  @Schema(name = "核算账簿")
	private String pkAccountingbook;
	
	  @Schema(name = "财务组织")
	private String pkOrg;
	
	  @Schema(name = "凭证主键")
	private String pkVoucher;
	
	  @Schema(name = "会计科目")
	private String pkAccasoa;
	
	  @Schema(name = "摘要内容")
	private String explanation;
	
	  @Schema(name = "原币借发生额 -最小值")
	private BigDecimal mindebitamount;
	  
	  @Schema(name = "原币借发生额 -最大值")
	  private BigDecimal maxdebitamount;
	
	  @Schema(name = "原币贷发生额-最小值")
	private BigDecimal mincreditamount;
	  
	  @Schema(name = "原币贷发生额-最大值")
	private BigDecimal maxcreditamount;
	
	  @Schema(name = "会计年度-最小值")
	private String minyearv;
	  
	  @Schema(name = "会计年度-最大值")
	private String maxyearv;
	
	  @Schema(name = "会计期间-最小值")
	private String minperiodv;
	  
	  @Schema(name = "会计期间-最大值")
	private String maxperiodv;
	
	  @Schema(name = "凭证编码")
	private Integer nov;
	
	  @Schema(name = "所属采集方案")
	private String fplanid;
	
	  @Schema(name = "数据来源 -2系统同步 ")
	private String dataoriginflag;
	
	  @Schema(name = "记账人姓名")
	private String managervname;
	  
	  @Schema(name = "制单日期-最小值")
      @JsonFormat(pattern="yyyy-MM-dd")
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minprepareddatev;
	  
	  
	  @Schema(name = "制单日期-最大值")
      @JsonFormat(pattern="yyyy-MM-dd")
	  @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxprepareddatev;
	  
	  @Schema(name = "凭证号")
	  private String gvnum;

}
