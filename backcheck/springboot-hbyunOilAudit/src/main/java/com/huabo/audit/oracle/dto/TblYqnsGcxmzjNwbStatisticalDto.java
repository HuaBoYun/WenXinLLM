package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Schema(name="工程项目造价表-内外部-统计表")
public class TblYqnsGcxmzjNwbStatisticalDto {
	


	/**
	 * 建设单位
	 */
	@Schema(name = "建设单位")
	@Transient
	@TableField(value = "JSDW")
	private String jsdw;
	
	@Schema(name = "合计个数")
	@Transient
	@TableField(value = "HTBHCOUNT")
	private String htbhCount;


	@Schema(name = "合计金额合计")
	@Transient
	@TableField(value = "EDJESUM")
	private String edjeSum;



	@Schema(name = "抽审合计个数")
	@Transient
	@TableField(value = "ESSCJECOUNT")
	private String esscjeCount;


	@Schema(name = "抽审合计金额合计")
	@Transient
	@TableField(value = "ESSCJESUM")
	private String esscjeSum;

	@Schema(name = "外部单位合计个数")
	@Transient
	@TableField(value = "EXTERNALUNITCOUNT")
	private String externalunitCount;


	@Schema(name = "外部单位金额合计")
	@Transient
	@TableField(value = "EXTERNALUNITSUM")
	private String externalunitSum;


	@Schema(name = "内部单位合计个数")
	@Transient
	@TableField(value = "TENGFEIORINTERNALCOUNT")
	private String tengfeIorinternalCount;

	@Schema(name = "内部单位金额合计")
	@Transient
	@TableField(value = "TENGFEIORINTERNALSUM")
	private String tengfeIorinternalSum;


	@Schema(name = "内部多径单位个数")
	@Transient
	@TableField(value = "INTERNALMULTIPATHCOUNT")
	private String internalmultipathCount;


	@Schema(name = "内部多径单位合计")
	@Transient
	@TableField(value = "INTERNALMULTIPATHSUM")
	private String internalmultipathSum;


	@Schema(name = "内部腾飞单位个数")
	@Transient
	@TableField(value = "TENGFEICOUNT")
	private String tengfeiCount;


	@Schema(name = "内部腾飞金额合计")
	@Transient
	@TableField(value = "TENGFEISUM")
	private String tengfeiSum;


	@Schema(name = "工程建设公司个数")
	@Transient
	@TableField(value = "ENGINEERINGCONSTRUCTIONCOUNT")
	private String engineeringconstructionCount;


	@Schema(name = "工程建设公司金额合计")
	@Transient
	@TableField(value = "ENGINEERINGCONSTRUCTIONSUM")
	private String engineeringconstructionSum;
	
	@Schema(name = "筛选年度")
	@Transient
	@TableField(exist = false)
	private Integer queryYear;
}
