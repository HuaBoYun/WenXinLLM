package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审计发现列表查询入参")
public class TblNbsjQuestionVo extends BaseVo{
	@Schema(name = "审计事项")
    private String businessAffiliation;

    @Schema(name = "发现人")
    private String findPeople;
    
    @Schema(name = "事实确认")
    private String status;
    
    @Schema(name = "是否整改")
    private String recStatus;
    
}
