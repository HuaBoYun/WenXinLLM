package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="疑点管理列表查询入参")
public class TblNbsjDoubtfulpointVo extends BaseVo{
	@Schema(name = "疑点编号")
    private String dpnumber;

    @Schema(name = "疑点名称")
    private String dpname;
    
    @Schema(hidden=true)
    private BigDecimal orgid;
    
    @Schema(name = "项目id")
    private BigDecimal projectid;
    
    //审计线索类型
    
    @Schema(name = "编制时间-开始")
    private String startDate;
    
    @Schema(name = "编制时间-结束")
    private String endDate;
}
