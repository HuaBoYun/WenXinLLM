package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoParam {

	@Schema(name = "创建人ID")
	private BigDecimal creator;

	@Schema(name = "创建人名称")
	private String creatorName;

	@Schema(name = "工作单位ID")
	private Integer workUnit;

	@Schema(name = "所属集团ID")
	private Integer belongGroup;

	// 手动添加构造器以支持 Lombok 不生成的情况
	public UserInfoParam(BigDecimal creator, String creatorName, int workUnit, int belongGroup) {
		this.creator = creator;
		this.creatorName = creatorName;
		this.workUnit = workUnit;
		this.belongGroup = belongGroup;
	}

}
