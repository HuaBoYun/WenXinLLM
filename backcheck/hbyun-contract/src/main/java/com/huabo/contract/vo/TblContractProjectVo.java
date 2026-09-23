package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="立项管理列表查询入参")
public class TblContractProjectVo extends BaseVo{
	
	@Schema(name = "项目名称")
    private String projectname;
	
	@Schema(name = "项目编码")
    private String projectcode;

//    @Schema(name = "报告开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private String startDate;
//    
//    @Schema(name = "报告结束时间 ")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private String endDate;
}
