package com.huabo.cybermonitor.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.cybermonitor.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计报告编制列表查询入参")
public class TblNbkzRiskVo extends BaseVo {
	
	@Schema(name = "风险名称")
    private String riskname;
	
	@Schema(name = "风险编号")
    private String risknumber;

    @Schema(name = "发现开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "发现结束时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;
}
