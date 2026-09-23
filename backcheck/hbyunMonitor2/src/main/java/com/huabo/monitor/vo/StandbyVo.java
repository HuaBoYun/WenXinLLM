package com.huabo.monitor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandbyVo {

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name="备用时间1 格式yyyy-MM-dd")
	private Date standbyTime1;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name="备用时间2 格式yyyy-MM-dd")
	private Date standbyTime2;

	@Schema(name="备用字符串1")
	private String standbyString1;

	@Schema(name="备用字符串2")
	private String standbyString2;

	@Schema(name="备用字符串3")
	private String standbyString3;

	@Schema(name="备用字符串4")
	private String standbyString4;

	@Schema(name="备用字符串5")
	private String standbyString5;

	@Schema(name="备用字符串6")
	private String standbyString6;

	@Schema(name="备用大字符串1")
	private String standbyBigString1;

	@Schema(name="备用大字符串2")
	private String standbyBigString2;
}
