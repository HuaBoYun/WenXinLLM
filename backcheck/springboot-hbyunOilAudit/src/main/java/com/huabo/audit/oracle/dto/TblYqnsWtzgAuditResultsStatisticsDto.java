package com.huabo.audit.oracle.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Schema(name="问题整改-审计成果统计")
public class TblYqnsWtzgAuditResultsStatisticsDto {
	



	@Schema(name = "筛选年度")
	@Transient
	@TableField(exist = false)
	private Integer queryYear;


	@Schema(name = "项目id")
	@Transient
	@TableField(value = "PROJECTID")
	private String projectId;

	@Schema(name = "项目名称")
	@Transient
	@TableField(value = "PROJECTNAME")
	private String projectName;

	@Schema(name = "问题个数")
	@Transient
	@TableField(value = "NUMBERCOUNT")
	private String numberCount;

	@Schema(name = "审计发现问题金额")
	@Transient
	@TableField(value = "SUMMONEY")
	private String sumMoney;


	@Schema(name = "直接经济成果数")
	@Transient
	@TableField(value = "SUMZJJJCGJE")
	private String sumZjjjcgje;


	@Schema(name = "移送处理人员数量")
	@Transient
	@TableField(value = "SUMYSCLR")
	private String sumYsclr;





}
