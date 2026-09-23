package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

@Data
@Schema(name="工程项目造价表-抽审表(按施工单位及额度)-统计表")
public class TblYqnsGcxmzjSampleStatisticalDto {
	


	/**
	 * 建设单位
	 */
	@Schema(name = "建设单位")
	@Transient
	@TableField(value = "JSDW")
	private String jsdw;

	/**
	 * 内外部
	 */
	@Schema(name = "排序")
	@Transient
	@TableField(value = "SORT")
	private Integer sort;

	/**
	 * 内外部
	 */
	@Schema(name = "内外部(单位类别)")
	@Transient
	@TableField(value = "NWB")
	private String nwb;


	@Schema(name = "抽审比例")
	@Transient
	@TableField(value = "SAMPLINGRATIO")
	private BigDecimal samplingRatio;




	@Schema(name = "抽审合计个数")
	@Transient
	@TableField(value = "ESSCJECOUNT")
	private BigDecimal esscjeCount;


	@Schema(name = "抽审合计金额合计")
	@Transient
	@TableField(value = "ESSCJESUM")
	private BigDecimal esscjeSum;


	@Schema(name = "合计个数")
	@Transient
	@TableField(value = "HTBHCOUNT")
	private BigDecimal htbhCount;


	@Schema(name = "合计金额合计")
	@Transient
	@TableField(value = "EDJESUM")
	private BigDecimal edjeSum;




	@Schema(name = "大于900万个数")
	@Transient
	@TableField(value = "COUNTOVERNINEHUND")
	private BigDecimal countOverNineHund;

	@Schema(name = "大于900万金额")
	@Transient
	@TableField(value = "SUMOVERNINEHUND")
	private BigDecimal sumOverNineHund;

	@Schema(name = "100-900万个数")
	@Transient
	@TableField(value = "COUNTHUNDTONINEHUND")
	private BigDecimal countHundToNineHund;

	@Schema(name = "100-900万金额")
	@Transient
	@TableField(value = "SUMHUNDTONINEHUND")
	private BigDecimal sumHundToNineHund;

	@Schema(name = "50-100万 个数")
	@Transient
	@TableField(value = "COUNTFIFTYTOHUND")
	private BigDecimal countFiftyToHund;

	@Schema(name = "50-100万金额")
	@Transient
	@TableField(value = "SUMFIFTYTOHUND")
	private BigDecimal sumFiftyToHund;

	@Schema(name = "20-100万个数")
	@Transient
	@TableField(value = "COUNTTWENTYTOFIFTY")
	private BigDecimal countTwentyToFifty;

	@Schema(name = "20-50万金额")
	@Transient
	@TableField(value = "SUMTWENTYTOFIFTY")
	private BigDecimal sumTwentyToFifty;

	@Schema(name = "小于20万个数")
	@Transient
	@TableField(value = "COUNTUNDERTWENTY")
	private BigDecimal countUnderTwenty;

	@Schema(name = "小于20万金额")
	@Transient
	@TableField(value = "SUMUNDERTWENTY")
	private BigDecimal sumUnderTwenty;
	
	@Schema(name = "筛选年度")
	@Transient
	@TableField(exist = false)
	private Integer queryYear;

}
