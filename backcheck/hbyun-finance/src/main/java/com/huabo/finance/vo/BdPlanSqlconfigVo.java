package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="sql配置请求参数")
public class BdPlanSqlconfigVo extends BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	 @Schema(name = "sql标题")
    private String fname;
	 
	 @Schema(name = "关联方案")
    private String fplanid;
}
