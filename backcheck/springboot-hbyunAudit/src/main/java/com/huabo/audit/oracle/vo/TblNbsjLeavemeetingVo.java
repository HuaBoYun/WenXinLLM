package com.huabo.audit.oracle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="进场纪要列表查询入参")
public class TblNbsjLeavemeetingVo extends BaseVo{
	@Schema(name = "离场纪要编号")
    private String leavecoed;

    @Schema(name = "离场纪要名称")
    private String leavename;

    @Schema(name = "离场纪要开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "离场纪要结束时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;
    
    @Schema(hidden=true)
    private BigDecimal progectid;
}