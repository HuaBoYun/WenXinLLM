package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

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

    @Schema(name = "项目id")
    private BigDecimal projectId;
    
    @Schema(name = "问题类型")
    private String internalType;
    
    
}
