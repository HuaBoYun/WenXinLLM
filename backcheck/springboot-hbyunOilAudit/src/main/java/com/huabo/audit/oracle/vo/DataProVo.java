package com.huabo.audit.oracle.vo;

import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="项目资料准备列表查询入参")
public class DataProVo extends BaseVo{
	@Schema(name = "资料编号")
    private String datacode;

    @Schema(name = "资料名称")
    private String dataname;
    
}