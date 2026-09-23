package com.huabo.audit.oracle.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="知识库查询对象")

public class TblOtherarticleVo {
		
	@Schema(name = "文章标题")
	private String articletitle;

	@Schema(name = "文章作者")
	private String aruticleauther;
	
    @Schema(name = "所属行业id")
    private String orgid;
    
}
