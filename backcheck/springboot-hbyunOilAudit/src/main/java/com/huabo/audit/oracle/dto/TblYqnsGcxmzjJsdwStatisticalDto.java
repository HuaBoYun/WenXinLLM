package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Schema(name="工程项目造价表-建设单位统计表")
public class TblYqnsGcxmzjJsdwStatisticalDto {
	


	/**
	 * 建设单位
	 */
	@Schema(name = "建设单位")
	@Transient
	@TableField(value = "JSDW")
	private String jsdw;
	
	@Schema(name = "合同编号统计-项目个数")
	@Transient
	@TableField(value = "HTBHCOUNT")
	private String htbhCount;


	@Schema(name = "额度金额-结算金额合计")
	@Transient
	@TableField(value = "ESSCJESUM")
	private String esscjeSum;
	
	@Schema(name = "筛选年度")
	@Transient
	@TableField(exist = false)
	private Integer queryYear;


}
