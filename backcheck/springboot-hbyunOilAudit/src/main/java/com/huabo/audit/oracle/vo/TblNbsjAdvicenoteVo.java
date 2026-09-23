package com.huabo.audit.oracle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="我的底稿列表查询入参")
public class TblNbsjAdvicenoteVo extends BaseVo{
	@Schema(name = "审计通知书编号")
    private String advicecoed;

    @Schema(name = "审计通知书名称")
    private String advicename;
    
    @Schema(name = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "结束时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;
    
    @Schema(name = "项目ID")
    private BigDecimal projectId;
    
}
