package com.huabo.system.vo.param;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemHomeAuthorizationQueryParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="系统首页配置ID")
	private Integer homePageId;
}
