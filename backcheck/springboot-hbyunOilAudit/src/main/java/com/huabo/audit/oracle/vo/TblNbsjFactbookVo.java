package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="事实确认书列表查询入参")
public class TblNbsjFactbookVo extends BaseVo{
	@Schema(name = "编号")
    private String factcode;

    @Schema(name = "所属计划")
    private String factname;
    
    @Schema(hidden=true)
    private Integer projectid;
}