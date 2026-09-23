package com.huabo.audit.oracle.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name="缺陷管理列表查询入参")
public class TblNbsjBugVo extends BaseVo{
	
	@Schema(name = "缺陷id")
    private BigDecimal bugid;
	
	@Schema(name = "缺陷编号")	
	private String bugnumber;
	
	@Schema(name = "缺陷级别")
    private String bugcriid;

    @Schema(name = "发现开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;
    
    @Schema(name = "发现结束时间 ")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;

    @Schema(name = "缺陷级别")
    private BigDecimal companyid;

    @Schema(name = "缺陷部门")
    private BigDecimal orgid;
    
    @Schema(name="缺陷名称")
    private String defectsname;
    
    @Schema(name = "是否使用密级 0 不使用；1 使用")
    private Integer useSecrect;
    
    @Schema(name = "创建人-密级用")
    private BigDecimal secrectStaff;
    
    @Schema(name = "当前用户所能查看密级数据的范围主键")
    private String secrectScopeIds;
    



}

