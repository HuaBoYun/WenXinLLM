package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Schema(name="工程项目造价表-建设单位统计表-查看详情")
public class TblYqnsGcxmzjJsdwStatisticalToOneDto {
	



	@Schema(name = "筛选年度")
	@Transient
	@TableField(exist = false)
	private Integer queryYear;


	@Schema(name = "GCXMZJID")
	@Transient
	@TableField(value = "GCXMZJID")
	private String gcxmZjId;


	@Schema(name = "合同编号")
	@Transient
	@TableField(value = "HTBH")
	private String htbh;

	@Schema(name = "工程名称")
	@Transient
	@TableField(value = "GCMC")
	private String gcmc;

	@Schema(name = "施工单位")
	@Transient
	@TableField(value = "SGDW")
	private String sgdw;

	@Schema(name = "建设单位")
	@Transient
	@TableField(value = "JSDW")
	private String jsdw;

	@Schema(name = "内外部")
	@Transient
	@TableField(value = "NWB")
	private String nwb;

	@Schema(name = "额度")
	@Transient
	@TableField(value = "EDJE")
	private String edje;

	@Schema(name = "二审审查金额(万元)")
	@Transient
	@TableField(value = "ESSCJE")
	private String esscje;



}
