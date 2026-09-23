package com.huabo.audit.oracle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="工作日志列表查询入参")
public class TblNbsjWorkReportVo extends BaseVo{
	@Schema(name = "日志名称")
    private String reportname;

    @Schema(name = "报告类型")
    private String reporttype;
    
	@Schema(name="项目id",hidden=true)
	private BigDecimal projectId;

    @Schema(name = "开始时间yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String starttime;
    
    @Schema(name = "结束时间yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endtime;
    
}
