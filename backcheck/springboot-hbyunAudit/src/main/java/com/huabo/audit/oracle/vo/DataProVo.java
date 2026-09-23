package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;

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

    @Schema(name = "项目id")
    private String projectId;
    @Schema(name = "组织id")
    private BigDecimal orgid;

    @Schema(name = "用户id")
    private BigDecimal staffid;

    @Schema(name = "是否使用密级 0 不使用；1 使用")
    private Integer useSecrect;
    
    @Schema(name = "创建人-密级用")
    private BigDecimal secrectStaff;
    
    @Schema(name = "当前用户所能查看密级数据的范围主键")
    private String secrectScopeIds;

    
}