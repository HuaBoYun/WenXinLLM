package com.huabo.legal.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblFwglLeaderXfQueryParam extends PageableParam implements Serializable {

	@Schema(name = "类型：1-中心组学习 2-培训研讨 3-普法宣传 4-其他", required = true)
	private Integer leaderType;

	@Schema(name = "主题")
	private String topic;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "开始时间")
	private Date startTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(name = "结束时间")
	private Date endTime;

	@Schema(name="创建人",hidden=true)
	private String creator;

	@Schema(name="工作单位",hidden=true)
	private String workUnit;

	@Schema(name="所属集团",hidden=true)
	private String belongGroup;

}
