package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="自定义报告编制列表查询入参")
public class TblReportVo extends BaseVo{
	
	@Schema(name = "报告名称")
    private String reportname;

    @Schema(name = "报告开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "报告结束时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;
    
    @Schema(hidden=true)
    private String type;
    
}
