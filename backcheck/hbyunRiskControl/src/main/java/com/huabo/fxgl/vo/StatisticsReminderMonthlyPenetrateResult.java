package com.huabo.fxgl.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsReminderMonthlyPenetrateResult {

	@Schema(name="风险编号")
	private String no;

	@Schema(name="风险名称")
	private String name;

	@Schema(name="催办内容")
	private String msg;

	@Schema(name="催办人ID")
	private Long staffId;

	@Schema(name="催办人名称")
	private String staffName;

	@Schema(name="催办时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date time;
}
